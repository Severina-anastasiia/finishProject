package com.alevel.web.data.request;

import com.alevel.presistence.entity.user.Personal;
import com.alevel.web.data.AbstractEntityData;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PersonalData extends AbstractEntityData {

    private Date created;
    private Date updated;
    private String email;
    private Boolean enabled;
    private String nickName;

    public PersonalData(Personal personal){
        setId(personal.getId());
        this.created = personal.getCreated();
        this.updated = personal.getUpdated();
        this.email = personal.getEmail();
        this.enabled = personal.getEnabled();
        this.nickName = personal.getNickName();
    }

    public PersonalData(){ }
}
