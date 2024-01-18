package com.example.battleships_server.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Long score;
    public Score(Long score) {
        this.score = score;
    }

    public Score() {

    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }


    //NOT USED I THINK? LEFT JUST IN CASE
}