package com.alevel.facade;

import com.alevel.web.data.request.PersonalData;
import com.alevel.web.data.response.PageData;
import org.springframework.web.context.request.WebRequest;

public interface PersonalAdminFacade {

    PageData<PersonalData> findAll(WebRequest request);
    PersonalData findById(Integer id);
    void delete(Integer id);
    void lockAccount(Integer id);
    void unlockAccount(Integer id);
}
