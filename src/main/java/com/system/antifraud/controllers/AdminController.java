package com.system.antifraud.controllers;

import com.google.gson.Gson;
import com.system.antifraud.models.ERole;
import com.system.antifraud.models.db.User;
import com.system.antifraud.models.payload.request.SignupRequest;
import com.system.antifraud.models.payload.response.UserInfoResponse;
import com.system.antifraud.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public UserRepository userRepository;

    @GetMapping("/useradd")
    public String registration(){
        return "useradd";
    }

    @PostMapping("/registration")
    public String useradd(@RequestParam(value = "username") String username,
                          @RequestParam(value = "email") String email,
                          @RequestParam(value = "password") String password,
                          Model model) throws Exception {

        User userdata = userRepository.loginUser(username);
        if(userdata != null){
            model.addAttribute("errorUsername", "Логин используется в системе");
            model.addAttribute("styleUser", "color: red;");
        }

        model.addAttribute("username", username);
        model.addAttribute("email", email);

        if(!model.containsAttribute("errorUsername")){
            RestTemplate restTemplate = new RestTemplate();
            Gson gson = new Gson();

            SignupRequest regData = new SignupRequest();
            regData.setUsername(username);
            regData.setEmail(email);
            regData.setPassword(password);
            Set<String> role = Collections.singleton(String.valueOf(ERole.ROLE_USER));
            regData.setRole(role);

            ResponseEntity<String> signup_result = restTemplate.postForEntity("http://localhost:8081/api/auth/signup", regData, String.class);

            UserInfoResponse result = gson.fromJson(signup_result.getBody(), UserInfoResponse.class);
            log.warn(result.getId()+" signup_result");

            model.addAttribute("msg","Клиент успешно создан");
            model.addAttribute("styleMsg","display:block;");
            return "useradd";

        }
        return "useradd";
    }
}
