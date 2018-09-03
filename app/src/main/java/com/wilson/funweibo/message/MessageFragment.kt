package com.wilson.funweibo.message

import com.wilson.funweibo.R
import com.wilson.funweibo.base.BaseFragment

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
class MessageFragment: BaseFragment() {

    companion object {
        const val MESSAGE = "MessageFragment"
    }

    override fun inflate(): Int {
        return R.layout.fragment_message
    }

    override fun init() {
    }
}
