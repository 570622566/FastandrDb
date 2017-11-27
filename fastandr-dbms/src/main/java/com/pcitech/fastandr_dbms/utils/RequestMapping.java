package com.pcitech.fastandr_dbms.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author laijian
 * @version 2017/11/24
 * @Copyright (C)下午10:51 , www.hotapk.cn
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}
