package com.qst.medical.mapper;

import com.qst.medical.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    Account selectByUname(String uname);

    Account selectById(Long id);

    /**
     * 根据手机号查询账号，用于新增医师时校验手机号唯一性。
     * 排除指定 id，可用于修改时排除自己。
     */
    Account selectByPhonenumber(@Param("phonenumber") String phonenumber, @Param("excludeId") Long excludeId);

    int insert(Account account);

    /**
     * 按主键更新账号基本信息（姓名/手机号/角色/更新时间）。
     */
    int updateById(Account account);

    /**
     * 重置指定账号的密码。
     */
    int updatePwd(@Param("id") Long id, @Param("pwd") String pwd);

    /**
     * 按主键删除账号。
     */
    int deleteById(Long id);
}
