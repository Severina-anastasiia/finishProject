package com.alevel.service.user;

import com.alevel.presistence.entity.user.User;
import com.alevel.service.CrudService;

public interface UserService <U extends User> extends CrudService<U> {
}
