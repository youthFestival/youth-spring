package com.youth.server.repository;

import com.youth.server.domain.Artist;
import com.youth.server.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @Transactional
    void showAllUsers(){
        System.out.println(userRepository.findAll());
    }

    @Test
    @Transactional
    void testUserFavoriteArtists() {
        // Given
        User user = User.builder()
                .userId("testUser")
                .password("password123")
                .email("test@example.com")
                .gender(User.Gender.남성)
                .username("Test User")
                .isAdmin(User.Role.user)
                .isAllowEmail(true)
                .build();

        Artist artist1 = new Artist("artist1");
        Artist artist2 = new Artist("artist2");


        user.getFavoriteArtists().add(artist1);
        user.getFavoriteArtists().add(artist2);

        artist1.getSubscribedUsers().add(user);
        artist2.getSubscribedUsers().add(user);

        userRepository.save(user);
        artistRepository.save(artist1);
        artistRepository.save(artist2);

        // When
        Optional<User> foundUser = userRepository.findById(user.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(2, foundUser.get().getFavoriteArtists().size());
        assertTrue(foundUser.get().getFavoriteArtists().stream().anyMatch(a -> a.getName().equals("artist1")));
        assertTrue(foundUser.get().getFavoriteArtists().stream().anyMatch(a -> a.getName().equals("artist2")));
    }
}