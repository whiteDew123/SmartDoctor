package com.qst.medical.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qst.medical.entity.Material;
import com.qst.medical.entity.MaterialPageInfo;
import com.qst.medical.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    public MaterialPageInfo getMaterialPageInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Material> page = (Page<Material>) materialMapper.selectAll();
        MaterialPageInfo materialPageInfo = new MaterialPageInfo();
        materialPageInfo.setTotal(page.getTotal());
        materialPageInfo.setList(page.getResult());
        return materialPageInfo;
    }

    public MaterialPageInfo searchMaterialByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Material> page = (Page<Material>) materialMapper.selectByKeyword(keyword);
        MaterialPageInfo materialPageInfo = new MaterialPageInfo();
        materialPageInfo.setTotal(page.getTotal());
        materialPageInfo.setList(page.getResult());
        return materialPageInfo;
    }

    public Material getById(Long id) {
        return materialMapper.selectById(id);
    }
}