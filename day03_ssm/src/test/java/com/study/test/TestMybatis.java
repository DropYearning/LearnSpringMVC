package com.study.test;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestMybatis {

    @Test
    public void testFindAll() throws IOException {
        // 1 加载Mybatis配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2 创建SqlSessionFactory工厂对象
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3 创建SqlSession
        SqlSession sqlSession = factory.openSession();
        // 4 获取dao接口的代理对象
        IAccountDao accountDao = sqlSession.getMapper(IAccountDao.class);
        // 5 执行持久层操作
        List<Account> accounts = accountDao.findAll();
        for (Account account:accounts){
            System.out.println(account);
        }
        // 6 释放资源
        sqlSession.close();
        in.close();
    }

    @Test
    public void testSave() throws IOException {
        // 1 加载Mybatis配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2 创建SqlSessionFactory工厂对象
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3 创建SqlSession
        SqlSession sqlSession = factory.openSession();
        // 4 获取dao接口的代理对象
        IAccountDao accountDao = sqlSession.getMapper(IAccountDao.class);
        // 5 执行持久层操作
        Account newAccount = new Account("李白", 2000.0);
        accountDao.saveAccount(newAccount);
        // 6 提交事务
        sqlSession.commit();
        // 7 释放资源
        sqlSession.close();
        in.close();
    }
}
