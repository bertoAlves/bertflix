package com.bertflix.authms.repositories;

import com.bertflix.authms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.active = TRUE WHERE a.email = ?1")
    int enableUser(String email);
}
