package com.example.backend.repos;

import com.example.backend.entity.TestModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestModel, Integer>{
    
}