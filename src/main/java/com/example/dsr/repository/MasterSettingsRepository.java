package com.example.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dsr.model.MasterSettings;

@Repository
public interface MasterSettingsRepository extends JpaRepository<MasterSettings, Integer>{

}
