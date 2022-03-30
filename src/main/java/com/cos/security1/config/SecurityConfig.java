package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 설정파일 등록
@EnableWebSecurity // 시프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화 , pre(post)Authorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

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
                .loginPage("/loginForm")// 위 조건에 걸리면 /login 으로 보내기
//                .usernameParameter("username2")// 입력 name 바꾸고 싶으면
                .loginProcessingUrl("/login")//  /login 주소가 호출이 되면, 시큐리티가 낚아채서 대신 로그인을 진행해준다. -> 컨트롤러에 /login 없어도 됨
                .defaultSuccessUrl("/") // 로그인 성공하면 어디로 갈지 설정
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인이 완료된 뒤, 후처리가 필요함.
                .userInfoEndpoint()
                .userService(principalOauth2UserService) // oauth2로 로그인 하면 이 클래스를 장착함
        ;
    }
}
