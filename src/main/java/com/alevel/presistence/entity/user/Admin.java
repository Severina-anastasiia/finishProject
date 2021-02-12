package com.alevel.presistence.entity.user;

import com.alevel.presistence.type.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User{

    public Admin(){
        super();
        setRoleType(RoleType.ROLE_ADMIN);
    }
}
