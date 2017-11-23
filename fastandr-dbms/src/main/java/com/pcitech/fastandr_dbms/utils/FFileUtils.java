package com.pcitech.fastandr_dbms.utils;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author laijian
 * @version 2017/9/11
 * @Copyright (C)下午12:02 , www.hotapk.cn
 * 文件操作工具类
 */
public class FFileUtils {

    /**
     * 文件转InputStream
     *
     * @param absPath
     * @return
     */
    public static InputStream file2Inp(String absPath) {
        File file = new File(absPath);
        if (!file.exists()) {
            return null;
        }
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return is;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * InputStream 转字符串
     */
    public static String readInp(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            int len1;
            while ((len1 = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len1);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException var5) {
        }

        return outputStream.toString();
    }

}
