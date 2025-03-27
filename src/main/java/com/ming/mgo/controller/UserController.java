package com.ming.mgo.controller;

import com.ming.mgo.dto.ResponseMessage;
import com.ming.mgo.entity.User;
import com.ming.mgo.security.JwtUtils;
import com.ming.mgo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseMessage<User>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        user.setPassword(null);
        return ResponseEntity.ok(ResponseMessage.success(user));
    }

    @GetMapping("/usernames")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseMessage<List<String>>> getAllUsernames() {
        List<User> users = userService.getAllUsers();
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseMessage.success(usernames));
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage<String>> logout() {
        return ResponseEntity.ok(ResponseMessage.success("Logout successfully!"));
    }

} 