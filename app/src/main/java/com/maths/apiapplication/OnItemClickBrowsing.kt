package com.maths.apiapplication

import androidx.viewpager2.widget.ViewPager2

interface OnItemClickBrowsing {
    fun onLeftButtonClick(position: Int, viewPager: ViewPager2)
    fun onRightButtonClick(position: Int, viewPager: ViewPager2)
}