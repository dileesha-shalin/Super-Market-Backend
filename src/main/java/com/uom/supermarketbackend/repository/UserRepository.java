package com.uom.supermarketbackend.repository;
import com.uom.supermarketbackend.enums.RoleType;
import com.uom.supermarketbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByEmail(String email);

    @Query(value = """
      select u from User u\s
      where u.email = :email and u.role = :role\s
      """)
    Optional <User> findByEmailAndRole(String email, RoleType role);

    @Query(value = """
      select u from User u\s
      where u.id = :id and u.role = 'DELIVERY_PERSON'\s
      """)
    Optional <User> findDeliveryPersonById(Long id);
}
