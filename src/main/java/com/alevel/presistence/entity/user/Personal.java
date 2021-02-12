package com.alevel.presistence.entity.user;

import com.alevel.presistence.type.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("PERSONAL")
public class Personal extends User {

    @Column(name = "NICK_NAME")
    private String nickName;

    public Personal(){
        super();
        setRoleType(RoleType.ROLE_PERSONAL);
    }
}
