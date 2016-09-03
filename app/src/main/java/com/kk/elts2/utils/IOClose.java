package com.kk.elts2.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭IO的方法
 * Created by Administrator on 2016/9/2.
 */
public class IOClose  {
    public  static void  closeAll(Closeable ... ios){
        for (Closeable io:ios){
            try {
                if (io!=null)
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
