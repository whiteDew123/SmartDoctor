package com.qst.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qst.medical.entity.Drugcompany;
import com.qst.medical.mapper.DrugcompanyMapper;
import com.qst.medical.param.DrugcompanyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 医药公司业务逻辑层
 * 负责医药公司的分页查询、详情查询
 */
@Service
public class DrugcompanyService {

    @Autowired
    private DrugcompanyMapper drugcompanyMapper;

    /**
     * 分页查询医药公司
     * 使用 PageHelper 插件实现物理分页，支持按公司名称和联系电话模糊搜索
     *
     * @param param 查询参数（页码、每页条数、公司名称、联系电话）
     * @return PageInfo 分页对象，包含总条数、总页数、当前页数据等
     */
    public PageInfo<Drugcompany> getByPage(DrugcompanyParam param) {
        if (param.getPageNum() == null) {
            param.setPageNum(1);
        }
        if (param.getPageSize() == null) {
            param.setPageSize(10);
        }
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<Drugcompany> list = drugcompanyMapper.selectByPage(param.getCompanyName(), param.getCompanyPhone());
        return new PageInfo<>(list);
    }

    /**
     * 根据 ID 查询单个医药公司信息
     *
     * @param companyId 医药公司主键 ID
     * @return 医药公司实体，未找到则返回 null
     */
    public Drugcompany getById(Long companyId) {
        return drugcompanyMapper.selectById(companyId);
    }

    /**
     * 添加医药公司
     *
     * @param drugcompany 医药公司实体，companyId 由数据库自增生成
     * @return 添加后的医药公司（含自增 ID）
     */
    @Transactional(rollbackFor = Exception.class)
    public Drugcompany save(Drugcompany drugcompany) {
        drugcompany.setCreatetime(LocalDateTime.now());
        drugcompany.setUpdatetime(LocalDateTime.now());
        drugcompanyMapper.insert(drugcompany);
        return drugcompany;
    }

    /**
     * 修改医药公司
     *
     * @param drugcompany 医药公司实体，companyId 为必填字段用于定位记录
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Drugcompany drugcompany) {
        drugcompany.setUpdatetime(LocalDateTime.now());
        return drugcompanyMapper.update(drugcompany);
    }

    /**
     * 根据 ID 删除医药公司
     *
     * @param companyId 医药公司主键 ID
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long companyId) {
        return drugcompanyMapper.deleteById(companyId);
    }
}
