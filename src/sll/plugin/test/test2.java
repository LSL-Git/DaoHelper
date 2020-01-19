package sll.plugin.test;


import groovy.json.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSL on 2020/1/15 16:29
 */
public class test2 {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("123");
        sb.insert(0, "0");
        System.out.println(sb.toString());
    }
}
