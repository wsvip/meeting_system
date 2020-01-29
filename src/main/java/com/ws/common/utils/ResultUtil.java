package com.ws.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回参数封装工具类
 */
public class ResultUtil {
    /**
     * success后返回的数据封装成map集合,code设置为0，表示成功
     * @param data 成功后需要返回的数据
     * @param msg 成功后需要返回的信息
     * @return Map
     */
    public static Map<Object,Object> success(Object data,String msg){
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("data",data);
        resultMap.put("msg",msg);
        return resultMap;
    }

    /**
     * error后返回的数据封装成map集合，如果code为空，则设置默认值 1，表示失败
     * @param code 失败后返回的状态码。
     * @param msg 失败后返回的信息
     * @return Map
     */
    public static Map<Object,Object> error(int code,String msg){
        HashMap<Object, Object> resultMap = new HashMap<>();
        if (0==code){
            resultMap.put("code",1);
        }else {
            resultMap.put("code",code);
        }
        resultMap.put("msg",msg);
        return  resultMap;
    }

    /**
     * warming后返回的数据疯转改成map集合，如果code为空，则设置code为-1，表示警告
     * @param code 警告状态码
     * @param msg 警告信息
     * @param data 需要返回的数据
     * @return Map
     */
    public static Map<Object,Object> warming(int code,String msg,Object data){
        HashMap<Object, Object> resultMap = new HashMap<>();
        if (0==code){
            resultMap.put("code",-1);
        }else {
            resultMap.put("code",code);
        }
        resultMap.put("data",data);
        resultMap.put("msg",msg);
        return resultMap;
    }

    /**
     * layui的分页数据返回集合，code为0表示成功，count为数据条数,data为所要分页的数据
     * @param code 状态码
     * @param msg 信息
     * @param count 数据量
     * @param data 数据
     * @return Map
     */
    public static Map<Object,Object> layuiPageData(int code,String msg,int count,Object data){
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        resultMap.put("count",count);
        resultMap.put("data",data);
        return resultMap;
    }
}
