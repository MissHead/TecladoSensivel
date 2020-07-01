package com.izabela.tecladosensivel.components

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.annotation.CallSuper
import com.izabela.tecladosensivel.components.utilities.ComponentUtils


abstract class ResizableRelativeLayout(
    context: Context, attr: AttributeSet) :
    RelativeLayout(context, attr) {

    val Int.toPx: Int
        get() = ComponentUtils.dpToPx(context, this)

    val Int.toDp: Int
        get() = ComponentUtils.pxToDp(context, this)

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        resetContent()
    }

    @CallSuper
    fun resetContent() {
        removeAllViews()
        Handler().postDelayed({ configureSelf() }, 50)
    }

    protected abstract fun configureSelf()
}