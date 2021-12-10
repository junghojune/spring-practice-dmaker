package com.dmaker.repository;

import com.dmaker.entity.Developer;
import com.dmaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetiredDeveloperRepository extends JpaRepository<RetiredDeveloper, Long> {

}
