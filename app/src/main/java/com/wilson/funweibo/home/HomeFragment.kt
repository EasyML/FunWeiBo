package com.wilson.funweibo.home

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.exception.WeiboException
import com.sina.weibo.sdk.net.RequestListener
import com.wilson.funweibo.R
import com.wilson.funweibo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.jetbrains.anko.support.v4.onUiThread
import java.io.IOException


class HomeFragment : BaseFragment(), View.OnClickListener,RequestListener {



    companion object {
        const val HOME = "HomeFragment"
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }

    override fun inflate(): Int {
        return R.layout.fragment_home
    }

    override fun init() {
        recyc_weibo_home.adapter = WeiBoAdapter(context)
        recyc_weibo_home.layoutManager = LinearLayoutManager(context)
        recyc_weibo_home.setHasFixedSize(true)

        ktp_home.setRefreshListener {
           loadData()
        }
    }

    private fun loadData() {
        val token = AccessTokenKeeper.readAccessToken(context)
//        val params = WeiboParameters(Constant.APP_KEY)
//        params.put("access_token", token)

        val url = "https://api.weibo.com/2/statuses/home_timeline.json?access_token=${token.token}"
        val show = "https://api.weibo.com/2/users/show.json?access_token=${token.token}&uid=${token.uid}"
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url(show).get().build()

        val call = okHttpClient.newCall(request)
        call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Response","----onFailure-----: ${e.message}" )
                onUiThread {
                    ktp_home.stopRefresh()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                onUiThread {
                    ktp_home.stopRefresh()
                }
                Log.e("Response","----onResponse-----: "+response.body()?.string())
//
            }


        })

    }


    override fun onClick(v: View?) {
        when(v?.id){


        }
    }
    override fun onWeiboException(p0: WeiboException?) {
        Log.e("Response","-------: $p0" )
        ktp_home.stopRefresh()
    }

    override fun onComplete(p0: String?) {
        Log.e("Response","-------: $p0" )
        ktp_home.stopRefresh()
    }

}
