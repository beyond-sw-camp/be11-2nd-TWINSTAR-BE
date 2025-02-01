package com.TwinStar.TwinStar.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCreateReq {

    public Post toEntity(USER user, LocalDateTime appointmentTime){
        return Post.builder()
                .title(this.title)
                .user(this.user)
                .contents(this.contents)
                .appointment(this.appointment)
                .appointmentTime(this.appointmentTime)
                .build();
    }
    
}
