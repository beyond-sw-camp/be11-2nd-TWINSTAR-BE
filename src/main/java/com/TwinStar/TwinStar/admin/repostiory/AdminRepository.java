package com.TwinStar.TwinStar.admin.repostiory;

import com.TwinStar.TwinStar.commonDomain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<User,Long> {

}
