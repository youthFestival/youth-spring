package com.youth.server.repository;

import com.youth.server.domain.Inquiry;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry,Integer> {

    @Query("SELECT i FROM Inquiry i WHERE i.category = '질문'")
    List<Inquiry> findAllQuestion(PageRequest pageRequest);
    @Query("SELECT i FROM Inquiry i WHERE i.author.id = :authorId AND i.category= '질문'")
    List<Inquiry> findAllQuestionByUserId(int authorId,PageRequest pageRequest);

    @Query("SELECT i from  Inquiry i WHERE i.festivalId = :festivalId AND i.category = '질문'")
    List<Inquiry> findAllQuestionByFestivalId(Integer festivalId,PageRequest pageRequest);

    @Query("SELECT i  FROM Inquiry i WHERE i.id = :inquiryId")
    Optional<Inquiry> findDetailById(int inquiryId);
}
