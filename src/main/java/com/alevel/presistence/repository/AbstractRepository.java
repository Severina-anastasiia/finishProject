package com.alevel.presistence.repository;

import com.alevel.presistence.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;

@NoRepositoryBean
public interface AbstractRepository<E extends AbstractEntity> extends JpaRepository<E, Integer>, JpaSpecificationExecutor<E> {

    @Query("select min(ae.created) from #{#entityName} ae")
    Date findMinCreated();

    @Query("select max(ae.created) from #{#entityName} ae")
    Date findMaxCreated();

    Long countAllByCreatedBetween(Date start, Date end);
}
