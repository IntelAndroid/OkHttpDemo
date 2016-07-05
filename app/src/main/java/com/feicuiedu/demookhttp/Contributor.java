package com.feicuiedu.demookhttp;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class Contributor {

    String login;
    int contributions;

    @Override public String toString() {
        return login + ", " + contributions;
    }
}
