package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findById(Integer id);
    Optional<User> findByUsernameIgnoreCase(String username);
    Boolean existsByPersonId(Integer personId);
    Optional<User> findByUsernameAndPassword(String username,String password);
    Optional<User> findUserByPerson_Id(Integer id);
    List<User> findAllByActive(Boolean active);
    List<User> findAllByRoles(Role role);
}
