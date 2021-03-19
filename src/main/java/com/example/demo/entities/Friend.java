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

    @Column(name = "user")
    private long userId;

    @ManyToOne
    @JoinColumn(name = "user", insertable = false,updatable = false)
    private User user;

    public Friend(String email, String firstName, long userId) {
        this.email = email;
        this.firstName = firstName;
        this.userId = userId;
    }

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
    public long getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendId=" + friendId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", user_id=" + user +
                '}';
    }
}
