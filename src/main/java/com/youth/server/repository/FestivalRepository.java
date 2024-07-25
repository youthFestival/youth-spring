package com.youth.server.repository;

import com.youth.server.domain.Festival;
import com.youth.server.dto.SearchFestivalByFilterDTO;
import com.youth.server.dto.festival.FestivalRequest;
import org.springframework.data.domain.PageRequest;
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


    @Query("SELECT f FROM Festival f JOIN f.favoriteUsers u WHERE u.id = :uid")
    List<Festival> findAllByFavoriteUserId(@Param("uid") int uid);

    Optional<Festival> findFestivalById(int festivalId);


    List<Festival> findTop5ByNameIsContaining(String name);


    Optional<Festival> findById(int id);

    @Query("""
        SELECT new com.youth.server.dto.SearchFestivalByFilterDTO(
           f.id, f.name, f.startDate, f.endDate, f.locality, MIN(i.imgUrl) ,f.viewCount,f.ticketOpen , COUNT(u.id)
        )
        From Festival f join f.images i
        left join f.favoriteUsers u
        where
        (:#{#data.search} IS NULL OR f.name LIKE %:#{#data.search}%) AND
        (:#{#data.locality} IS NULL OR f.locality = :#{#data.locality}) AND
        (:#{#data.categories} IS NULL OR f.category = :#{#data.getCategory()}) AND
        (:#{#data.isOngoing} IS NULL OR (
            f.startDate <= CURRENT_DATE() AND f.endDate >= CURRENT_DATE()
        ))
        GROUP BY f.id, f.name, f.startDate, f.endDate, f.locality, f.viewCount, f.ticketOpen
        ORDER BY f.id
   

""")
    List<SearchFestivalByFilterDTO> findFestivalByDTO(@Param("data")FestivalRequest data, PageRequest of);


    // 3개 랜덤 탐색 인기순
    @Query("SELECT f FROM Festival f ORDER BY f.viewCount DESC")
    List<Festival> findTop3(PageRequest of);
}
