package com.kuanquan.notificationtoast

import android.app.NotificationManager
import android.content.*
import android.os.*
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.notificationtoast.notification.NotificationFactory
import com.kuanquan.notificationtoast.other.SecondActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun show_second_page(view: View?) {
        startActivity(Intent(this, SecondActivity::class.java))
    }

    fun show_notification(view: View?) {
        Toast.makeText(this, "发送到通知栏列表的通知", Toast.LENGTH_SHORT).show()
        showNotification()
    }

    fun show_notification2(view: View?) {
        val notificationId = 0x1001
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val factory = NotificationFactory(applicationContext, manager)
        val notification = factory.createNotification("id1", "系统通知标题", "系统通知内容")
        factory.clear()
        manager.notify(notificationId, notification)


        val notificationView = layoutInflater.inflate(R.layout.view_notification_toast, null)
        notificationView.findViewById<TextView>(R.id.title).text = "横幅通知标题"
        notificationView.findViewById<TextView>(R.id.content).text = "横幅通知内容"
        findTopView()?.addView(notificationView)
        notificationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.notification_toast_enter))

        // 延时 3 秒消失横幅通知栏
        notificationView.postDelayed({
            // 取消系统通知
            manager.cancel(notificationId)

            val parent = notificationView.parent
            if (parent is ViewGroup) {
                parent.removeView(notificationView)
            }
        }, 3000)
    }

    private fun findTopView(): ViewGroup? {
        var view = findViewById<View>(Window.ID_ANDROID_CONTENT)
        var frameLayout: FrameLayout? = null
        while (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
            if (parent is FrameLayout) {
                frameLayout = parent
            }
        }
        return frameLayout
    }

    // 显示到通知栏列表
    private fun showNotification() {
        val notificationId = 0x1001
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val factory = NotificationFactory(applicationContext, manager)
//        val notification = factory.createNotification("id1", "通知标题", "通知内容")
        val notification = factory.createNotificationNew("id1", "通知标题", "通知内容")
        factory.clear()
        manager.notify(notificationId, notification)
    }
}