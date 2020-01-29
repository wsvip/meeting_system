package com.ws.common.utils;

import java.util.UUID;

public class  UuidUtils {
    public static String getUuid(){
        String uuidToStr = UUID.randomUUID().toString();
        String uuid = uuidToStr.replaceAll("-", "");
        return uuid;
    }
}
