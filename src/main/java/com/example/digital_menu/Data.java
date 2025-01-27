package com.example.digital_menu;

import java.io.Serializable;

public class Data implements Serializable, Cloneable {
    public String message;

    @Override
    public String clone() throws CloneNotSupportedException {
        return (String) super.clone();
    }
}
