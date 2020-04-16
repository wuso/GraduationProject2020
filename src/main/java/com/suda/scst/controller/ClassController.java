package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.services.ClassService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.List;

@RestController
//设置想要跳转的父路径
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    //添加新班级
    @PostMapping(value = "/add")
    //接收post请求
    public void addClass(@RequestBody Class clazz) {
        classService.upsertClass(clazz);
        classService.addMajor(clazz);
    }

    //删除班级
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        classService.deleteByName(name);
        return "delete ok";
    }

    //编辑班级
    @PostMapping(value = "/edit")
    public String editByName(@NotNull @RequestBody Class origin) {
        //不可更改原节点的name的值
        Class clazz = classService.findByName(origin.getName());
        //修改原节点的信息
        clazz.setGrade(origin.getGrade());
        clazz.setTeacher(origin.getTeacher());
        clazz.setMajor(origin.getMajor());
        clazz.setClass_id(origin.getClass_id());
        //更新节点
        classService.upsertClass(clazz);
        return "edit ok";
    }

    //查询班级
    @GetMapping("/query")
    public Collection<Class> query(@RequestParam(value = "name") String name) {
        return classService.findByNameLike(name);
    }

    //poi导入
    @RequestMapping("/importList")
    public void importList(@RequestParam(value = "file") MultipartFile mFile) throws IOException {
        //CommonsMultipartFile cFile = (CommonsMultipartFile) mFile;
        //DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
        //InputStream inputStream = mFile.getInputStream();
//        1.通过流读取Excel文件
        //FileInputStream inputStream = new FileInputStream("C://Users/Odinnnnn/1586451847691.xls");
//        2.通过poi解析流 HSSFWorkbook 处理流得到的对象中 就封装了Excel文件所有的数据
        //HSSFWorkbook workbook = new HSSFWorkbook((InputStream) data);
        assert mFile != null;
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(mFile.getInputStream()));
        //HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//        3.从文件中获取表对象  getSheetAt通过下标获取
        HSSFSheet sheet = workbook.getSheetAt(0);
//        4.从表中获取到行数据  从第二行开始 到 最后一行  getLastRowNum() 获取最后一行的下标
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i <= lastRowNum; i++) {
//            通过下标获取行
            HSSFRow row = sheet.getRow(i);
//            从行中获取数据
            /**
             * getNumericCellValue() 获取数字
             * getStringCellValue 获取String
             */
            double id = row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            double grade = row.getCell(2).getNumericCellValue();
            String teacher = row.getCell(3).getStringCellValue();
            String major = row.getCell(4).getStringCellValue();
//            封装到对象中
            Class user = new Class();
            user.setClass_id((int) id);
            user.setName(name);
            user.setGrade((int) grade);
            user.setTeacher(teacher);
            user.setMajor(major);

            //查看数据库中是否有名字相同的教师，有则更新该节点，否则新建节点
            if(classService.findByName(name)!=null){
                editByName(user);
            }
            else{
                addClass(user);
            }
//            将对象添加数据库
            System.out.println(user.getName());
        }
    }

    //poi导出
    @RequestMapping("/exportList")
    public void exportList(HttpServletResponse response) throws IOException {
        String fileName = "测试"+System.currentTimeMillis()+".xls";
        String[] titles = {"班号","班级名","年级","班主任","专业"};
        List<Class> result = (List<Class>) classService.findByNameLike("**");
        // 方式一: 使用迭代器
//        for (Class aClass : result) {
//            System.out.println("打印字符: " + aClass.getName());
//        }
        /**
         * 先写入 标题栏数据
         */
        //1.创建文件对象   创建HSSFWorkbook只能够写出为xls格式的Excel
        //要写出 xlsx 需要创建为 XSSFWorkbook 两种Api基本使用方式一样
        HSSFWorkbook workbook = new HSSFWorkbook();

        //2.创建表对象
        HSSFSheet sheet = workbook.createSheet("classes");

        //3.创建标题栏（第一行）  参数为行下标  行下标从0开始
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) (20 * 30));
        //设置样式
        HSSFCellStyle setBorder = workbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 居中
        //4.在标题栏中写入数据
        for (int i = 0; i < titles.length; i++) {
         //创建单元格
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        /**
         * 写入用户数据
         */
        //5 创建行 如果是用户数据的集合 需要遍历
        HSSFRow row = sheet.createRow(1);

        for (int i = 0; i < result.size(); i++) {
            row = sheet.createRow(i + 1);
            //6 将用户数据写入到行中
            Class aClass = result.get(i);
            row.createCell(0).setCellValue(aClass.getClass_id());
            row.createCell(1).setCellValue(aClass.getName());
            row.createCell(2).setCellValue(aClass.getGrade());
            row.createCell(3).setCellValue(aClass.getTeacher());
            row.createCell(4).setCellValue(aClass.getMajor());
            row.setHeight((short) (20 * 30));
        }
        //文件保存到本地 参数为要写出的位置
        //workbook.write(new FileOutputStream("e://test/h1.xls"));
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            workbook.write(os);
            os.flush();
            os.close();
    }

//    @RequestMapping("/exportExcel")
//    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String dataTable = request.getParameter("dataTable");
//            String fileName = "测试"+System.currentTimeMillis()+".xls";
//            // 各浏览器基本都支持ISO编码
//            String userAgent = request.getHeader("User-Agent").toUpperCase();
//            if (userAgent.contains("TRIDENT") || userAgent.contains("EDGE")) {
//                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
//            } else if (userAgent.contains("MSIE")) {
//                fileName = new String(fileName.getBytes(), "ISO-8859-1");
//            } else {
//                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
//            }
//
//            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
//            OutputStream os = new BufferedOutputStream(response.getOutputStream());
//            response.setContentType("application/vnd.ms-excel;charset=gb2312");
//            //将excel写入到输出流中
//            HSSFWorkbook workbook = Html2Excel.table2Excel(dataTable);
//            // XSSFWorkbook workbook = XHtml2Excel.table2Excel(dataTable);
//            System.out.println("a ");
//            workbook.write(os);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("b");
//            //log.error("请求 exportExcel异常：{}", e.getMessage());
//        }
//    }

}
