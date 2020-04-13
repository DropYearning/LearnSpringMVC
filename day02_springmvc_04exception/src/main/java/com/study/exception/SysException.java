package com.study.exception;

/**
 * 自定义的异常类
 */
public class SysException extends Exception {
    private String msg; // 提示信息

    public SysException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
