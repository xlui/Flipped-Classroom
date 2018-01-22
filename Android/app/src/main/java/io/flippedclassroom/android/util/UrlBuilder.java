package io.flippedclassroom.android.util;

public class UrlBuilder {
    private final static String BASE_URL = "https://fc.xd.style/";

    public static String getLoginTokenUrl() {
        return BASE_URL + "login";
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
