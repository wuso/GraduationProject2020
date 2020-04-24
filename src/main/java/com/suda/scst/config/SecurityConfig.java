package com.suda.scst.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/index.html","/register.html","/register","/load").permitAll()//访问index.html不需要权限验证
                    .antMatchers("/new/newteacher.html","/new/newclass.html","/new/newmajor.html","/edit/editteacher.html","/edit/editclass.html","/edit/editmajor.html","/show/showteacher.html","/show/showclass.html","/show/showmajor.html").hasRole("ADMIN")  // user接口只有ADMIN角色的可以访问
                    .antMatchers("/new/newstudent.html","/edit/editstudent.html","/show/showstudent.html").hasAnyRole("ADMIN","TEACHER")  // user接口只有ADMIN角色的可以访问
                    //.antMatchers("/").hasAnyRole("ADMIN","TEACHER","STUDENT")
                    .anyRequest().authenticated()   //其他地址的访问均需要验证权限
                .and()
                .formLogin() //内部注册 UsernamePasswordAuthenticationFilter
                    .loginPage("/login.html")
                    .loginProcessingUrl("/login")//form表单POST请求url提交地址，默认为/login
                    .passwordParameter("password")//form表单用户名参数名
                    .usernameParameter("username") //form表单密码参数名
                    .defaultSuccessUrl("/")
                    //.successForwardUrl("/success.html")  //登录成功跳转地址
                    .failureForwardUrl("/error.html") //登录失败跳转地址
                    //.defaultSuccessUrl()//如果用户没有访问受保护的页面，默认跳转到页面
                    //.failureUrl()
//                    .failureHandler(AuthenticationFailureHandler)
//                    .successHandler(AuthenticationSuccessHandler)
                    //.failureUrl("/login?error")
                    .permitAll()
                    .and()
                .logout()//配置注销
                    .logoutUrl("/logout")//注销接口
                    .logoutSuccessUrl("/login.html").permitAll()//注销成功跳转接口
                    .deleteCookies("myCookie") //删除自定义的cookie
                    .and()
                .csrf().disable();//默认开启，这里先显式关闭。禁用csrf，否则post方法无法访问
                //.logout().logoutUrl("/index.html").logoutSuccessUrl("/login.html").invalidateHttpSession(true).deleteCookies("JSESSIONID")
    }
    /**
     * 认证信息管理
     * spring5中摒弃了原有的密码存储格式，官方把spring security的密码存储格式改了
     *
     * @param auth
     * @throws Exception
     */

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //User user= loginController.

        auth.inMemoryAuthentication() //认证信息存储到内存中
                .passwordEncoder(passwordEncoder())
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN")
                .and()
                .passwordEncoder(passwordEncoder())
                .withUser("teacher").password(passwordEncoder().encode("123456")).roles("TEACHER")
        //auth.inMemoryAuthentication() //认证信息存储到内存中
                .and()
                .passwordEncoder(passwordEncoder())
                .withUser("student").password(passwordEncoder().encode("123456")).roles("STUDENT");
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/dist/**");
        //可以仿照上面一句忽略静态资源
    }

//    @Component("myAuthenctiationSuccessHandler")
//    public class MyAuthenctiationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//        private Logger logger = (Logger) LoggerFactory.getLogger(getClass());
//        @Autowired
//        private ObjectMapper objectMapper;
//
//        @Override
//        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//                throws IOException, ServletException {
//            logger.info("登录成功");
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write( objectMapper.writeValueAsString(authentication));
//        }
//    }

//    // 密码加密方式
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

}

