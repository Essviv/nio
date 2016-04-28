package com.cmcc.syw.utils;

import java.io.Serializable;

/**
 * utils class
 * <p>
 * Created by sunyiwei on 16/4/27.
 */
public class Person implements Serializable {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
