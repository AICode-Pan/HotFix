package com.aihua.hotfix.activity

import android.app.Activity
import android.os.Bundle
import com.aihua.hotfix.R
import com.aihua.hotfix.utils.TestUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvContent.text = TestUtils.getContent()
    }
}