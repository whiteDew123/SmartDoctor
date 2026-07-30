package com.qst.medical.mapper;

import com.qst.medical.entity.DoctorEntity;
import com.qst.medical.entity.DoctorLevelEntity;
import com.qst.medical.entity.TreatTypeEntity;
import com.qst.medical.vo.Doctor;
import com.qst.medical.vo.DoctorQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoctorMapper {

    List<Doctor> selectDoctorList(@Param("param") DoctorQueryParam param, @Param("offset") Integer offset);

    Long countDoctor(@Param("param") DoctorQueryParam param);

    List<DoctorLevelEntity> selectAllDoctorLevels();

    List<TreatTypeEntity> selectAllTreatTypes();

    /**
     * 根据主键查询医师完整信息（含关联账号信息）。
     */
    Doctor selectDoctorById(@Param("id") Long id);

    int insertDoctor(DoctorEntity doctor);

    int updateDoctor(DoctorEntity doctor);

    int deleteDoctor(@Param("id") Long id);
}
