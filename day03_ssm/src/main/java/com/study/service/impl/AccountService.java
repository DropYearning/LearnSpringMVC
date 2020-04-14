package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import com.study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountService")
public class AccountService implements IAccountService {

    @Autowired
    private IAccountDao accountDao;

    public List<Account> findAll() {
        System.out.println("Service层 findAll()");
        return accountDao.findAll();
    }

    public void saveAccount(Account account) {

        System.out.println("Service层 saveAccount()");
        accountDao.saveAccount(account);
    }
}
