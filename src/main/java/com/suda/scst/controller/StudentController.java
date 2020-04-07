package com.suda.scst.controller;

import com.suda.scst.domain.Student;
import com.suda.scst.services.StudentService;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    }

    //删除学生
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        studentService.deleteByName(name);
        return "delete ok";
    }

    //编辑学生
    @PostMapping(value = "/edit")
    public void editByName(@RequestBody Student student) {
        //不可更改原节点的name的值
        Student origin = studentService.findByName(student.getName());
        //修改原节点的信息
        origin.setClazz(student.getClazz());
        origin.setGender(student.getGender());
        origin.setBirth(student.getBirth());
        origin.setStudent_id(student.getStudent_id());
        //更新节点
        studentService.upsertStudent(origin);
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
