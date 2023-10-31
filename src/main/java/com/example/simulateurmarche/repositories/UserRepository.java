package com.example.simulateurmarche.repositories;


import com.example.simulateurmarche.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    User findByUsername(String username);
    User findByName(String Name);
    List<User> findByAddress(String address);
    List<User> findByStateuser (boolean stateUser);

    @Query("SELECT MIN(e.datenaissance) FROM User e ")
    Date getminage();
    @Query("SELECT MAX(e.datenaissance) FROM User e ")
    Date getmaxage();

    List<User> findAllByOrderByRolesDesc();
    @Query("SELECT e,COUNT(e.stateuser) FROM User e WHERE e.stateuser=false")
    List<User> getusersdisable();
}