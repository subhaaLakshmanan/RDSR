package com.example.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dsr.model.OndutyDetails;

@Repository
public interface OndutyDetailsRepository extends JpaRepository<OndutyDetails,Long> {

}
