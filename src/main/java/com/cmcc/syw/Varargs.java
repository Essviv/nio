package com.cmcc.syw;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sunyiwei on 2016/1/28.
 */
public class Varargs {
    private static void test(String... args){
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    public static void main(String[] args) {
        List<String> varargs = new LinkedList<String>();
        varargs.add("Hello");
        varargs.add("world");
        varargs.add("fkdaslkf");
        varargs.add("fdaslk");
        varargs.add("fujian");

        String[] strings = (String[])varargs.toArray(new String[varargs.size()]);
        test(strings);
    }
}
