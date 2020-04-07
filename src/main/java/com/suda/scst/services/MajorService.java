package com.suda.scst.services;

import com.suda.scst.domain.Major;
import com.suda.scst.repositories.MajorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class MajorService {
    private final static Logger LOG = LoggerFactory.getLogger(MajorService.class);

    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    //查询1
    @Transactional(readOnly = true)
    public Major findByName(String name) {
        Major result = majorRepository.findByName(name);
        return result;
    }

    //查询2
    @Transactional(readOnly = true)
    public Collection<Major> findByNameLike(String name) {
        Collection<Major> result = majorRepository.findByNameLike(name);
        return result;
    }


    //删除
    @Transactional(readOnly = true)
    public void deleteByName(String name) {
        Major result = majorRepository.findByName(name);
        if (result != null) {
            majorRepository.delete(result);
        }
    }

    //更新或新增（1.如果入参的major对象是先从库里搜索出来，在内存中改了部分字段，那么这边save就是更新，
    // 2.如果入参的major对象是直接new出来的，那么就是新增，框架会帮你自动生成id）
    @Transactional(readOnly = true)
    public Major upsertMajor(Major major) {
        return majorRepository.save(major);
    }
}
