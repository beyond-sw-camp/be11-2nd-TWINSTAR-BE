package com.TwinStar.TwinStar.admin.service;

import com.TwinStar.TwinStar.admin.dtos.UserListDto;
import com.TwinStar.TwinStar.admin.repostiory.AdminRepository;
import com.TwinStar.TwinStar.commonDomain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<UserListDto> findAll(){
        return adminRepository.findAll().stream().map(user -> user.listFromEntity()).collect(Collectors.toList());
    }
}
