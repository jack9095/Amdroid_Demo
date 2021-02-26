package com.kuanquan.panelemojikeyboard

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.library.PanelSwitchHelper
import com.kuanquan.library.view.panel.PanelView
import com.kuanquan.panelemojikeyboard.emotion.EmotionBean
import com.kuanquan.panelemojikeyboard.emotion.EmotionRecyclerView
import com.kuanquan.panelemojikeyboard.util.EmojiData
import com.kuanquan.panelemojikeyboard.util.SpUtil

/**
 * 演示如何正确使用 auto_reset (点击内容区域自动隐藏面板) 的功能
 * auto_reset 可以被定义在 container 的扩展属性内,包含
 * 1. auto_reset_enable 表示是否支持点击内容区域内隐藏面板，默认打开。
 *    打开时，当区域内子view没有消费事件时，则会默认消费该事件并自动隐藏。
 * 2. auto_reset_area，当且仅当 auto_reset_enable 为 true 才有效，指定一个 view 的id，为 1 的消费事件限定区域。
 *    比如场景一，指定了空白透明 view  ，view 没有消费事件时，则才会自动隐藏面板；
 *    比如场景二，指定了列表的 recyclerview ，则recyclerview 没有消费事件时，则才会自动隐藏面板；
 *    比如场景三，场景二 recyclerview 时显然很难不消费事件，如果 holder 被点击（比如聊天项），则应该被正常消费，
 *              如果点击 recyclerview 内的空白，recyclerview 也会默认消费，因为可能需要滑动、
 *              为了解决这种下层应该消费点击滑动事件，而上层容器应该获取点击并自动隐藏，HookActionUpRecyclerView 就是该场景的 DEMO
 *              需要把下层消费完之后的 ACTION_UP 返回 false 让上层有机会处理。 ContentContainerImpl 内的实现预留了这种可能，用于处理该复杂场景。
 *
 * 可参考自定义 [CusContentContainer] 或库提供的多种Container实现类
 * created by yummylau on 2020/06/06
 */
class ResetActivity : AppCompatActivity() {
    private var mHelper: PanelSwitchHelper? = null
    private var recyclerView: EmotionRecyclerView? = null

    // 本地表情数据
    private var emotionList = mutableListOf<EmotionBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_api_auto_reset_enable_layout)
        initView()
    }

    private val sendView: View
        get() = findViewById(R.id.send)

    private val editView: EditText
        get() = findViewById(R.id.edit_text)

    // 表情入口 控件
    private val emotionView: View
        get() = findViewById(R.id.emotion_btn)

    private fun initView() {

        sendView.setOnClickListener(View.OnClickListener {
            val content = editView.text.toString()
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this@ResetActivity, "当前没有输入", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            Log.e("ResetActivity 数据 -》", content)
            recyclerView?.saveEmotion()
            editView.text = null
        })
    }


    override fun onStart() {
        super.onStart()
        if (mHelper == null) {
            mHelper = PanelSwitchHelper.Builder(this) //可选
                .addKeyboardStateListener {
                    onKeyboardChange { visible, height ->
                        Log.e(TAG, "系统键盘是否可见 : $visible ,高度为：$height")
                    }
                }
                .addEditTextFocusChangeListener {
                    onFocusChange { _, hasFocus ->
                        Log.e(TAG, "输入框是否获得焦点 : $hasFocus")
                        if (hasFocus) {

                        }
                    }
                }
                .addViewClickListener {
                    onClickBefore {

                        Log.e(TAG, "点击了View : $it")
                    }
                }
                .addPanelChangeListener {
                    onKeyboard {
                        Log.e(TAG, "唤起系统输入法")
                        emotionView.isSelected = false
                    }
                    onNone {
                        Log.e(TAG, "隐藏所有面板")
                        emotionView.isSelected = false
                    }
                    onPanel {
                        Log.e(TAG, "唤起面板 : $it")
                        // 存储最近使用的数据
                        val latelyData = SpUtil.getInstace(recyclerView?.context).getString("latelyKey", "")
                        if (!TextUtils.isEmpty(latelyData)) {
                            val split = latelyData.split(",")
                            val mutableList = mutableListOf<EmotionBean>()
                            split.forEach {
                                mutableList.add(EmotionBean(unicode = it))
                            }

                            mutableList.reverse()
                            Log.e("EmotionRv size -> ", "${mutableList.size}")
                            emotionList.add(0,EmotionBean(childList = mutableList, type = EmotionBean.TYPE_HEAD))
                            recyclerView?.upData(emotionList)
                        }
                        if (it is PanelView) {
                            emotionView.isSelected = it.id == R.id.panel_emotion
                        }
                    }
                    onPanelSizeChange { panelView, _, _, _, width, height ->
                        if (panelView is PanelView) {
                            when (panelView.id) {
                                // 表情面板id
                                R.id.panel_emotion -> {
                                    recyclerView = findViewById(R.id.recyclerView)
//                                    val viewPagerSize = height - DisplayUtils.dip2px(this@ResetActivity, 30f)
                                    recyclerView?.buildEmotionViews(
                                        /*findViewById<View>(R.id.pageIndicatorView) as PageIndicatorView,*/
                                        editView,
                                        /*Emotions.getEmotions(), */
                                        emotionList.addAll(EmojiData().getData()),
                                        width, height
                                    )
                                }
                                // 音视频，图片的面板
//                                R.id.panel_addition -> {
//                                }
                            }
                        }
                    }
                }
                .logTrack(true) //output log
                .build()
        }

    }

    override fun onBackPressed() {
        if (mHelper != null && mHelper?.hookSystemBackByPanelSwitcher() == true) {
            return
        }
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "ResetActivity"
    }

}