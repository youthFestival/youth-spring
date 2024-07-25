package com.youth.server.repository;

import com.youth.server.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {

    @Query("SELECT e FROM Event e WHERE e.userId = :userId")
    List<Event> findAllByUserId(int userId);
}
