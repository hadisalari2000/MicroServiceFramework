package com.salari.framework.msuaa.repository;

import com.salari.framework.msuaa.model.domain.user.UserFilterRequest;
import com.salari.framework.msuaa.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findById(Integer id);
    Optional<User> findByUsernameIgnoreCase(String username);
    Boolean existsByPersonId(Integer personId);
    Optional<User> findByPersonId(Integer id);

    default Page<User> findUsersByFilters(UserFilterRequest request, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!request.getUsername().isEmpty())
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), request.getUsername().toLowerCase()));

            if(!request.getFirstName().isEmpty()){
                Join join=root.join("person");
                predicates.add(criteriaBuilder.like(join.get("firstName"),request.getFirstName()));
            }

            if(!request.getLastName().isEmpty()){
                Join join=root.join("person");
                predicates.add(criteriaBuilder.like(join.get("lastName"),request.getLastName()));
            }

            if (!request.getNationalCode().isEmpty()) {
                Join join=root.join("person");
                predicates.add(criteriaBuilder.like(join.get("nationalCode"),request.getNationalCode()));
            }

            if(!request.getMobileNumber().isEmpty()){
                Join join=root.join("person");
                predicates.add(criteriaBuilder.like(join.get("mobileNumber"),request.getMobileNumber()));
            }

            if(!request.getMobileNumber().isEmpty()){
                Join join=root.join("person");
                predicates.add(criteriaBuilder.like(join.get("email"),request.getEmail()));
            }

            if(request.getGender()!=null){
                Join join=root.join("person");
                predicates.add(criteriaBuilder.equal(join.get("gender"),request.getGender()));
            }

            if(request.getRoleId() != null){
                Join join=root.join("user_role");
                predicates.add(criteriaBuilder.equal(join.get("roleId"),request.getRoleId()));
            }

            Join join=root.join("person");
            predicates.add(criteriaBuilder.equal(join.get("deleted"),false));
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
}
