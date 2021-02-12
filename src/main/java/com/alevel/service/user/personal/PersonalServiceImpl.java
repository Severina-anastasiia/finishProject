package com.alevel.service.user.personal;

import com.alevel.presistence.data.PersistenceRequestData;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.presistence.repository.user.PersonalRepository;
import com.alevel.service.user.PersonalService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonalServiceImpl implements PersonalService {

    private final PersonalRepository personalRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PersonalServiceImpl(PersonalRepository personalRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personalRepository = personalRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Page<Personal> findAll(PersistenceRequestData persistenceRequestData) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Personal findById(Integer id) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void create(Personal personal) {
        if(personalRepository.existsByEmail(personal.getEmail())){
            throw new RuntimeException("user exist");
        }
        personal.setPassword(bCryptPasswordEncoder.encode(personal.getPassword()));
        personalRepository.save(personal);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(Personal personal) {
        Personal current = getPersonal(personal.getId());
        current.setNickName(personal.getNickName());
        personalRepository.save(current);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Integer id) {
        Personal personal = getPersonal(id);
        personalRepository.delete(personal);
    }

    @Override
    @Transactional(readOnly = true)
    public Personal findByEmail(String email) {
        return personalRepository.findByEmail(email);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public List<Personal> findAllByListId(List<Integer> ids) {
        return personalRepository.findAllById(ids);
    }

    private Personal getPersonal(Integer id){
        Personal current = personalRepository.findById(id).orElse(null);
        if(current == null){
            throw new RuntimeException("user not exist");
        }
        return current;
    }
}
