package io.flippedclassroom.android.util;

//Url的构造工具类
public class UrlBuilder {
    private final static String BASE_URL = "https://fc.xd.style/";

    public static String getLoginTokenUrl() {
        return BASE_URL + "login";
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getRegisteredUrl() {
        return BASE_URL+"register";
    }
}
