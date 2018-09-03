package com.wilson.funweibo.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wilson.funweibo.R
import com.wilson.funweibo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), View.OnClickListener {



    companion object {
        const val HOME = "HomeFragment"
    }

    override fun inflate(): Int {
        return R.layout.fragment_home
    }

    override fun init() {
        recyc_weibo_home.adapter = WeiBoAdapter(context)
        recyc_weibo_home.layoutManager = LinearLayoutManager(context)
        recyc_weibo_home.setHasFixedSize(true)
    }



    override fun onClick(v: View?) {
        when(v?.id){


        }
    }


}
