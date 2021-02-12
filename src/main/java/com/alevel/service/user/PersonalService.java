package com.alevel.service.user;

import com.alevel.presistence.entity.user.Personal;

import java.util.List;

public interface PersonalService extends UserService<Personal> {

    Personal findByEmail(String email);
    List<Personal> findAllByListId(List<Integer> ids);
}
