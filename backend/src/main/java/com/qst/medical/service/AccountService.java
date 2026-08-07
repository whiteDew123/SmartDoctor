package com.qst.medical.service;

import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.DoctorEntity;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.mapper.AccountMapper;
import com.qst.medical.mapper.DoctorMapper;
import com.qst.medical.vo.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account login(String uname, String pwd) {
        Account account = accountMapper.selectByUname(uname);
        if (account != null && passwordEncoder.matches(pwd, account.getPwd())) {
            return account;
        }
        return null;
    }

    public void register(Account account) {
        account.setPwd(passwordEncoder.encode(account.getPwd()));
        accountMapper.insert(account);
    }

    /**
     * 登录页注册：支持医生(utype=2)与患者(utype=3)。
     * - 不允许注册管理员(utype=1)
     * - 医生注册需同步创建 doctor 记录，使用事务保证一致
     * - 患者注册仅创建 account 记录
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> registerByParam(RegisterParam param) {
        if (param.getUtype() == null || "1".equals(param.getUtype())) {
            return Result.error("不允许注册管理员账号");
        }
        if (!"2".equals(param.getUtype()) && !"3".equals(param.getUtype())) {
            return Result.error("角色类型非法");
        }
        if (param.getUname() == null || param.getUname().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (param.getPwd() == null || param.getPwd().isEmpty()) {
            return Result.error("密码不能为空");
        }
        if (param.getRealname() == null || param.getRealname().trim().isEmpty()) {
            return Result.error("姓名不能为空");
        }
        if (param.getPhonenumber() == null || param.getPhonenumber().trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }

        // 用户名唯一性校验
        Account existedByName = accountMapper.selectByUname(param.getUname().trim());
        if (existedByName != null) {
            return Result.error("用户名已被占用");
        }
        // 手机号唯一性校验
        Account existedByPhone = accountMapper.selectByPhonenumber(param.getPhonenumber(), null);
        if (existedByPhone != null) {
            return Result.error("该手机号已被注册");
        }

        // 医生注册必填字段校验
        if ("2".equals(param.getUtype())) {
            if (param.getHospital() == null || param.getHospital().trim().isEmpty()) {
                return Result.error("所属医院不能为空");
            }
            if (param.getLevelId() == null) {
                return Result.error("医师级别不能为空");
            }
            if (param.getTypeId() == null) {
                return Result.error("诊治类型不能为空");
            }
        }

        // 1. 创建账户
        Account account = new Account();
        account.setRealname(param.getRealname().trim());
        account.setUname(param.getUname().trim());
        account.setPwd(passwordEncoder.encode(param.getPwd()));
        account.setPhonenumber(param.getPhonenumber().trim());
        account.setUtype(param.getUtype());
        accountMapper.insert(account);

        // 2. 医生需同步写入 doctor 记录
        if ("2".equals(param.getUtype())) {
            DoctorEntity doctor = new DoctorEntity();
            doctor.setAccountId(account.getId());
            doctor.setAge(param.getAge());
            doctor.setSex(param.getSex());
            doctor.setHospital(param.getHospital());
            doctor.setLevelId(param.getLevelId());
            doctor.setTypeId(param.getTypeId());
            doctorMapper.insertDoctor(doctor);
        }

        return Result.success();
    }

    public Account getById(Long id) {
        return accountMapper.selectById(id);
    }

    /**
     * 忘记密码重置：通过用户名+手机号验证身份后重置密码。
     */
    public Result<Void> resetPasswordByPhone(String uname, String phonenumber, String newPwd) {
        if (uname == null || uname.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (phonenumber == null || phonenumber.trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        if (newPwd == null || newPwd.isEmpty()) {
            return Result.error("新密码不能为空");
        }
        if (newPwd.length() < 6) {
            return Result.error("新密码至少6位");
        }

        Account account = accountMapper.selectByUname(uname.trim());
        if (account == null) {
            return Result.error("用户不存在");
        }
        if (!phonenumber.trim().equals(account.getPhonenumber())) {
            return Result.error("用户名与手机号不匹配");
        }

        accountMapper.updatePwd(account.getId(), passwordEncoder.encode(newPwd));

        return Result.success();
    }

    @Override
    public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
        Account account = accountMapper.selectByUname(uname);
        if (account == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new MyUserDetails(account);
    }
}