package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Student;
import com.suda.scst.domain.Teacher;
import com.suda.scst.services.TeacherService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

@RestController
//设置想要跳转的父路径
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    //添加新教师
    @PostMapping(value = "/add")
    //接收post请求
    public void addTeacher(@RequestBody Teacher teacher) {
        teacherService.upsertTeacher(teacher);
        teacherService.addColleague(teacher);
    }

    //删除教师
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        teacherService.deleteByName(name);
        return "delete ok";
    }

    //编辑教师
    @PostMapping(value = "/edit")
    public String editByName(@RequestBody Teacher origin) {
        //不可更改原节点的name的值
        Teacher teacher = teacherService.findByName(origin.getName());
        //修改原节点的信息
        teacher.setCourse(origin.getCourse());
        teacher.setGender(origin.getGender());
        teacher.setTitle(origin.getTitle());
        teacher.setTeacher_id(origin.getTeacher_id());
        //更新节点
        teacherService.upsertTeacher(teacher);
        return "edit ok";
    }

    //查询教师
    @GetMapping("/query")
    public Collection<Teacher> query(@RequestParam(value = "name") String name) {
        return teacherService.findByNameLike(name);
    }

    //poi导入
    @RequestMapping("/importList")
    public void importList(@RequestParam(value = "file") MultipartFile mFile) throws IOException {
//        1.通过流读取Excel文件
        assert mFile != null;
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(mFile.getInputStream()));
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
            String gender = row.getCell(2).getStringCellValue();
            String title = row.getCell(3).getStringCellValue();
            String course = row.getCell(4).getStringCellValue();

//            封装到对象中
            Teacher user = new Teacher();
            user.setTeacher_id((int) id);
            user.setName(name);
            user.setGender(gender);
            user.setTitle(title);
            user.setCourse(course);

            //查看数据库中是否有名字相同的教师，有则更新该节点，否则新建节点
            if(teacherService.findByName(name)!=null){
                editByName(user);
            }
            else{
                addTeacher(user);
            }
//            将对象添加数据库
            System.out.println(user.getName());
        }
    }
    //poi导出
    @RequestMapping("/exportList")
    public void exportList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "测试"+System.currentTimeMillis()+".xls";
        String[] titles = {"工号","姓名","性别","职称","教授课程"};
        List<Teacher> result = (List<Teacher>) teacherService.findByNameLike("**");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("teachers");
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) (20 * 30));
        for (int i = 0; i < titles.length; i++) {
            //创建单元格
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        HSSFRow row = sheet.createRow(1);
        for (int i = 0; i < result.size(); i++) {
            row = sheet.createRow(i + 1);
            Teacher aTeacher = result.get(i);
            row.createCell(0).setCellValue(aTeacher.getTeacher_id());
            row.createCell(1).setCellValue(aTeacher.getName());
            row.createCell(2).setCellValue(aTeacher.getGender());
            row.createCell(3).setCellValue(aTeacher.getTitle());
            row.createCell(4).setCellValue(aTeacher.getCourse());
            row.setHeight((short) (20 * 30));
        }
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
        workbook.write(os);
        os.flush();
        os.close();
    }

}
