package com.aihua.hotfix.utils

class TestUtil {
    companion object {
        fun getContent() : String {
            var content = "这是未被修复的页面"
            //setp 1 : 修改content字段
//            var content = "这是修复后的页面"

            //step 2 : 将这个类打包到dex文件中
            return content
        }
    }
}