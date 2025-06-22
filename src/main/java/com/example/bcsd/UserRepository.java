package com.example.bcsd;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    public Boolean existsByEmail(String email);
}
