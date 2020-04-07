package com.suda.scst.services;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Major;
import com.suda.scst.repositories.ClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClassService {

    private final static Logger LOG = LoggerFactory.getLogger(ClassService.class);

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    //查询1
    @Transactional(readOnly = true)
    public Class findByMajor(String major) {
        Class result = classRepository.findByMajor(major);
        return result;
    }

    //查询1
    @Transactional(readOnly = true)
    public Class findByName(String name) {
        Class result = classRepository.findByName(name);
        return result;
    }

    //查询2
    @Transactional(readOnly = true)
    public Collection<Class> findByMajorLike(String major) {
        Collection<Class> result = classRepository.findByMajorLike(major);
        return result;
    }

    //添加到专业的关系
    @Transactional(readOnly = true)
    public void addMajor(Class clazz) {
        int class_id = clazz.getClass_id();
        String major = clazz.getMajor();
        classRepository.belongTo(class_id,major);
    }

    //删除
    @Transactional(readOnly = true)
    public void deleteByName(String name) {
        Class result = classRepository.findByName(name);
        if (result != null) {
            classRepository.delete(result);
        }
    }

    //更新或新增（1.如果入参的class对象是先从库里搜索出来，在内存中改了部分字段，那么这边save就是更新，
    // 2.如果入参的class对象是直接new出来的，那么就是新增，框架会帮你自动生成id）
    @Transactional(readOnly = true)
    public Class upsertClass(Class clazz) {
        return classRepository.save(clazz);
    }
}
