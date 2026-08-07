package com.qst.medical.service;

import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.DoctorEntity;
import com.qst.medical.entity.DoctorLevelEntity;
import com.qst.medical.entity.TreatTypeEntity;
import com.qst.medical.mapper.AccountMapper;
import com.qst.medical.mapper.DoctorMapper;
import com.qst.medical.vo.Doctor;
import com.qst.medical.vo.DoctorQueryParam;
import com.qst.medical.vo.DoctorSaveParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Result<Map<String, Object>> getDoctorList(DoctorQueryParam param) {
        Integer offset = (param.getPn() - 1) * param.getSize();
        List<Doctor> list = doctorMapper.selectDoctorList(param, offset);
        Long total = doctorMapper.countDoctor(param);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);

        return Result.success(result);
    }

    public Result<List<DoctorLevelEntity>> getDoctorLevels() {
        return Result.success(doctorMapper.selectAllDoctorLevels());
    }

    public Result<List<TreatTypeEntity>> getTreatTypes() {
        return Result.success(doctorMapper.selectAllTreatTypes());
    }

    /**
     * 新增医师：先创建用户账户（角色=医生），再写入 doctor 记录。
     * 一个医师对应一个用户账户，因此需要事务保证同步。
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<DoctorSaveParam> addDoctor(DoctorSaveParam param) {
        if (param.getName() == null || param.getName().isEmpty()) {
            return Result.error("医师姓名不能为空");
        }
        if (param.getPhoneNumber() == null || param.getPhoneNumber().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        if (param.getPwd() == null || param.getPwd().isEmpty()) {
            return Result.error("密码不能为空");
        }

        // 手机号唯一性校验
        Account existed = accountMapper.selectByPhonenumber(param.getPhoneNumber(), null);
        if (existed != null) {
            return Result.error("该手机号已被注册");
        }

        // 1. 新建账户（医师角色 utype=2）
        Account account = new Account();
        account.setRealname(param.getName());
        account.setUname(param.getPhoneNumber()); // 用手机号作为登录用户名
        account.setPwd(passwordEncoder.encode(param.getPwd()));
        account.setPhonenumber(param.getPhoneNumber());
        account.setUtype("2");
        accountMapper.insert(account);

        // 2. 新建医师信息
        DoctorEntity doctor = new DoctorEntity();
        doctor.setAccountId(account.getId());
        doctor.setAge(param.getAge());
        doctor.setSex(param.getSex());
        doctor.setHospital(param.getHospital());
        doctor.setLevelId(param.getLevelId());
        doctor.setTypeId(param.getTypeId());
        doctorMapper.insertDoctor(doctor);

        // 回填新生成的 id 供前端使用
        param.setAccountId(account.getId());
        param.setPwd("");

        return Result.success(param);
    }

    /**
     * 修改医师：同步更新账户基础信息（姓名/手机号/角色）与 doctor 扩展信息。
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<DoctorSaveParam> updateDoctor(Long id, DoctorSaveParam param) {
        Doctor existed = doctorMapper.selectDoctorById(id);
        if (existed == null) {
            return Result.error("医师不存在");
        }
        if (param.getAccountId() == null) {
            return Result.error("账户id不能为空");
        }

        // 手机号唯一性校验（排除自己）
        if (param.getPhoneNumber() != null && !param.getPhoneNumber().isEmpty()) {
            Account phoneOwner = accountMapper.selectByPhonenumber(param.getPhoneNumber(), param.getAccountId());
            if (phoneOwner != null) {
                return Result.error("该手机号已被其他账户使用");
            }
        }

        // 1. 更新账户
        Account account = new Account();
        account.setId(param.getAccountId());
        account.setRealname(param.getName());
        if (param.getPhoneNumber() != null && !param.getPhoneNumber().isEmpty()) {
            account.setUname(param.getPhoneNumber()); // 用户名跟随手机号
            account.setPhonenumber(param.getPhoneNumber());
        }
        account.setUtype("2");
        accountMapper.updateById(account);

        // 2. 更新医师
        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(id);
        doctor.setAge(param.getAge());
        doctor.setSex(param.getSex());
        doctor.setHospital(param.getHospital());
        doctor.setLevelId(param.getLevelId());
        doctor.setTypeId(param.getTypeId());
        doctorMapper.updateDoctor(doctor);

        return Result.success();
    }

    /**
     * 删除医师：先删 doctor 记录，再删关联账户。
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteDoctor(Long id) {
        Doctor existed = doctorMapper.selectDoctorById(id);
        if (existed == null) {
            return Result.error("医师不存在");
        }
        doctorMapper.deleteDoctor(id);
        if (existed.getAccountId() != null) {
            accountMapper.deleteById(existed.getAccountId());
        }
        return Result.success();
    }

    /**
     * 重置医师登录密码。
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> resetPassword(Long id, String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            return Result.error("新密码不能为空");
        }
        Doctor existed = doctorMapper.selectDoctorById(id);
        if (existed == null) {
            return Result.error("医师不存在");
        }
        if (existed.getAccountId() == null) {
            return Result.error("该医师未关联账户");
        }
        accountMapper.updatePwd(existed.getAccountId(), passwordEncoder.encode(pwd));

        return Result.success();
    }
}
