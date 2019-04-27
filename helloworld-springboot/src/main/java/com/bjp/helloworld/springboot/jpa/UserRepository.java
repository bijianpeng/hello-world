package com.bjp.helloworld.springboot.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUserName(String userName);

    @Transactional
    @Modifying
    @Query("update User u set u.userName=?1 where u.id=?2")
    int modifyUserNameById(String userName, Long id);

    @Query("select new com.bjp.helloworld.springboot.jpa.UserAvgScore(u.userName, avg(s.score) as avgScore) from User u left join Score s on u.id=s.userId where u.id=?1")
    UserAvgScore queryUserAvgScore(Long id);

    @Query("select u.userName as userName, max(s.score) as maxScore from User u left join Score s on u.id=s.userId where u.id=?1")
    UserMaxScore queryUserMaxScore(Long id);
}
