package com.youth.server.repository;

import com.youth.server.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    Optional<Festival> findFestivalById(int festivalId);


    List<Festival> findTop5ByNameIsContaining(String name);

//    @Query("""
//            SELECT f
//            FROM Festival f
//            WHERE f.locality = :data.locality
//            LIMIT = :data.limit OFFSET = :data.offset
//            """)
//    List<Festival> findFestival(FestivalRequest data);

    // 페스티벌 id에 해당하는 FestivalArtist 관계테이블에서 Artist 가져오기

    Optional<Festival> findById(int id);
}
