package com.pcitech.fastandr_dbms;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.pcitech.fastandr_dbms.bean.ResponseData;
import com.pcitech.fastandr_dbms.utils.FConstant;
import com.pcitech.fastandr_dbms.utils.NanoHTTPD;
import com.pcitech.fastandr_dbms.utils.RequestMapping;

import java.util.Map;

/**
 * @author laijian
 * @version 2017/11/13
 * @Copyright (C)上午10:17 , www.hotapk.cn
 * sql内网查询服务
 */
public class FDbNetServer extends NanoHTTPD {
    private java.lang.reflect.Method[] methods = FDbController.class.getDeclaredMethods();

    public FDbNetServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String file_name = session.getUri().substring(1);
        if (TextUtils.isEmpty(file_name) || file_name.endsWith("index.html")
                || file_name.contains("css/") || file_name.contains("js/")
                || file_name.contains("fonts/")
                ) {
            if (TextUtils.isEmpty(file_name)) {
                file_name = "index.html";
            }
            return newFixedLengthResponse(Response.Status.OK, detectMimeType(file_name), SqlService.index(file_name));
        } else {
            return responseData(session, file_name);
        }
    }

    /**
     * 解析注解
     */
    private Response responseData(IHTTPSession session, String file_name) {
        Response response = null;
        //获得成员变量
        try {
            for (java.lang.reflect.Method method : methods) {
                //判断注解
                if (method.getAnnotations() != null) {
                    //确定注解类型
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        //允许修改反射属性
                        method.setAccessible(true);
                        RequestMapping getViewTo = method.getAnnotation(RequestMapping.class);
                        if (getViewTo.value().equals(file_name)) {
                            Class cla = FDbController.class;
                            Object obj = cla.newInstance();
                            Class<?>[] parameterTypes = method.getParameterTypes(); //获得参数类型
                            Object[] objects = new Object[parameterTypes.length];
                            if (parameterTypes.length <= 0) {
                                response = (Response) method.invoke(obj);
                                break;
                            } else {
                                for (int i = 0; i < parameterTypes.length; i++) {
                                    if (parameterTypes[i] == Map.class) {
                                        objects[i] = session.getParms();
                                    } else if (parameterTypes[i] == IHTTPSession.class) {
                                        objects[i] = session;
                                    }
                                }

                                response = (Response) method.invoke(obj, objects);
                                break;

                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            ResponseData responseData = new ResponseData();
            responseData.setError(e.getMessage());
            responseData.setSuccessful(false);
            response = newFixedLengthResponse(Response.Status.OK, detectMimeType(file_name), new Gson().toJson(responseData));
        }
        return response;
    }


    public static String detectMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }


}
