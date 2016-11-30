package com.yyHaker.semantic.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * FileUtils
 *
 * @author Le Yuan
 * @date 2016/11/8
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
            System.out.print(bos.toString());
            return bos.toString();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
