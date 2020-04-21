package com.aihua.hotfix.utils;

public class TestUtils {
    public static String getContent() {
        String content = "这是未被修复的页面";
        //setp 1 : 修改content字段
//        String content = "这是修复后的页面";

        //step 2 : 将这个类打包到dex文件中
        return content;
    }
}
