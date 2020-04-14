package com.study.controller;

import com.study.domain.Account;
import com.study.service.IAccountService;
import com.study.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 账户Web控制器
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/findAll")
    public  String findAll(Model model){
        System.out.println("Controller中的查询所有账户的方法执行了");
        // 调用业务层的方法
        List<Account> list = accountService.findAll();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping("/save")
    /*借助Spring自动封装来自jsp页面表单的数据到实体类Account*/
    public void save(Account account, HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("Controller中的保存账户的方法执行了");
        // 调用业务层的方法
        accountService.saveAccount(account);
        // 增加用户之后重定向查询结果页面显示结果
        response.sendRedirect(request.getContextPath() + "/account/findAll");
        return;
    }
}
