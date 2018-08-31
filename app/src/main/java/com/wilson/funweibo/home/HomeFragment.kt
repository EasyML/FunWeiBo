package com.wilson.funweibo.home

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wilson.funweibo.R
import com.wilson.funweibo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_weibo_content.*


class HomeFragment : BaseFragment(), View.OnClickListener {


    private lateinit var rcvHome: RecyclerView

    companion object {
        const val HOME = "HomeFragment"
    }

    override fun inflate(): Int {
        return R.layout.fragment_home
    }

    override fun init() {
        rcvHome = contentView.findViewById(R.id.recyc_weibo_home)
        rcvHome.adapter = WeiBoAdapter(context!!)
        rcvHome.layoutManager = LinearLayoutManager(context)
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }


    override fun onClick(v: View?) {
        when(v?.id){


        }
    }


}
