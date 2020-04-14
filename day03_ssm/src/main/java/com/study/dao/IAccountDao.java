package com.study.dao;

import com.study.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户持久层接口（使用Mybatis框架只需写接口即可，不用再写实现类）
 */
@Repository
public interface IAccountDao {

    @Select("select * from account")
    List<Account> findAll(); // 查询所有


    @Insert("insert into account (name, money) values (#{name}, #{money})")
    void saveAccount(Account account); // 保存账户信息

}
