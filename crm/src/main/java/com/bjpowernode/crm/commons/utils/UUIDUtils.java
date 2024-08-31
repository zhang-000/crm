package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

public class UUIDUtils {
    //生成一个UUID（Universally Unique Identifier)
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
