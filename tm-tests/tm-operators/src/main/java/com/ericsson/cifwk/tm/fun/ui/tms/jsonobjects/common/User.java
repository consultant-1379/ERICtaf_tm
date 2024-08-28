package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

/**
 * Created by egergle on 09/08/2016.
 */
public class User {

    private String signum;

    private String name;

    public User (String signum, String name) {
        this.signum = signum;
        this.name = name;
    }

    public String getSignum() {
        return signum;
    }

    public void setSignum(String signum) {
        this.signum = signum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
