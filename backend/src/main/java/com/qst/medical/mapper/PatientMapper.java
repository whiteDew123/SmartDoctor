package com.qst.medical.mapper;

import com.qst.medical.entity.PatientEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PatientMapper {

    int insertPatient(PatientEntity patient);
}