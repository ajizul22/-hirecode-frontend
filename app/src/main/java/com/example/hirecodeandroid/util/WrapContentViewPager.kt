package com.example.hirecodeandroid.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class WrapContentViewPager : ViewPager {
    constructor(context: Context?) : super(context!!) {
        initPageChangeListener()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initPageChangeListener()
    }

    private fun initPageChangeListener() {
        addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                requestLayout()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val child: View? = getChildAt(currentItem)
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h: Int = child.getMeasuredHeight()
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}