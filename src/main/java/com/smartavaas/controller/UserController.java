package com.smartavaas.controller;

import com.smartavaas.model.User;
import com.smartavaas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser (@RequestBody User user) {
        System.out.println("MOBILE: " + user.getMobile());
        System.out.println("EMAIL: " + user.getEmail());
        System.out.println("PASSWORD: " + user.getPassword());
        User registeredUser  = userService.registerUser (user);
        return ResponseEntity.ok(registeredUser );
    }

    // Role-Guarded Endpoint
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String mobile, @RequestParam String password) {
//        boolean success = userService.authenticateUser(mobile, password);
//        if (success) {
//            return ResponseEntity.ok("Login successful.");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
//        }
//    }
}

















//
//
//import com.example.residentcommunityapp.model.User;
//import com.example.residentcommunityapp.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<List<User>> getUsers() {
//        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
//    }
//
//    @GetMapping("/{email}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
//        try {
//
//            User theUser = userService.getUser(email);
//            return ResponseEntity.ok(theUser);
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    "Error fetching user by email: ");
//        }
//
//    }
//
//    @DeleteMapping("/delete/{email}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
//    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
//        try {
//            userService.deleteUser(email);
//            return ResponseEntity.ok("User deleted successfully");
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" +
//                    "error deleting user");
//        }
//    }
//
//}
//
