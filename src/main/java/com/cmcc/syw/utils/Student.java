package com.cmcc.syw.utils;

import java.io.Serializable;

/**
 * Created by sunyiwei on 16/4/27.
 */
public class Student extends Person implements Serializable {
    private int grade;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
