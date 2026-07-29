package com.qst.medical.mapper;

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
}
