package com.qst.medical.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qst.medical.entity.Drug;
import com.qst.medical.entity.DrugPageInfo;
import com.qst.medical.mapper.DrugMapper;
import com.qst.medical.mapper.DrugSaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DrugService {

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugSaleMapper drugSaleMapper;

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

    @Transactional
    public Long addDrug(Drug drug, List<Long> saleIds) {
        drugMapper.insert(drug);
        Long drugId = drug.getDrugId();
        if (saleIds != null && !saleIds.isEmpty()) {
            drugSaleMapper.batchInsert(drugId, saleIds);
        }
        return drugId;
    }

    @Transactional
    public void updateDrug(Drug drug, List<Long> saleIds) {
        drugMapper.updateById(drug);
        Long drugId = drug.getDrugId();
        drugSaleMapper.deleteByDrugId(drugId);
        if (saleIds != null && !saleIds.isEmpty()) {
            drugSaleMapper.batchInsert(drugId, saleIds);
        }
    }

    @Transactional
    public void deleteDrug(Long drugId) {
        drugSaleMapper.deleteByDrugId(drugId);
        drugMapper.deleteById(drugId);
    }
}
