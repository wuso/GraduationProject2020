package com.suda.scst.services;

import com.suda.scst.domain.User;
import com.suda.scst.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class UserService {
    private final static Logger LOG = LoggerFactory.getLogger(com.suda.scst.services.UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //查询1
    @Transactional(readOnly = true)
    public User findByName(String name) {
        User result = userRepository.findByUsername(name);
        return result;
    }

    //查询2
    @Transactional(readOnly = true)
    public Collection<User> findByNameLike(String name) {
        Collection<User> result = userRepository.findByUsernameLike(name);
        return result;
    }


    //删除
    @Transactional(readOnly = true)
    public void deleteByName(String name) {
        User result = userRepository.findByUsername(name);
        if (result != null) {
            userRepository.delete(result);
        }
    }

    //更新或新增（1.如果入参的user对象是先从库里搜索出来，在内存中改了部分字段，那么这边save就是更新，
    // 2.如果入参的user对象是直接new出来的，那么就是新增，框架会帮你自动生成id）
    @Transactional(readOnly = true)
    public User upsertUser(User user) {
        System.out.println("1");
        return userRepository.save(user);
    }
}
