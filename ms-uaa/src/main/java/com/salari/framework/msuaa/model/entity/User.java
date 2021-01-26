package com.salari.framework.msuaa.model.entity;
import com.salari.framework.msuaa.model.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Where(clause = "deleted=false")
public class User extends BaseEntity<Integer> {

    @Column(length = 20,nullable = false,unique = true)
    private String username;

    @Column(length = 100,nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean active=true;

    @Column(nullable = false, name = "person_id")
    private Integer personId;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="person_id",referencedColumnName = "id", insertable = false, updatable = false)
    private Person person;

    @Column(nullable = true)
    private Integer currentRoleId;

    @Column(nullable = true)
    private Long lastLoginTryDate;

    @Column(nullable = false)
    private Short loginFailedTryCount=0;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;
}
