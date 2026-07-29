package com.qst.medical.service;

import com.qst.medical.common.Result;
import com.qst.medical.entity.DoctorLevelEntity;
import com.qst.medical.entity.TreatTypeEntity;
import com.qst.medical.mapper.DoctorMapper;
import com.qst.medical.vo.Doctor;
import com.qst.medical.vo.DoctorQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

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
}
