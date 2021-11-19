package com.example.backend.repos;

import java.util.Optional;

import com.example.backend.entity.AccountModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, String>{
    Optional<AccountModel> findByEmail(String email);
}
