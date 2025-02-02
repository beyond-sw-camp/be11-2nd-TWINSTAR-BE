package com.TwinStar.TwinStar.admin.controller;

import com.TwinStar.TwinStar.admin.dtos.UserDetailDto;
import com.TwinStar.TwinStar.admin.dtos.UserListDto;
import com.TwinStar.TwinStar.admin.service.AdminService;
import com.TwinStar.TwinStar.common.CommonDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("user_list")
    public ResponseEntity<?> userList() {
        List<UserListDto> userListDtos = adminService.findAll();
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "list is found", userListDtos), HttpStatus.OK);
    }

    //    회원상세조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> userDetail(@PathVariable Long id) {
        UserDetailDto dto = adminService.findById(id);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "memberDetailLest is found", dto), HttpStatus.OK);
    }
}
