package com.suda.scst.controller;

import com.suda.scst.config.SecurityConfig;
import com.suda.scst.domain.User;
import com.suda.scst.repositories.MajorRepository;
import com.suda.scst.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/")
public class LoginController {

    @Autowired
    private MajorRepository majorRepository;
    private UserService userService;

    public LoginController(SecurityConfig securityConfig, UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/get")
    public int[] getNum() {
        int[] a=new int[4];
        a[0]=majorRepository.getNum1();
        a[1]=majorRepository.getNum2();
        a[2]=majorRepository.getNum3();
        a[3]=majorRepository.getNum4();
        //System.out.println(Arrays.toString(a));
        //return "delete ok";
        return a;
    }

    @GetMapping(value = "/error")
    public void showError() {
        System.out.println("账号权限不足");
    }

    @PostMapping("/register")
    //储存账号信息
    public void addUser(@RequestBody User user) {
        System.out.println(user.getUsername());
        userService.upsertUser(user);
    }

    //从数据库中加载用户账号密码
    @PostMapping("/load")
    public User loadUser(@RequestParam(value = "username") String name){
        User user = userService.findByName(name);
        System.out.println(user.getUsername());
        return user;
    }

//    @Bean
//    public EmbeddedServletContainerCustomizer containerCustomizer() {
//
//        return (container -> {
//            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
//            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
//            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
//
//            container.addErrorPages(error401Page, error404Page, error500Page);
//        });
//    }
    //    @GetMapping("/login/{status}")
//    public String login(@PathVariable String status) {
//        System.out.println(status);
//        if ("auth".equals(status)) {
//            return "没有登录";
//        }
//        if ("fail".equals(status)) {
//            return "登录失败";
//        }
//        if ("success".equals(status)) {
//            return "登录成功";
//        }
//        if ("logout".equals(status)) {
//            return "注销成功";
//        }
//        return "";
//    }
}