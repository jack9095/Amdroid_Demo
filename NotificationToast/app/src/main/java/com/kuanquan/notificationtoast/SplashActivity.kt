package com.kuanquan.notificationtoast

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import com.kuanquan.notificationtoast.other.FirstActivity
import com.kuanquan.notificationtoast.other.PushResultActivity
import com.kuanquan.notificationtoast.service.NotificationDisplayService
import java.util.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    fun comment_notifiction(view: View) {
//        showNotification(this)
        createNotification("比心APP","点击进入与好友一起看视频", this)
    }
    fun progress_notifiction(view: View) {
//        showNotificationProgress(this)

        val startNotificationServiceIntent = Intent(this, NotificationDisplayService::class.java)
        startService(startNotificationServiceIntent)
    }
    fun suspended_notifiction(view: View) {
        showFullScreen(this)
    }
    fun folding_notifiction(view: View) {
        shwoNotify(this)
    }
    fun clickMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
    fun clickFirst(view: View) {
        startActivity(Intent(this, FirstActivity::class.java))
    }

    // 显示一个普通的通知栏
    private fun showNotification(context: Context) {
        val notification: Notification = NotificationCompat.Builder(context)
                /**设置通知左边的大图标 */
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标 */
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的 */
                .setTicker("通知来了")
                /**设置通知的标题 */
                .setContentTitle("这是一个通知的标题")
                /**设置通知的内容 */
                .setContentText("这是一个通知的内容这是一个通知的内容")
                /**通知产生的时间，会在通知信息里显示 */
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级 */
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消 */
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接) */
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合： */
                .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context, 1, Intent(context, MainActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT))
                .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /**发起通知 */
        notificationManager.notify(0, notification)
    }

    // 显示一个下载带进度条的通知
    private fun showNotificationProgress(context: Context) {
        //进度条通知
        val builderProgress = NotificationCompat.Builder(context)
        builderProgress.setContentTitle("下载中")
        builderProgress.setSmallIcon(R.mipmap.ic_launcher)
        builderProgress.setTicker("进度条通知")
        builderProgress.setProgress(100, 0, false)
        val notification = builderProgress.build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //发送一个通知
        notificationManager.notify(2, notification)
        /**创建一个计时器,模拟下载进度 */
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            var progress = 0
           override fun run() {
                Log.e("progress", progress.toString() + "")
                while (progress <= 100) {
                    progress++
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    //更新进度条
                    builderProgress.setProgress(100, progress, false)
                    //再次通知
                    notificationManager.notify(2, builderProgress.build())
                }
                //计时器退出
                this.cancel()
                //进度条退出
                notificationManager.cancel(2)
                return  //结束方法
            }
        }, 0)
    }

    /**
     * 显示一个悬挂式的通知
     *   悬挂式,部分系统厂商可能不支持
     */
    @SuppressLint("WrongConstant")
    private fun showFullScreen(context: Context) {
//        val builder = NotificationCompat.Builder(context)
        val builder = getNotificationBuilder(context)!!
        val mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/linglongxin24"))
        val pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0)
        builder.setContentIntent(pendingIntent)
        builder.setCategory("${Notification.FLAG_ONGOING_EVENT}")
        builder.setVisibility(Notification.VISIBILITY_PUBLIC)
        val intent2 = Intent()
        val pi = PendingIntent.getBroadcast(this, 0, intent2, 0)
        builder.setFullScreenIntent(pi, true)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
        builder.setAutoCancel(true)
        builder.setContentTitle("悬挂式通知")
        builder.setPriority(NotificationManager.IMPORTANCE_HIGH)
        //设置点击跳转
        val hangIntent = Intent()
        hangIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        hangIntent.setClass(context, MainActivity::class.java)
        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
        val hangPendingIntent = PendingIntent.getActivity(context, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setFullScreenIntent(hangPendingIntent, true)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(3, builder.build())
    }

    // 显示一个折叠式的通知
    private fun shwoNotify(context: Context) {
        //先设定RemoteViews
        val view_custom = RemoteViews(context.packageName, R.layout.view_custom)
        //设置对应IMAGEVIEW的ID的资源图片
        view_custom.setImageViewResource(R.id.custom_icon, R.mipmap.ic_launcher)
        view_custom.setTextViewText(R.id.tv_custom_title, "今日头条")
        view_custom.setTextColor(R.id.tv_custom_title, Color.BLACK)
        view_custom.setTextViewText(R.id.tv_custom_content, "金州勇士官方宣布球队已经解雇了主帅马克-杰克逊，随后宣布了最后的结果。")
        view_custom.setTextColor(R.id.tv_custom_content, Color.BLACK)
//        val mBuilder = NotificationCompat.Builder(context)
        val mBuilder = getNotificationBuilder(context)!!
        mBuilder.setContent(view_custom)
                .setContentIntent(PendingIntent.getActivity(context, 4, Intent(context, MainActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis()) // 通知产生的时间，会在通知信息里显示
                .setTicker("有新资讯")
                .setPriority(Notification.PRIORITY_HIGH) // 设置该通知优先级
                .setOngoing(false) //不是正在进行的   true为正在进行  效果和.flag一样
                .setSmallIcon(R.mipmap.ic_launcher)
        val notify = mBuilder.build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(4, notify)
    }

    // 8.0通知栏新增通知渠道
    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id", "channel_name",
                    NotificationManager.IMPORTANCE_DEFAULT)
            //是否绕过请勿打扰模式
            channel.canBypassDnd()
            //闪光灯
            channel.enableLights(true)
            //锁屏显示通知
            channel.lockscreenVisibility = VISIBILITY_SECRET
            //闪关灯的灯光颜色
            channel.lightColor = Color.RED
            //桌面launcher的消息角标
            channel.canShowBadge()
            //是否允许震动
            channel.enableVibration(true)
            //获取系统通知响铃声音的配置
            channel.audioAttributes
            //获取通知取到组
            channel.group
            //设置可绕过  请勿打扰模式
            channel.setBypassDnd(true)
            //设置震动模式
            channel.vibrationPattern = longArrayOf(100, 100, 200)
            //是否会有灯光
            channel.shouldShowLights()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "channel_id")
        notification.setContentTitle("新消息来了")
        notification.setContentText("周末到了，不用上班了")
        notification.setSmallIcon(R.mipmap.ic_launcher)
        notification.setAutoCancel(true)
        return notification
    }


    @SuppressLint("WrongConstant")
    private fun createNotification(title: String?, content: String?, mContext: Context) {
        val vibrate = longArrayOf(0, 40, 20, 40, 20, 40, 20, 40, 20, 40, 20, 40)
        val notificationId = 0x1001
        // 点击通知跳转的页面
        val intentClick = Intent(mContext, PushResultActivity::class.java)
        intentClick.action = PushResultActivity.ACTION_CLICK
        val pendingIntentClick = PendingIntent
                .getActivity(mContext, notificationId.hashCode(), intentClick,
                        PendingIntent.FLAG_ONE_SHOT)
//        val notificationBuilder: Notification.Builder = Notification.Builder(mContext)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(mContext)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(vibrate)
                .setTicker(title)
                .setColor(resources.getColor(R.color.colorAccent))
//                .setPriority(Notification.PRIORITY_DEFAULT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI)
                .setContentIntent(pendingIntentClick)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText("text"))
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setCategory(Notification.CATEGORY_STATUS)
        }
        val mNotificationManager = mContext.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id_push",
                    mContext.packageName,
                    NotificationManager.IMPORTANCE_HIGH)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            mNotificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId(channel.id)
//            notificationBuilder.setSmallIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
        }

        mNotificationManager.notify(notificationId, notificationBuilder.build())
    }

}