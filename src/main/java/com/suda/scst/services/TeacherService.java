package com.suda.scst.services;

import com.suda.scst.domain.Student;
import com.suda.scst.domain.Teacher;
import com.suda.scst.repositories.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class TeacherService {
    private final static Logger LOG = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    //查询1
    @Transactional(readOnly = true)
    public Teacher findByName(String name) {
        Teacher result = teacherRepository.findByName(name);
        return result;
    }

    //查询2
    @Transactional(readOnly = true)
    public Collection<Teacher> findByNameLike(String name) {
        Collection<Teacher> result = teacherRepository.findByNameLike(name);
        return result;
    }

    //删除
    @Transactional(readOnly = true)
    public void deleteByName(String name) {
        Teacher result = teacherRepository.findByName(name);
        if (result != null) {
            teacherRepository.delete(result);
        }
    }

    //更新或新增（1.如果入参的teacher对象是先从库里搜索出来，在内存中改了部分字段，那么这边save就是更新，
    // 2.如果入参的teacher对象是直接new出来的，那么就是新增，框架会帮你自动生成id）
    @Transactional(readOnly = true)
    public Teacher upsertTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
}

