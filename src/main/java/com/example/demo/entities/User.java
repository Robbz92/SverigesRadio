package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "friends", // cross table
            joinColumns = @JoinColumn(name = "user"), // user column
            inverseJoinColumns = @JoinColumn(name = "friend_id") // the friend column
    )
    @JsonIgnoreProperties("friends") // ignore the property from the related entity
    private List<User> friends;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;

    public void addFriend(User user){
        friends.add(user);
    }

    public void removeFriend(long id){
        friends.remove(id);
    }

    public User() {
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public List<User> getFriends() {
        return friends;
    }

    @JsonProperty
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @JsonIgnore
    public List<Favorite> getFavorites() {return favorites;}

    @JsonProperty
    public void setFavorites(List<Favorite> favorites) {this.favorites = favorites;}

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
