package com.lw.mvplibrary.bean;

import org.litepal.crud.LitePalSupport;

public class Country extends LitePalSupport {
    //名称
    private String name;
    //代码
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
