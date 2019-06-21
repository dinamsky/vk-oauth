package com.example.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @Column(nullable = false,unique = true)
    private String username;

    private String owner;

    public Bot(String token, String username) {

        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }



    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bot bot = (Bot) o;
        return Objects.equals(id, bot.id) &&
                Objects.equals(token, bot.token) &&
                Objects.equals(username, bot.username);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, username);
    }
}
