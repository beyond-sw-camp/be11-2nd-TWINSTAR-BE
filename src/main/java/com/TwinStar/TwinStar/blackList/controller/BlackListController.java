package com.TwinStar.TwinStar.blackList.controller;

import com.TwinStar.TwinStar.blackList.domain.BlackList;
import com.TwinStar.TwinStar.blackList.dto.BlackListSearchDto;
import com.TwinStar.TwinStar.blackList.dto.BlockRequest;
import com.TwinStar.TwinStar.blackList.service.BlackListService;
import com.TwinStar.TwinStar.common.dto.CommonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blacklist")
public class BlackListController {

    @Autowired
    private BlackListService blackListService;

//   1. 차단
    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody BlockRequest request) {
        BlackList block = blackListService.blockUser(request.getUserId(), request.getBlockedUserId());
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "차단 성공",block),HttpStatus.OK);
    }

//    2.차단 해제
    @DeleteMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestBody BlockRequest request) {
        blackListService.unblockUser(request.getUserId(), request.getBlockedUserId());
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "차단해제 성공",null),HttpStatus.OK);
    }

//   3. 차단된 목록 조회
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getBlockedUsers(@PathVariable Long userId
            , @PageableDefault(size = 12,sort = "id",direction = Sort.Direction.ASC) Pageable pageable, BlackListSearchDto dto) {
        Page<BlackList> blackLists = blackListService.getBlockedUsers(userId,pageable,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "차단목록 조회 성공", blackLists),HttpStatus.OK);
    }
}
