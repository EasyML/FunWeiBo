package com.wilson.funweibo.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
abstract class BaseFragment: Fragment() {

    abstract fun inflate(): Int

    abstract fun init()

    lateinit var contentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = LayoutInflater.from(context).inflate(inflate(),container,false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }



}
