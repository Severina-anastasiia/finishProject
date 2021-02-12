package com.alevel.presistence.entity.user;

import com.alevel.presistence.entity.AbstractEntity;
import com.alevel.presistence.type.RoleType;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_TYPE", nullable = false)
    private RoleType roleType;

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

    public User(){
        super();
        this.enabled = true;
    }
}
