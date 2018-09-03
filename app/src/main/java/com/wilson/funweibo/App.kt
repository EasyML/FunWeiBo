package com.wilson.funweibo

import android.app.Application
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.wilson.funweibo.base.Constant.Companion.APP_KEY
import com.wilson.funweibo.base.Constant.Companion.REDIRECT_URL
import com.wilson.funweibo.base.Constant.Companion.SCOPE

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        WbSdk.install(this, AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE))
    }



}
