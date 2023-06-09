package com.example.diplom.model;

import java.security.KeyPair;

public class User {

    String name;
    KeyPair kaypair;

    public User(String name, KeyPair kaypair) {
        this.name = name;
        this.kaypair = kaypair;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyPair getKaypair() {
        return kaypair;
    }

    public void setKaypair(KeyPair kaypair) {
        this.kaypair = kaypair;
    }
}
