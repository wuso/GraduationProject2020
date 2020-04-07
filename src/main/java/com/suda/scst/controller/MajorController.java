package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Major;
import com.suda.scst.domain.Student;
import com.suda.scst.services.MajorService;
import com.suda.scst.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
//设置想要跳转的父路径
@RequestMapping("/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    //添加新专业
    @PostMapping(value = "/add")
    //接收post请求
    public void addMajor(@RequestBody Major major) {
        majorService.upsertMajor(major);
    }

    //删除专业
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        majorService.deleteByName(name);
        return "delete ok";
    }

    //编辑班级
    @PostMapping(value = "/edit")
    public String editByName(@RequestBody Major origin) {
        //不可更改原节点的name的值
        Major major = majorService.findByName(origin.getName());
        //修改原节点的信息
        major.setFaculty(origin.getFaculty());
        major.setResearch(origin.getResearch());
        major.setMajor_id(origin.getMajor_id());
        //更新节点
        majorService.upsertMajor(major);
        return "edit ok";
    }

    //查询专业
    @GetMapping("/query")
    public Collection<Major> query(@RequestParam(value = "name") String name) {
        return majorService.findByNameLike(name);
    }

}
