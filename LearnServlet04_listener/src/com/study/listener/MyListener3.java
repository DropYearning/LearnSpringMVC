package com.study.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * application监听器类
 */
public class MyListener3 implements ServletContextListener, ServletRequestAttributeListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("MyListener3 contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("MyListener3 contextDestroyed");
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent event) {
        System.out.println("session中增加了一条数据" + event.getName() + ":" + event.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent event) {
        System.out.println("session中删除了一条数据" + event.getName() + ":" + event.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent event) {
        System.out.println("session中更新了一条数据" + event.getName() + ":" + event.getValue());
    }
}
