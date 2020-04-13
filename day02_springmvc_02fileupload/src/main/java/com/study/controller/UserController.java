package com.study.controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    // 传统方式上传文件(借助:common-fileupload.jar)
    @RequestMapping("/upload1")
    public String fileUpload(HttpServletRequest request) throws Exception {
        System.out.println("文件上传...");
        //使用fileupload组件完成文件上传
        // 1 指定文件上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads");
        // 2 判断该路径是否存在
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        // 3 解析Request对象，获取上传的文件项
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = upload.parseRequest(request);
        // 4 遍历
        for (FileItem item:fileItems){
            // 进行判断，当前的item对象是否是上传文件项
            if (item.isFormField()){ // 若为true则为普通表单项目

            }else{ // 若为上传文件项
                // 获取到上传文件的名称
                String filename = item.getName();
                // 将文件的名称设置为唯一的uuid
                String uuid = UUID.randomUUID().toString().replace("-", "");
                filename = uuid + "_" + filename;
                item.write(new File(path, filename));
                // 删除临时文件
                item.delete();
            }
        }
        return "success";
    }

    // SpringMVC上传文件(借助文件解析器)
    @RequestMapping("/upload2")
                                                        // MultipartFile的变量名要与表单中的name字段一致
    public String fileUpload2(HttpServletRequest request, MultipartFile upload) throws Exception {
        System.out.println("SpringMVC文件上传...");
        String path = request.getSession().getServletContext().getRealPath("/uploads");
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        // 获取到上传文件的名称
        String filename = upload.getOriginalFilename();
        // 将文件的名称设置为唯一的uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + "_" + filename;
        upload.transferTo(new File(path, filename));
        return "success";
    }

    // SpringMVC跨服务器文件上传
    @RequestMapping("/upload3")
    // MultipartFile的变量名要与表单中的name字段一致
    public String fileUpload3(MultipartFile upload) throws Exception {
        // 定义上传文件的服务器的路径
        String path = "http://localhost:9091/file/uploads";
        // 获取到上传文件的名称
        String filename = upload.getOriginalFilename();
        // 将文件的名称设置为唯一的uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + "_" + filename;
        // 跨服务器上传
        // 1 创建客户端对象
        Client client = Client.create();
        // 2 和图片服务器进行连接
        WebResource resource = client.resource(path + "/" + filename);
        // 3 上传文件
        resource.put(upload.getBytes());
        return "success";
    }
}
