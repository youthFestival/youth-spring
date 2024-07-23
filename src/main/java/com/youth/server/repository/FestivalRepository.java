package com.youth.server.repository;

import com.youth.server.domain.Festival;
import com.youth.server.dto.festival.FestivalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Integer> {

    @Query("""
             SELECT f from Festival f WHERE
                (YEAR(f.startDate) = :year AND MONTH(f.startDate) = :month) OR
                (YEAR(f.endDate) = :year AND MONTH(f.endDate) = :month)
            """)
    List<Festival> findAllByYearAndMonth(@Param("year")int year, @Param("month")int month);


    @Query("SELECT f FROM Festival f JOIN f.favoriteUsers u WHERE u.id = :userId")
    List<Festival> findAllByFavoriteUserId(@Param("userId") int userId);

    @Query("SELECT f FROM Festival f WHERE " +
            "f.name LIMIT = :data.getLimit() AND f.startDate = :data.getStartDate() AND f.endDate = :data.getEndDate()")
    List<Festival> findFestival(FestivalRequest data);
}
