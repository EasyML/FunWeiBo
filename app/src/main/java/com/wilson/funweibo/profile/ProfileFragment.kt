package com.wilson.funweibo.profile

import com.wilson.funweibo.R
import com.wilson.funweibo.base.BaseFragment

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
class ProfileFragment: BaseFragment() {

    companion object {
        const val PROFLIE = "ProfileFragment"
    }
    override fun inflate(): Int {
        return R.layout.fragment_profile
    }

    override fun init() {
    }
}
