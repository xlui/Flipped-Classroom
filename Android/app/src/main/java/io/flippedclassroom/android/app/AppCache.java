package io.flippedclassroom.android.app;


public class AppCache {
    private static String id;

    public static void saveId(String userId) {
        id = userId;
    }

    public static String getId() {
        return id;
    }
}
