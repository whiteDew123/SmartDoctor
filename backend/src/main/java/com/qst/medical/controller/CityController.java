package com.qst.medical.controller;

import com.qst.medical.annotation.LogOperation;
import com.qst.medical.common.Result;
import com.qst.medical.entity.City;
import com.qst.medical.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * 查询所有城市信息
     */
    @GetMapping("/list")
    public Result<List<City>> list() {
        List<City> list = cityService.getAll();
        return Result.success(list);
    }

    /**
     * 根据ID查询城市信息
     */
    @GetMapping("/{cityId}")
    public Result<City> getById(@PathVariable Long cityId) {
        City city = cityService.getById(cityId);
        return Result.success(city);
    }

    /**
     * 添加城市信息
     */
    @PostMapping("/add")
    @LogOperation(value = "添加城市", operation = "ADD")
    public Result<Void> add(@RequestBody City city) {
        cityService.add(city);
        return Result.success();
    }

    /**
     * 删除城市信息（级联删除该城市下的所有医保政策）
     */
    @DeleteMapping("/{cityId}")
    @LogOperation(value = "删除城市", operation = "DELETE")
    public Result<Void> delete(@PathVariable Long cityId) {
        cityService.deleteCity(cityId);
        return Result.success();
    }

    /**
     * 查询城市是否存在
     */
    @GetMapping("/exists/{cityId}")
    public Result<Boolean> checkExists(@PathVariable Long cityId) {
        boolean exists = cityService.checkExists(cityId);
        return Result.success(exists);
    }
}