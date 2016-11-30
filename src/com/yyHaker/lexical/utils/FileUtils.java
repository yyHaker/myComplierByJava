package com.yyHaker.lexical.utils;

import java.io.*;

/**
 * the tool class of read and write file
 *
 * @author yyHaker
 * @create 2016-10-05-9:00
 */
public class FileUtils {

    /**
     *读取文件，返回String
     * @param file  文件对象
     * @return  String
     */
    public static  String  readFromFile(File file) {
        try {
            FileInputStream fis=new FileInputStream(file);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int length=-1;
            while((length=fis.read(buffer))!=-1){
                bos.write(buffer,0,length);
            }
            bos.close();
            fis.close();
            return bos.toString();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
      }

    /**
     * 将指定字符串写到文件中
     * @param text 字符串
     * @param file   文件对象
     */
    public static void  writeToFile(String text,File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
