package com.salari.framework.uaa.model.initdata;

import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.enums.RoleTypes;
import com.salari.framework.uaa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class DatabaseInitializer implements ApplicationRunner {

    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Populating database with sample data...");
        setRoleInitialData();
    }

    private void setRoleInitialData() {
       Optional<Role> roleExist= roleRepository.findByKey("public");
       if(!roleExist.isPresent()){
           Role role=Role.builder()
                   .active(true)
                   .title("")
                   .creationDate(System.currentTimeMillis())
                   .deleted(false)
                   .description("کاربر عمومی سامانه")
                   .key("public")
                   .roleType(RoleTypes.USER)
                   .build();
           roleRepository.save(role);
       }
    }
}
