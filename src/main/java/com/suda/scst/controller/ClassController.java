package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Major;
import com.suda.scst.domain.Student;
import com.suda.scst.services.ClassService;
import com.suda.scst.services.MajorService;
import com.suda.scst.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
//设置想要跳转的父路径
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    //添加新班级
    @PostMapping(value = "/add")
    //接收post请求
    public void addMajor(@RequestBody Class clazz) {
        classService.addMajor(clazz);
        classService.upsertClass(clazz);
    }

    //删除班级
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        classService.deleteByName(name);
        return "delete ok";
    }

    //编辑班级
    @PostMapping(value = "/edit")
    public String editByName(@RequestBody Class origin) {
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


}
