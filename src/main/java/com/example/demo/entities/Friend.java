package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private long friendId;
    private String email;
    private String firstName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    public Friend() {
    }


    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonIgnore
    public User getUserId() {
        return user_id;
    }

    @JsonProperty
    public void setUserId(User user) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendId=" + friendId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
