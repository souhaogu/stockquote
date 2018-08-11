package com.soustock.stockquote.common;

/**
 * Created by xuyufei on 2018/8/11.
 * 复权类型
 */
public enum FuquanKind {

    Origin("0", "不复权"),
    Front("1","前复权"),
    Behind("2","后复权");

    private final String code;

    private final String name;

    FuquanKind(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static FuquanKind getByCode(String code){
        switch (code){
            case "1":
                return Front;
            case "2":
                return Behind;
            default:
                return Origin;
        }
    }

}
