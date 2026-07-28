package com.qst.medical.controller;

import com.qst.medical.common.DrugResult;
import com.qst.medical.entity.Drug;
import com.qst.medical.entity.DrugPageInfo;
import com.qst.medical.entity.DrugRequest;
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

    @PostMapping
    public DrugResult addDrug(@RequestBody DrugRequest drugRequest) {
        try {
            Drug drug = new Drug();
            drug.setDrugName(drugRequest.getDrugName());
            drug.setDrugInfo(drugRequest.getDrugInfo());
            drug.setDrugEffect(drugRequest.getDrugEffect());
            drug.setDrugImg(drugRequest.getDrugImg());
            drug.setPublisher(drugRequest.getDrugPublisher());

            Long drugId = drugService.addDrug(drug, drugRequest.getSaleIds());

            Integer pages = (int) Math.ceil((double) (drugService.getDrugPageInfo(1, 1).getTotal() + 1) / 10);
            return DrugResult.success("添加成功", pages);
        } catch (Exception e) {
            return DrugResult.error("添加失败：" + e.getMessage());
        }
    }

    @PutMapping("/{drugId}")
    public DrugResult updateDrug(@PathVariable Long drugId, @RequestBody DrugRequest drugRequest) {
        try {
            Drug drug = new Drug();
            drug.setDrugId(drugId);
            drug.setDrugName(drugRequest.getDrugName());
            drug.setDrugInfo(drugRequest.getDrugInfo());
            drug.setDrugEffect(drugRequest.getDrugEffect());
            drug.setDrugImg(drugRequest.getDrugImg());
            drug.setPublisher(drugRequest.getDrugPublisher());

            drugService.updateDrug(drug, drugRequest.getSaleIds());

            DrugPageInfo drugPageInfo = drugService.getDrugPageInfo(1, 10);
            Integer pages = (int) Math.ceil((double) drugPageInfo.getTotal() / 10);
            return DrugResult.success("修改成功", pages);
        } catch (Exception e) {
            return DrugResult.error("修改失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{drugId}")
    public DrugResult deleteDrug(@PathVariable Long drugId) {
        try {
            drugService.deleteDrug(drugId);
            DrugPageInfo drugPageInfo = drugService.getDrugPageInfo(1, 10);
            Integer pages = (int) Math.ceil((double) drugPageInfo.getTotal() / 10);
            return DrugResult.success("删除成功", pages);
        } catch (Exception e) {
            return DrugResult.error("删除失败：" + e.getMessage());
        }
    }
}
