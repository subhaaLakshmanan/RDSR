package com.example.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dsr.model.holiday;

@Repository
public interface HolidayRepository extends JpaRepository<holiday,Long> {

}
