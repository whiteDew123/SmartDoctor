package com.qst.medical.controller;

import com.qst.medical.common.DrugResult;
import com.qst.medical.entity.DrugPageInfo;
import com.qst.medical.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping("/{pageNum}/{pageSize}")
    public DrugResult getDrugPageInfo(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        DrugPageInfo drugPageInfo = drugService.getDrugPageInfo(pageNum, pageSize);
        return DrugResult.success(drugPageInfo);
    }
}
