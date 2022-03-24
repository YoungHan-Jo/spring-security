package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 설정파일 등록
@EnableWebSecurity // 시프링 시큐리티 필터가 스프링 필터체인에 등록됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //비활성화
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증이 필요함
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // admin이거나 manager 만 접근가능
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // admin만 접근가능
                .anyRequest().permitAll() // 나머지 요청은 모두에게 허용
                .and()
                .formLogin()
                .loginPage("/loginForm"); // 걸리면 /login 으로 보내기
    }
}
