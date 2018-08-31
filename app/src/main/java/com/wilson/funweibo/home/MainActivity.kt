package com.wilson.funweibo.home

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.wilson.funweibo.R
import com.wilson.funweibo.discovery.DiscoverFragment
import com.wilson.funweibo.message.MessageFragment
import com.wilson.funweibo.profile.ProfileFragment
import kotlinx.android.synthetic.main.main_bottom_navigation_bar.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        tabs = arrayOf(iv_home, iv_message,iv_add,iv_discovery,iv_profile)
        val clicks = arrayOf(layout_home_tab,layout_message_tab,layout_add_tab,layout_discovery_tab,layout_profile_tab)
        for( v in clicks){
            v.setOnClickListener(this)
        }
        home = HomeFragment()
        message = MessageFragment()
        discover = DiscoverFragment()
        profile = ProfileFragment()
        transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.layout_main_content,home,HomeFragment.HOME).commit()

    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.layout_home_tab -> {
                refreshView(TAB_HOME)
            }

            R.id.layout_message_tab -> {
                refreshView(TAB_MESSAGE)
            }

            R.id.layout_add_tab -> {
                refreshView(TAB_ADD)
            }
            R.id.layout_discovery_tab -> {
                refreshView(TAB_DISCOVER)
            }

            R.id.layout_profile_tab -> {
                refreshView(TAB_ME)
            }
        }

    }

    private fun refreshView(index: Int) {
     for ( i in tabs.indices){
         tabs[i].isSelected = i == index
     }
    }

    private fun switchToFragment(index: Int){
        transaction = supportFragmentManager.beginTransaction()
        hideAllFragments(transaction)



    }

    private fun hideAllFragments(tsa: FragmentTransaction){
        tsa.hide(home)
        tsa.hide(message)
        tsa.hide(discover)
        tsa.hide(profile)
        tsa.commit()
        refreshView(-1)
    }
}
