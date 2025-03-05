package com.example.admin.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class User {

    @Id
    private String email;

    private String password;
    private String name;

    // 사용자 권한
    @Enumerated(EnumType.STRING)
    private Role role;

    private Date createdAt;
    private Date lastLoginAt;

    // 사용자 상태
    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Role {
        ADMIN, USER
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}