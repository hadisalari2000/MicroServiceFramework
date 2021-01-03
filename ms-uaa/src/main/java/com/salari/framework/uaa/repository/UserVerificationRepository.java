package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification,Integer> {

    @Transactional
    void deleteById(Long id);
    Optional<UserVerification> findByKeyAndCodeAndExpirationDateGreaterThan(UUID key, String code, Long expirationDate);
    Optional<UserVerification> findByNationalCode(String nationalCode);
}
