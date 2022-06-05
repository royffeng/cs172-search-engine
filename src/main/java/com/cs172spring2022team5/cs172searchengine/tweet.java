package com.cs172spring2022team5.cs172searchengine;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter

public class tweet {
    @Id
    private String _id;
    private String id;
    private String tweet_text;
    private String created_at;
    private String name;
    private String screen_name;
}
