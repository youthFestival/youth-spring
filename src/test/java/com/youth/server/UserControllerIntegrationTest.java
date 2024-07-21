//package com.youth.server;
//
//import com.youth.server.domain.User;
//import com.youth.server.domain.User.Gender;
//import com.youth.server.domain.User.Role;
//import com.youth.server.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.time.LocalDateTime;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class UserControllerIntegrationTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(UserControllerIntegrationTest.class);
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setUp() {
//        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
//        User user = new User();
//        user.setUserId("testUser");
//        user.setPassword("password123");
//        user.setEmail(uniqueEmail); // Use the unique email
//        user.setGender(Gender.남성);
//        user.setUsername("Test User");
//        user.setTel("010-1234-5678");
//        user.setAddress("123 Test St");
//        user.setIsAdmin(Role.USER);
//        user.setAllowEmail(true);
//
//        userRepository.save(user);
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void 모든_사용자_조회() throws Exception {
//        logger.info("모든_사용자_조회 테스트 시작");
//
//        mockMvc.perform(get("/api/users")
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userId").value("testUser"));
//
//        logger.info("모든_사용자_조회 테스트 종료");
//    }
//}
