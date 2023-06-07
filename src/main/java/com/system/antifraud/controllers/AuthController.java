package com.system.antifraud.controllers;

import com.system.antifraud.models.ERole;
import com.system.antifraud.models.db.Role;
import com.system.antifraud.models.db.User;
import com.system.antifraud.models.payload.request.LoginRequest;
import com.system.antifraud.models.payload.request.SignupRequest;
import com.system.antifraud.models.payload.response.MessageResponse;
import com.system.antifraud.models.payload.response.UserInfoResponse;
import com.system.antifraud.repository.RoleRepository;
import com.system.antifraud.repository.UserRepository;
import com.system.antifraud.security.jwt.JwtUtils;
import com.system.antifraud.security.services.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @PostMapping("/signin")//войти
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

        System.out.println(loginRequest.getUsername()+" "+loginRequest.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        System.out.println("AuthController 1");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("AuthController 2");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        System.out.println("AuthController 3");

        System.out.println(userDetails.getUsername());

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        System.out.println(jwtCookie);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        System.out.println("AuthController 4");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE) //регистрация
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest){
        System.out.println(signUpRequest.getUsername());
        System.out.println(signUpRequest.getEmail());

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            System.out.println();
            return ResponseEntity.ok().body(new MessageResponse(400, "Имя пользователя уже используется"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.ok().body(new MessageResponse(400, "Email уже используется"));
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        // Create new user's account
        User user = new User(new Date(),signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),true);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Роль не найдена"));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Роль не найдена"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok().body(new UserInfoResponse(user.getId(),
                user.getUsername(),
                user.getEmail(), null));
    }

    @PostMapping("/signout")//выход
    public ResponseEntity<?> logoutUser(){
        System.out.println("logout");
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse(200, "You've been signed out!"));
    }
}
