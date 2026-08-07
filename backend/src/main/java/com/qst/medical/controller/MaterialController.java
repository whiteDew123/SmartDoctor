package com.qst.medical.controller;

import com.qst.medical.annotation.LogOperation;
import com.qst.medical.common.Result;
import com.qst.medical.entity.Material;
import com.qst.medical.entity.MaterialPageInfo;
import com.qst.medical.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/{pageNum}/{pageSize}")
    public Result<MaterialPageInfo> getMaterialPageInfo(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        MaterialPageInfo materialPageInfo = materialService.getMaterialPageInfo(pageNum, pageSize);
        return Result.success(materialPageInfo);
    }

    @GetMapping("/search/{pageNum}/{pageSize}")
    public Result<MaterialPageInfo> searchMaterial(@PathVariable Integer pageNum, @PathVariable Integer pageSize, 
                                                   @RequestParam String keyword) {
        MaterialPageInfo materialPageInfo = materialService.searchMaterialByKeyword(pageNum, pageSize, keyword);
        return Result.success(materialPageInfo);
    }

    @GetMapping("/detail/{id}")
    public Result<Material> getById(@PathVariable Long id) {
        Material material = materialService.getById(id);
        return Result.success(material);
    }

    @PostMapping("/add")
    @LogOperation(value = "添加医疗物资", operation = "ADD")
    public Result<Integer> addMaterial(@RequestBody Material material) {
        int result = materialService.addMaterial(material);
        if (result > 0) {
            return Result.success(result);
        } else {
            return Result.error("添加失败");
        }
    }

    @PutMapping("/update")
    @LogOperation(value = "修改医疗物资", operation = "UPDATE")
    public Result<Integer> updateMaterial(@RequestBody Material material) {
        int result = materialService.updateMaterial(material);
        if (result > 0) {
            return Result.success(result);
        } else {
            return Result.error("修改失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    @LogOperation(value = "删除医疗物资", operation = "DELETE")
    public Result<Integer> deleteMaterial(@PathVariable Long id) {
        int result = materialService.deleteMaterial(id);
        if (result > 0) {
            return Result.success(result);
        } else {
            return Result.error("删除失败");
        }
    }
}