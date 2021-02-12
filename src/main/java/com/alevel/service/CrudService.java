package com.alevel.service;

import com.alevel.presistence.data.PersistenceRequestData;
import com.alevel.presistence.entity.AbstractEntity;
import org.springframework.data.domain.Page;

public interface CrudService<AE extends AbstractEntity> {

    Page<AE> findAll(PersistenceRequestData persistenceRequestData);
    AE findById(Integer id);
    void create(AE ae);
    void update(AE ae);
    void delete(Integer id);
}
