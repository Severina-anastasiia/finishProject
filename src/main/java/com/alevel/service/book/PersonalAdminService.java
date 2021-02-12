package com.alevel.service.book;

import com.alevel.presistence.entity.user.Personal;
import com.alevel.service.CrudService;

public interface PersonalAdminService extends CrudService<Personal> {

    void lockAccount(Integer id);
    void unlockAccount(Integer id);
}
