package com.hjnu.Controller;

import com.hjnu.Common.*;
import lombok.extern.slf4j.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${ruijiwaimai.path}")
    String  path;

    /**
     * 上传文件到指定位置（本机）
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();//获得原始文件名称
        String  suffix=originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName=UUID.randomUUID().toString()+suffix;
        //判断目录是否存在
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        File picture=new File(path+fileName);
        if(!picture.exists()){
            fileName.replace(".jpg","png");
        }
        if(!picture.exists()){
            fileName.replace(".png","jpeg");
        }
        try {
            /**
             * file.transferTo方法会产生一个默认目录C:\Users\36017\AppData\Local\Temp，而且没法修改
             * 所有换方法FileUtils.copyInputStreamToFile
             */
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(path+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    @GetMapping("download")
    public void downloadImage(HttpServletResponse response,String name){
        byte[] bytes=new byte[1024];
        int len=0;
        try {
            //输入流,读取文件内容
            FileInputStream fileInputStream=new FileInputStream(new File(path+name));
            //设置的内容类型
            response.setContentType("image/jepg");
            //输出流，将读出的内容写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            while ((len=fileInputStream.read(bytes))!=-1){      //只要不等于-1，则还有内容
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
