package com.alevel.presistence.repository.user;

import com.alevel.presistence.entity.user.User;
import com.alevel.presistence.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<U extends User> extends AbstractRepository<U> {

    U findByEmail(String email);
    boolean existsByEmail(String email);
}
