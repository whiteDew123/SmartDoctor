package com.qst.medical.mapper;

import com.qst.medical.entity.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {

    Account selectByUname(String uname);

    Account selectById(Long id);

    int insert(Account account);
}