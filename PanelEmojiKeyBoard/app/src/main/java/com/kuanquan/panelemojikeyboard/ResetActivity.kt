package com.kuanquan.panelemojikeyboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
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
        emotionList.addAll(EmojiData().getData())
    }

    private val sendView: View
        get() = findViewById(R.id.send)

    private val add_btn: View
        get() = findViewById(R.id.add_btn)

    private val btn: View
        get() = findViewById(R.id.btn)

    private val editView: EditText
        get() = findViewById(R.id.edit_text)

    // 表情入口 控件
    private val emotionView: View
        get() = findViewById(R.id.emotion_btn)

    /**获取EditText光标所在的位置 */
    private fun getEditTextCursorIndex(mEditText: EditText): Int {
        return mEditText.selectionStart
    }

    /**向EditText指定光标位置插入字符串 */
    private fun insertText(mEditText: EditText, mText: String) {
        mEditText.text.insert(getEditTextCursorIndex(mEditText), mText)
    }

    /**
     * 删除表情和文字 -- 》 然而不好使
     */
    private fun deleteEditTextEmojiAndString(mEditText: EditText){
        val selectionStart: Int = mEditText.getSelectionStart() // 获取光标的位置

        if (selectionStart > 0) {
            val body: String = mEditText.getText().toString()
            if (!TextUtils.isEmpty(body)) {
                val tempStr = body.substring(0, selectionStart)
                val i = tempStr.lastIndexOf("[") // 获取最后一个表情的位置
                if (i != -1) {
                    val cs = tempStr.subSequence(i, selectionStart)
                    if (cs == "[fac") { // 判断是不是一个表情
                        mEditText.getEditableText().delete(i, selectionStart)
                        return
                    }
                }
                mEditText.getEditableText().delete(tempStr.length - 1, selectionStart)
            }
        }
    }

    /**
     * 删除表情和文字 仿照系统键盘删除按钮 --->  完美实现
     */
    private fun deleteEditTextEmojiAndStringSystem(mEditText: EditText){
        val keyCode = KeyEvent.KEYCODE_DEL
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, keyCode)
        mEditText.onKeyDown(keyCode, keyEventDown)
        mEditText.onKeyUp(keyCode, keyEventUp)
    }

    private fun initView() {

        btn.setOnClickListener(View.OnClickListener {
            Log.e(TAG,"删除数据")
            deleteEditTextEmojiAndStringSystem(editView)
        })

        sendView.setOnClickListener(View.OnClickListener {
            val content = editView.text.toString()
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this@ResetActivity, "当前没有输入", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            Log.e("ResetActivity 点击发送数据 -》", content)
//            recyclerView?.saveEmotion()
            editView.text = null
        })

        add_btn.setOnClickListener {
            Log.e(TAG,"点击加号")
            SpUtil.getInstace(recyclerView?.context).clearAll()
        }
    }

    var isEmoji = false

    @SuppressLint("LongLogTag")
    override fun onStart() {
        super.onStart()
        if (mHelper == null) {
            mHelper = PanelSwitchHelper.Builder(this) //可选
                .addPanelChangeListener {
                    onKeyboard {
                        emotionView.isSelected = false
                        emotionList.addAll(EmojiData().getData())
                    }
                    onNone {
                        emotionView.isSelected = false
                        emotionList.addAll(EmojiData().getData1())
                    }
                    onPanel {
                        // 获取最近使用的表情数据
                        val latelyData = SpUtil.getInstace(recyclerView?.context).getString("latelyKey", "")
                        Log.e(TAG, "最近使用表情数据 : $latelyData")
                        if (!TextUtils.isEmpty(latelyData)) {
                            val split = latelyData.split(",")
                            val mutableList = mutableListOf<EmotionBean>()
                            split.forEach {
                                mutableList.add(EmotionBean(unicode = it))
                            }

//                            mutableList.reverse()
                            Log.e("$TAG latelysize -> ", "${mutableList.size}")
                            emotionList.clear()


                            emotionList.add(EmotionBean(childList = mutableList, type = EmotionBean.TYPE_HEAD)
                            )
                            emotionList.addAll(EmojiData().getData())

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
                                    recyclerView?.buildEmotionViews(
                                        editView,
                                        emotionList,
                                        width, height
                                    )
                                }
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