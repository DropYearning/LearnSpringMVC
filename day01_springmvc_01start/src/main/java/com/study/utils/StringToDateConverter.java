package com.study.utils;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 把字符串转换为日期
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (source == null){
            throw new RuntimeException("没有传入日期字符串");
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
             return df.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException("输入的日期不是yyyy-MM-dd格式的");
        }
    }
}
