package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Major;
import com.suda.scst.domain.Student;
import com.suda.scst.services.StudentService;

import net.minidev.json.JSONObject;
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
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Xiao
 */

//@RestController是@ResponseBody和@Controller的组合注解
@RestController
//设置想要跳转的父路径
@RequestMapping("/student")
public class StudentController {


    @Autowired
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //service的graph方法，实际做业务处理的方法封装
    @GetMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit", required = false) Integer limit) {
        return studentService.graph(limit == null ? 100 : limit);
    }

    //添加新学生
    @PostMapping(value = "/add")
    //接收post请求
    public void addStudent(@RequestBody Student student) {
        studentService.upsertStudent(student);
        //添加学生到班级的关系
        studentService.addClass(student);
        //添加学生作为班级成员的关系
        studentService.asMember(student);
    }

    //删除学生
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        studentService.deleteByName(name);
        return "delete ok";
    }

    //编辑学生
    @PostMapping(value = "/edit")
    public String editByName(@RequestBody Student student) {
        //不可更改原节点的name的值
        Student origin = studentService.findByName(student.getName());
        //修改原节点的信息
        origin.setClazz(student.getClazz());
        origin.setGender(student.getGender());
        origin.setBirth(student.getBirth());
        origin.setStudent_id(student.getStudent_id());
        //更新节点
        studentService.upsertStudent(origin);
        return "edit ok";
    }

    //查询
    @GetMapping("/query")
    public Collection<Student> graph(@RequestParam(value = "name") String name) {
        return studentService.findByNameLike(name);
    }

    @RequestMapping(path = "/getUserNodeList", method = RequestMethod.GET)
    public List<Student> getUserNodeList() {
        return studentService.getUserNodeList();
    }

    //poi导入
    @RequestMapping("/importList")
    public void importList(@RequestParam(value = "file") MultipartFile mFile) throws IOException {
        assert mFile != null;
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(mFile.getInputStream()));
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            double id = row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String gender = row.getCell(2).getStringCellValue();
            double born = row.getCell(3).getNumericCellValue();
            String clazz = row.getCell(4).getStringCellValue();
            Student user = new Student();
            user.setStudent_id((long) id);
            user.setName(name);
            user.setGender(gender);
            user.setBirth((int)born);
            user.setClazz(clazz);
            //查看数据库中是否有名字相同的专业，有则更新该节点，否则新建节点
            if(studentService.findByName(name)!=null){
                editByName(user);
            }
            else{
                addStudent(user);
            }
//            将对象添加数据库
            System.out.println(user.getName());
        }
    }

    //poi导出
    @RequestMapping("/exportList")
    public void exportList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "测试"+System.currentTimeMillis()+".xls";
        String[] titles = {"学号","姓名","性别","出生日期","所属班级"};
        List<Student> result = (List<Student>) studentService.findByNameLike("**");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("students");
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
            Student aStudent = result.get(i);
            row.createCell(0).setCellValue(aStudent.getStudent_id());
            row.createCell(1).setCellValue(aStudent.getName());
            row.createCell(2).setCellValue(aStudent.getGender());
            row.createCell(3).setCellValue(aStudent.getBirth());
            row.createCell(4).setCellValue(aStudent.getClazz());
            row.setHeight((short) (20 * 30));
        }
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
        workbook.write(os);
        os.flush();
        os.close();
    }

//    public Map<String,Student> getStudent()
//	{
//		Student student1 = new Student("wang",1998,21);
//		Student student2 = new Student("gong", 1999,21);
//		Student student3 = new Student("zhang", 2000,20);
//		Map<String,Student> map = new HashMap<>();
//		map.put("student1",student1);
//		map.put("student2",student2);
//		map.put("student3",student3);
//        return map;
//    }
}
