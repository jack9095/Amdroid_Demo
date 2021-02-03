package com.kuanquan.utilwidget

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.kuanquan.commentutillibrary.ShapeBuilder


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val drawable = ShapeBuilder()
            .setCornerRadius(ShapeBuilder.ANGLE_TOP_LEFT, 10f)
            .setCornerRadius(ShapeBuilder.ANGLE_BOTTOM_LEFT, 10f)
            .setGradientColors(intArrayOf(Color.parseColor("#000000"), Color.parseColor("#999999"), Color.parseColor("#ffffff")))
            .setOrientation(GradientDrawable.Orientation.LEFT_RIGHT).build()
        textView?.background = drawable
    }
}