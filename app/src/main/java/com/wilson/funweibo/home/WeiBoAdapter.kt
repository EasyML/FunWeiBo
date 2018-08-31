package com.wilson.funweibo.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wilson.funweibo.R

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
class WeiBoAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return WeiBoHolder(LayoutInflater.from(context).inflate(R.layout.home_weibo_content, parent,false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    inner class WeiBoHolder(view: View): RecyclerView.ViewHolder(view)

}
