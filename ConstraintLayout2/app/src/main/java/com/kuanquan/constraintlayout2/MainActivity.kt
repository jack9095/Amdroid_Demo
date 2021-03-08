package com.kuanquan.constraintlayout2

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import android.widget.SeekBar
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_group_orientation.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_orientation_vertical -> Flow.VERTICAL
                    else -> Flow.HORIZONTAL
                }.let {
                    TransitionManager.beginDelayedTransition(parentContainer)
                    flow.setOrientation(it)
                }
            }
        }
        button_group_wrap_modes.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_wrap_mode_chain -> Flow.WRAP_CHAIN
                    R.id.button_wrap_mode_align -> Flow.WRAP_ALIGNED
                    else -> Flow.WRAP_NONE
                }.let {
                    TransitionManager.beginDelayedTransition(parentContainer)
                    flow.setWrapMode(it)
                }
            }
        }
        button_group_vertical_chain_style.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_vertical_chain_style_spread -> Flow.CHAIN_SPREAD
                    R.id.button_vertical_chain_style_spread_inside -> Flow.CHAIN_SPREAD_INSIDE
                    else -> Flow.CHAIN_PACKED
                }.let {
                    TransitionManager.beginDelayedTransition(parentContainer)
                    flow.setVerticalStyle(it)
                }
            }
        }
        button_group_horizontal_chain_style.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_horizontal_chain_style_spread -> Flow.CHAIN_SPREAD
                    R.id.button_horizontal_chain_style_spread_inside -> Flow.CHAIN_SPREAD_INSIDE
                    else -> Flow.CHAIN_PACKED
                }.let {
                    TransitionManager.beginDelayedTransition(parentContainer)
                    flow.setHorizontalStyle(it)
                }
            }
        }
        seekbar_horizontal_bias.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> flow.setHorizontalBias(value / 100f) })
        }
        seekbar_vertical_bias.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> flow.setVerticalBias(value / 100f) })
        }
        seekbar_horizontal_gap.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> flow.setHorizontalGap(value) })
        }
        seekbar_vertical_gap.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> flow.setVerticalGap(value) })
        }
        button_group_horizontal_alignments.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(parentContainer)
                when (checkedId) {
                    R.id.button_horizontal_alignment_end -> Flow.HORIZONTAL_ALIGN_END
                    R.id.button_horizontal_alignment_start -> Flow.HORIZONTAL_ALIGN_START
                    else -> Flow.HORIZONTAL_ALIGN_CENTER
                }.let {
                    flow.setHorizontalAlign(it)
                }
            }
        }
        button_group_vertical_alignments.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(parentContainer)
                when (checkedId) {
                    R.id.button_vertical_alignment_top -> Flow.VERTICAL_ALIGN_TOP
                    R.id.button_vertical_alignment_bottom -> Flow.VERTICAL_ALIGN_BOTTOM
                    R.id.button_vertical_alignment_baseline -> Flow.VERTICAL_ALIGN_BASELINE
                    else -> Flow.VERTICAL_ALIGN_CENTER
                }.let {
                    flow.setVerticalAlign(it)
                }
            }
        }
    }
}

inline fun SeekBar.addSeekBarChangeListener(
        crossinline startTrackingTouch: (p0: SeekBar?) -> Unit = {},
        crossinline stopTrackingTouch: (p0: SeekBar?) -> Unit = {},
        crossinline progressChanged: (p0: SeekBar?, p1: Int, p2: Boolean) -> Unit
) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            progressChanged(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            startTrackingTouch(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            stopTrackingTouch(seekBar)
        }
    })
}

class CircularRevealHelper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    override fun updatePostLayout(container: ConstraintLayout?) {
        super.updatePostLayout(container)
        getViews(container).forEach { view ->
            ViewAnimationUtils.createCircularReveal(
                    view, view.width / 2,
                    view.height / 2, 0f,
                    hypot((view.height / 2.0), (view.width / 2.0)).toFloat()
            ).apply {
                duration = 1000
                start()
            }
        }
    }
}

class FlyingHelper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Layer(context, attrs, defStyleAttr) {

    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)
        val centerPoint = PointF(((left + right) / 2).toFloat(), ((top + bottom) / 2).toFloat())
        ValueAnimator.ofFloat(0f, 1f).setDuration(1000).apply {
            addUpdateListener { animation ->
                val animatedFraction = animation.animatedFraction
                updateTranslation(centerPoint, animatedFraction, container)
            }
            start()
        }
    }

    private fun updateTranslation(centerPoint: PointF, animatedFraction: Float, container: ConstraintLayout) {
        val views = getViews(container)
        for (view in views) {
            val viewCenterX = (view.left + view.right) / 2
            val viewCenterY = (view.top + view.bottom) / 2
            val startTranslationX = if (viewCenterX < centerPoint.x) -2000f else 2000f
            val startTranslationY = if (viewCenterY < centerPoint.y) -2000f else 2000f
            view.translationX = (1 - animatedFraction) * startTranslationX
            view.translationY = (1 - animatedFraction) * startTranslationY
        }
    }
}