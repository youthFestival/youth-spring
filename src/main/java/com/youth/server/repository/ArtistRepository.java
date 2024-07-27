package com.youth.server.repository;

import com.youth.server.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    List<Artist> findAllByNameContains(String name);

    @Query("select u from User u join u.favoriteArtists a  where u.id = :uid ")
    List<Artist> findFavoriteArtistsByUid(Integer uid);


}
