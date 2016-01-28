package com.cmcc.syw.file;

/**
 * Created by sunyiwei on 16-1-27.
 */
public class Formatter {
    public static void main(String[] args) {
        int i = 2;
        double r = Math.sqrt(i);
        System.out.format("The square of %d is sqrt(%<d) = %f.%n", i, r);
    }
}
