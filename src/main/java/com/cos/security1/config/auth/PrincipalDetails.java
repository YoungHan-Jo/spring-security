package com.cos.security1.config.auth;

// 1. 시큐리티가 /login 주소요청이 오면 낚아채서 로그인을 진행시킨다.
// 2. 로그인 진행이 완료가 되면 시큐리티전용session을 만들어준다. (Security ContextHolder라는 키값에 세션정보를 저장함)
// 3. 시큐리티 세션에 들어갈 오브젝트 -> Authentication 타입 객체만 들어갈 수 있음
// 4. Authentication 안에는 User 정보가 있어야 됨.
// 5. User 오브젝트 -> UserDetails 타입 객체로 정해져 있음

// Security Session 에는 Authentication 객체만 저장 할 수 있음.
// Authentication 객체에는 유저정보가 담겨 있는데 UserDetails 타입으로 담겨 있음

// Security Session => Authentication => UserDetails(PrincipalDetails)

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails { // 상속으로 인해 PrincipalDetails는 UserDetails이 될 수 있다.

    private User user; // 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // example
        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면계정으로
        // 현재시간 - 로그인시간 = 1년을 초과하면 return false;
        return true;
    }
}
