package com.TwinStar.TwinStar.user.controller;


import com.TwinStar.TwinStar.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    public final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }





}
