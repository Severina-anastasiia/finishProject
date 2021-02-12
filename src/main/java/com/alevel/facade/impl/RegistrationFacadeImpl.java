package com.alevel.facade.impl;

import com.alevel.facade.RegistrationFacade;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.service.user.PersonalService;
import com.alevel.web.data.request.AuthData;
import org.springframework.stereotype.Service;

@Service
public class RegistrationFacadeImpl implements RegistrationFacade {

    private final PersonalService personalService;

    public RegistrationFacadeImpl(PersonalService personalService) {
        this.personalService = personalService;
    }

    @Override
    public void registration(AuthData authData) {
        Personal personal = new Personal();
        personal.setEmail(authData.getEmail());
        personal.setPassword(authData.getPassword());
        personalService.create(personal);
    }
}
