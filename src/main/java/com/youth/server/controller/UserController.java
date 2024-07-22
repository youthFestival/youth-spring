//package com.youth.server.controller;
//
//import com.youth.server.domain.User;
//import com.youth.server.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable int id) {
//        return userService.getUserById(id);
//    }
//
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        return userService.save(user);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User userDetails) {
//        return userService.findById(id)
//                .map(user -> {
//                    user.setUserId(userDetails.getUserId());
//                    user.setPassword(userDetails.getPassword());
//                    user.setEmail(userDetails.getEmail());
//                    user.setCreatedAt(userDetails.getCreatedAt());
//                    user.setGender(userDetails.getGender());
//                    user.setUsername(userDetails.getUsername());
//                    user.setTel(userDetails.getTel());
//                    user.setAddress(userDetails.getAddress());
//                    user.setIsAdmin(userDetails.getIsAdmin());
//                    user.setAllowEmail(userDetails.isAllowEmail());
//                    return ResponseEntity.ok(userService.save(user));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
//        return userService.findById(id)
//                .map(user -> {
//                    userService.deleteById(id);
//                    return ResponseEntity.noContent().build();
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//}
