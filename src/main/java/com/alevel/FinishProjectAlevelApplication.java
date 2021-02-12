package com.alevel;

import com.alevel.presistence.entity.user.Admin;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.presistence.repository.user.AdminRepository;
import com.alevel.presistence.repository.user.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@SpringBootApplication
public class FinishProjectAlevelApplication {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${initPersonals}")
    private boolean initPersonals;

    public static void main(String[] args) {
        SpringApplication.run(FinishProjectAlevelApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        String email = "admin@gmail.com";
        String password = "12345678";
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            admin = new Admin();
            admin.setEmail(email);
            admin.setPassword(bCryptPasswordEncoder.encode(password));
            adminRepository.save(admin);
        }
        if (initPersonals) {
            System.out.println("start init");
            for (int i = 0; i < 111; i++) {
                Personal personal = new Personal();
                personal.setPassword(bCryptPasswordEncoder.encode(password));
                personal.setEmail("personal" + i + "@gmail.com");
                personalRepository.save(personal);
            }
            System.out.println("finish init");
        }
    }
}
