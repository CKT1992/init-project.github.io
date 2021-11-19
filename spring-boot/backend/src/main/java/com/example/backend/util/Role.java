package com.example.backend.util;

public enum Role {
    Admin(1),
    Manager(2),
    User(3);


    private final int value;
    private Role(int value) {
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
