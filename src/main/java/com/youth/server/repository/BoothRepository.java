package com.youth.server.repository;

import com.youth.server.domain.Booth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoothRepository extends JpaRepository<Booth,Integer> {
}
