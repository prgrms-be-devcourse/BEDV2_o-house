package com.prgrms.ohouse.domain.user.model;

import static com.google.common.base.Preconditions.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "following_count")
    private int followingCount = 0;

    @Column(name = "follower_count")
    private int followerCount = 0;

    @Column(name = "default_address")
    private String defaultAddress;

    public void checkPassword(PasswordEncoder passwordEncoder, String password) {
        checkArgument(!passwordEncoder.matches(password, this.password), "Bad credential. Login failed.");
    }

    @Builder
    public User(String nickname, String email, String password, int followingCount, int followerCount, String defaultAddress) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.defaultAddress = defaultAddress;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
