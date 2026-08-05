package com.qst.medical.service;

import com.qst.medical.entity.CompanyPolicy;
import com.qst.medical.entity.DoctorLevelEntity;
import com.qst.medical.entity.TreatTypeEntity;
import com.qst.medical.mapper.*;
import com.qst.medical.model.MedicalPolicyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugcompanyMapper drugcompanyMapper;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private MedicalPolicyMapper medicalPolicyMapper;

    @Autowired
    private CompanyPolicyMapper companyPolicyMapper;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> totalCounts = new HashMap<>();
        totalCounts.put("doctorCount", doctorMapper.countAllDoctors());
        totalCounts.put("drugCount", drugMapper.countAll());
        totalCounts.put("companyCount", drugcompanyMapper.countAll());
        totalCounts.put("pharmacyCount", saleMapper.countAll());
        result.put("totalCounts", totalCounts);

        List<DoctorLevelEntity> levels = doctorMapper.selectAllDoctorLevels();
        List<Map<String, Object>> doctorLevelStats = new ArrayList<>();
        for (DoctorLevelEntity level : levels) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", level.getName());
            item.put("count", doctorMapper.countByLevelId(level.getId()));
            doctorLevelStats.add(item);
        }
        result.put("doctorLevelStats", doctorLevelStats);

        List<TreatTypeEntity> treatTypes = doctorMapper.selectAllTreatTypes();
        List<Map<String, Object>> departmentStats = new ArrayList<>();
        for (TreatTypeEntity type : treatTypes) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getName());
            item.put("count", doctorMapper.countByTypeId(type.getId()));
            departmentStats.add(item);
        }
        result.put("departmentStats", departmentStats);

        List<MedicalPolicyModel> latestMedicalPolicies = medicalPolicyMapper.selectLatest(5);
        result.put("latestMedicalPolicies", latestMedicalPolicies);

        List<CompanyPolicy> latestCompanyPolicies = companyPolicyMapper.selectLatest(5);
        result.put("latestCompanyPolicies", latestCompanyPolicies);

        return result;
    }
}
