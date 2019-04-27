package com.bjp.helloworld.springboot;

import com.bjp.helloworld.springboot.jpa.User;
import com.bjp.helloworld.springboot.jpa.UserAvgScore;
import com.bjp.helloworld.springboot.jpa.UserMaxScore;
import com.bjp.helloworld.springboot.jpa.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testUserRepository() {
        User user = new User("张三", 30);

        List<User> usersByUserName = userRepository.findByUserName(user.getUserName());
        if (usersByUserName.size() > 0) {
            usersByUserName.stream().forEach(u -> {
                System.out.println("删除用户:" + u);
                userRepository.delete(u);
            });
        }

        userRepository.save(user);
        System.out.println(user.getId());

        long count = userRepository.count();
        assert (count > 0);
    }

    @Test
    public void testUserPageQuery() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<User> all = userRepository.findAll(pageable);
        System.out.println(all.getTotalElements());
        System.out.println(all.getTotalPages());
    }

    @Test
    public void testModifyUser() {
        List<User> users = userRepository.findByUserName("张三");
        int r = userRepository.modifyUserNameById("李四", users.get(0).getId());
        assert r > 0;
    }

    @Test
    public void testJoinQuery() {
        UserAvgScore userAvgScores = userRepository.queryUserAvgScore(8l);
        System.out.println(userAvgScores);

        UserMaxScore userMaxScore = userRepository.queryUserMaxScore(8l);
        System.out.println(userMaxScore.getUserName() + ":" + userMaxScore.getMaxScore());
    }
}
