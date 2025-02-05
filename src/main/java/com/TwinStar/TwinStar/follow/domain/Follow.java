package com.TwinStar.TwinStar.follow.domain;

import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follow")
public class Follow {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // 팔로우를 한사람 (나)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // 팔로우를 당한사람 (카리나)

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }


}
