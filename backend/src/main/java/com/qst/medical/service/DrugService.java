package com.qst.medical.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qst.medical.entity.Drug;
import com.qst.medical.entity.DrugPageInfo;
import com.qst.medical.mapper.DrugMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugService {

    @Autowired
    private DrugMapper drugMapper;

    public DrugPageInfo getDrugPageInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Drug> page = (Page<Drug>) drugMapper.selectAll();
        DrugPageInfo drugPageInfo = new DrugPageInfo();
        drugPageInfo.setTotal(page.getTotal());
        drugPageInfo.setList(page.getResult());
        return drugPageInfo;
    }

    public Drug getById(Long drugId) {
        return drugMapper.selectById(drugId);
    }
}
