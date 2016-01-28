package com.cmcc.syw.scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class Scanner {
    public static void main(String[] args) throws FileNotFoundException {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\test.txt";

        java.util.Scanner scanner = new java.util.Scanner(new InputStreamReader(new FileInputStream(filename)));
        scanner.useDelimiter("T");

        while(scanner.hasNext()){
            System.out.println(scanner.next());
        }

        scanner.close();
    }
}
