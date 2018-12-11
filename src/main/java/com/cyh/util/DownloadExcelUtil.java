package com.cyh.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.cyh.exception.MyException;

public class DownloadExcelUtil {

    public static void fileDownload(HttpServletResponse response, String fileName, String filePath) throws MyException {
        try {
            response.setCharacterEncoding("UTF-8");
            // 第一步：设置响应类型
            response.setContentType("application/force-download");// 应用程序强制下载
            // 第二读取文件
            InputStream in = new FileInputStream(filePath +System.getProperty("file.separator")+ fileName);
            // 设置响应头，对文件进行url编码
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(in.available());

            // 第三步：老套路，开始copy
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("下载文件出错！");
        }
    }
}