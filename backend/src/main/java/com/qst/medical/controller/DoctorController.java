package com.qst.medical.controller;

import com.qst.medical.annotation.LogOperation;
import com.qst.medical.common.Result;
import com.qst.medical.entity.DoctorLevelEntity;
import com.qst.medical.entity.TreatTypeEntity;
import com.qst.medical.service.DoctorService;
import com.qst.medical.vo.DoctorQueryParam;
import com.qst.medical.vo.DoctorSaveParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("")
    @LogOperation(value = "添加医生", operation = "ADD")
    public Result<DoctorSaveParam> add(@RequestBody DoctorSaveParam param) {
        return doctorService.addDoctor(param);
    }

    @PutMapping("/{id}")
    @LogOperation(value = "修改医生信息", operation = "UPDATE")
    public Result<DoctorSaveParam> update(@PathVariable Long id, @RequestBody DoctorSaveParam param) {
        return doctorService.updateDoctor(id, param);
    }

    @DeleteMapping("/{id}")
    @LogOperation(value = "删除医生", operation = "DELETE")
    public Result<Void> delete(@PathVariable Long id) {
        return doctorService.deleteDoctor(id);
    }

    @PutMapping("/{id}/reset-password")
    @LogOperation(value = "重置医生密码", operation = "UPDATE")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestParam String pwd) {
        return doctorService.resetPassword(id, pwd);
    }
}
