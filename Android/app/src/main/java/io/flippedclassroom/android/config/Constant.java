package io.flippedclassroom.android.config;

/**
 * Created by szh on 2017/5/14.
 * 常量字符串
 */

public class Constant {
    /**********************TYPE相关*******************************/
    public static final int  IMAGE_MESSAGE = 0;
    public static final int  NORMAL_MESSAGE = 1;

    /*********************请求相关********************************/
    //请求分页数据默认每页10条数据
    public static final int DEAULT_NUM_PER_PAGE = 10;
    //请求成功和失败
    public static final String REQUEST_SUCCESS = "200";


    /*********************文件命名相关********************************/
    public static final String FILE_NAME = "aipapp_preferences";

    /**********************Activity之间的通信************************/


    /**********************ActivityForResult的请求码************************/
    public static final int CAMERA_CODE = 1;
    public static final int GALLERY_CODE = 2;
    public static final int CROP_CODE = 3;

    /**********************缓存的图片保存目录************************/
    public static final String CONTRIBUTE_IMG_PATH = "contribute";
    public static final String CONTRIBUTE_IMG_FOLDER = "images";
    public static final String CONTRIBUTE_IMG_ZIP_PATH = "zip";
    public static final String CONTRIBUTE_IMG_ZIP_NAME = "contribute.zip";

}
