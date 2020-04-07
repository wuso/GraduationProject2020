package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Teacher;
import com.suda.scst.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
    }

    //删除教师
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        teacherService.deleteByName(name);
        return "delete ok";
    }

    //编辑教师
    @PostMapping(value = "/edit")
    public void editByName(@RequestBody Teacher origin) {
        //不可更改原节点的name的值
        Teacher teacher = teacherService.findByName(origin.getName());
        //修改原节点的信息
        teacher.setCourse(origin.getCourse());
        teacher.setGender(origin.getGender());
        teacher.setTitle(origin.getTitle());
        teacher.setTeacher_id(origin.getTeacher_id());
        //更新节点
        teacherService.upsertTeacher(teacher);
    }

    //查询教师
    @GetMapping("/query")
    public Collection<Teacher> query(@RequestParam(value = "name") String name) {
        return teacherService.findByNameLike(name);
    }

}
