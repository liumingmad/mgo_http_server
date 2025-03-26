package com.ming.mgo.controller;

import com.ming.mgo.annotation.LogPrint;
import com.ming.mgo.dto.AuthResponse;
import com.ming.mgo.dto.LoginRequest;
import com.ming.mgo.dto.RegisterRequest;
import com.ming.mgo.dto.ResponseMessage;
import com.ming.mgo.entity.User;
import com.ming.mgo.security.JwtUtils;
import com.ming.mgo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping({"/api/auth", ""})  // 支持 /api/auth 和根路径
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage<String>> logout() {
        return ResponseEntity.ok(ResponseMessage.success("Logout successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<AuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(ResponseMessage.success(new AuthResponse(jwt)));
    }

    @LogPrint
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<User>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

        User registeredUser = userService.registerUser(user);
        if (registeredUser == null) {
            return ResponseEntity.ok(ResponseMessage.error(400, "User registered failed!"));
        }

        return ResponseEntity.ok(ResponseMessage.success("User registered successfully!"));
    }
} 