package com.wilson.funweibo.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.wilson.funweibo.R
import com.wilson.funweibo.discovery.DiscoverFragment
import com.wilson.funweibo.message.MessageFragment
import com.wilson.funweibo.profile.ProfileFragment
import kotlinx.android.synthetic.main.main_bottom_navigation_bar.*

class MainActivity : AppCompatActivity(), View.OnClickListener,WbAuthListener {



    private lateinit var home: HomeFragment
    private lateinit var message: MessageFragment
    private lateinit var discover: DiscoverFragment
    private lateinit var profile: ProfileFragment

    private lateinit var transaction: FragmentTransaction

    companion object {
        private const val TAB_HOME = 0
        private const val TAB_MESSAGE = 1
        private const val TAB_ADD = 2
        private const val TAB_DISCOVER = 3
        private const val TAB_ME = 4

    }

    private var tabs = emptyArray<View>()
    private var fragments = emptyArray<Fragment>()
    private var currentIndex = 0

    private var ssoHandler: SsoHandler? = null
    private var accessToken: Oauth2AccessToken? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val token = AccessTokenKeeper.readAccessToken(this)
        if(System.currentTimeMillis()>token.expiresTime){
            loginWeiBo()
        }else{
            initViews()
        }

    }

    private fun initViews() {
        tabs = arrayOf(iv_home, iv_message,iv_add,iv_discovery,iv_profile)
        val clicks = arrayOf(layout_home_tab,layout_message_tab,layout_add_tab,layout_discovery_tab,layout_profile_tab)
        for( v in clicks){
            v.setOnClickListener(this)
        }

        initFragments()
    }

    private fun initFragments(){
        home = HomeFragment()
        message = MessageFragment()
        discover = DiscoverFragment()
        profile = ProfileFragment()
        fragments = arrayOf(home,message,Fragment(),discover,profile)
        transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.layout_main_content,home,HomeFragment.HOME)
        transaction.add(R.id.layout_main_content,message,MessageFragment.MESSAGE)
        transaction.add(R.id.layout_main_content,discover,DiscoverFragment.DISCOVER)
        transaction.add(R.id.layout_main_content,profile,ProfileFragment.PROFLIE)

        transaction.hide(message)
        transaction.hide(discover)
        transaction.hide(profile)

        transaction.commit()
        refreshView(TAB_HOME)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ssoHandler?.authorizeCallBack(requestCode,resultCode,data)
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.layout_home_tab -> {
                switchToFragment(TAB_HOME)
            }

            R.id.layout_message_tab -> {
                switchToFragment(TAB_MESSAGE)
            }

            R.id.layout_add_tab -> {
                refreshView(TAB_ADD)
            }
            R.id.layout_discovery_tab -> {
                switchToFragment(TAB_DISCOVER)
            }

            R.id.layout_profile_tab -> {
                switchToFragment(TAB_ME)
            }
        }

    }

    private fun refreshView(index: Int) {
     for ( i in tabs.indices){
         tabs[i].isSelected = i == index
     }
    }

    private fun switchToFragment(index: Int){


        if(currentIndex == index){

        }else {
            transaction = supportFragmentManager.beginTransaction()
            hideAllFragments(transaction)
            transaction.show(fragments[index])
            currentIndex = index
            refreshView(index)
        }




    }

    private fun hideAllFragments(tsa: FragmentTransaction){
        tsa.hide(home)
        tsa.hide(message)
        tsa.hide(discover)
        tsa.hide(profile)
        tsa.commit()
        refreshView(-1)
    }


    private fun loginWeiBo(){
        if(ssoHandler == null){
            ssoHandler = SsoHandler(this)
        }
        ssoHandler?.authorize(this@MainActivity)
    }


    override fun onSuccess(token: Oauth2AccessToken?) {
        this@MainActivity.runOnUiThread {
            accessToken = token
            if(accessToken!!.isSessionValid){
                AccessTokenKeeper.writeAccessToken(this@MainActivity, accessToken)
                initViews()
                Toast.makeText(this@MainActivity,
                        "授权成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFailure(error: WbConnectErrorMessage?) {
        Toast.makeText(this@MainActivity, error?.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun cancel() {
        Toast.makeText(this@MainActivity, "Cancel OAuth!", Toast.LENGTH_LONG).show()
    }
}
