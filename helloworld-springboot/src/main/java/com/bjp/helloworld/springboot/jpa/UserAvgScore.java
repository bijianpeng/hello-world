package com.bjp.helloworld.springboot.jpa;

import lombok.Data;

@Data
public class UserAvgScore {
    private String userName;
    private Double avgScore;

    public UserAvgScore(String userName, Double avgScore) {
        this.userName = userName;
        this.avgScore = avgScore;
    }
}
