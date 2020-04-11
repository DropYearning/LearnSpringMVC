package com.study.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet与线程
 */
@WebServlet(name = "ServletDemoThread")
public class ServletDemoThread extends HttpServlet {
    private String message;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        message = request.getParameter("message");
        System.out.println("修改实例变量message的值为" + message);
        PrintWriter printWriter = response.getWriter();
        try {
            System.out.println("Thread into sleeping");
            Thread.sleep(5000);
            System.out.println("Thread out of sleeping");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printWriter.write(message);
        System.out.println("读取实例变量message的值为" + message);
    }
}
