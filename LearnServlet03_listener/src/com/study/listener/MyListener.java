package com.study.listener;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * request监听器类:可以监听request的创建和销毁、request域中属性的增删改、
 */
public class MyListener implements ServletRequestListener, ServletRequestAttributeListener {

    // request被销毁时执行
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("MyListener requestDestroyed");
    }

    // request被创建时执行
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("MyListener requestInitialized");
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        // ServletRequestAttributeEvent对象可以帮助获取域中属性
        System.out.println("MyListener Request域中增加了一条数据：" +servletRequestAttributeEvent.getName() + ":" + servletRequestAttributeEvent.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("MyListener Request域中移除了一条数据："  +servletRequestAttributeEvent.getName() + ":" + servletRequestAttributeEvent.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("MyListener Request域中替换了一条数据：" +servletRequestAttributeEvent.getName() + ":" + servletRequestAttributeEvent.getValue());
    }
}
