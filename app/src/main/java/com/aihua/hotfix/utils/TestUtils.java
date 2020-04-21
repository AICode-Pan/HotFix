package com.aihua.hotfix.utils;

public class TestUtils {
    public static String getContent() {
        String content = "这是未被修复的页面";
        //setp 1 : 修改content字段
//        String content = "这是修复后的页面";

        //step 2 : 将这个类打包到dex文件中
        //step 2.1 : 通过 javac -encoding utf-8 TestUtils.java 编译成class文件
        //step 2.2 : 通过 jar cvfe dexPatch.jar a * 打包成jar文件
        //我已经打包好了，在项目路径下。可以直接通过adb命令，push到/storage/emulated/0/设备下
        return content;
    }
}
