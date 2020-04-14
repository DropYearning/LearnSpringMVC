package com.study.service;

import com.study.domain.Account;

import java.util.List;

public interface IAccountService {
    List<Account> findAll(); // 查询所有

    void saveAccount(Account account); // 保存账户信息
}
