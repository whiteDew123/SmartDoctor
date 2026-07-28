package com.qst.medical.controller;

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
}