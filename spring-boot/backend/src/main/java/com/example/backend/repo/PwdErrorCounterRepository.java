package com.example.backend.repo;

import com.example.backend.entity.PwdErrorCounterModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PwdErrorCounterRepository extends JpaRepository<PwdErrorCounterModel, String> {}
