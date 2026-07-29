package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.entity.DoctorLevelEntity;
import com.qst.medical.entity.TreatTypeEntity;
import com.qst.medical.service.DoctorService;
import com.qst.medical.vo.DoctorQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("")
    public Result<Map<String, Object>> list(DoctorQueryParam param) {
        return doctorService.getDoctorList(param);
    }

    @GetMapping("/levels")
    public Result<List<DoctorLevelEntity>> levels() {
        return doctorService.getDoctorLevels();
    }

    @GetMapping("/treat-types")
    public Result<List<TreatTypeEntity>> treatTypes() {
        return doctorService.getTreatTypes();
    }
}
