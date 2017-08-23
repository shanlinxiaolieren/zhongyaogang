package com.zhongyaogang.utils;

import android.os.Environment;

public class FileDirectory {

    /** 存储总目录 */
    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/qfy/";

    /** 图片存储目录 */
    public static final String PICTURE_PATH = PATH + "picture/";
    /** 意见反馈目录 */
    public static final String YIJIANFANKUI = PICTURE_PATH + "yijianfankui/";
    /** 拍照临时文件路径 */
    public static String pzls = null;
    /** 选择本地图片 */
    public static final String BD = PICTURE_PATH + "bd/";
}
