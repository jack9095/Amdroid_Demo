
package com.kuanquan.emotionkeyboard.utils;


import android.util.ArrayMap;

import com.kuanquan.emotionkeyboard.R;

/**
 * @author : zejian
 * @time : 2016年1月5日 上午11:32:33
 * @email : shinezejian@163.com
 * @description :表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 */
public class EmotionUtils {

	/**
	 * 表情类型标志符
	 */
	public static final int EMOTION_CLASSIC_TYPE=0x0001;//经典表情

	/**
	 * key-表情文字;
	 * value-表情图片资源
	 */
	public static ArrayMap<String, Integer> EMPTY_MAP;
	public static ArrayMap<String, Integer> EMOTION_CLASSIC_MAP;


	static {
		EMPTY_MAP = new ArrayMap<>();
		EMOTION_CLASSIC_MAP = new ArrayMap<>();

		EMOTION_CLASSIC_MAP.put("[呵呵]", R.mipmap.d_hehe);
		EMOTION_CLASSIC_MAP.put("[嘻嘻]", R.mipmap.d_xixi);
		EMOTION_CLASSIC_MAP.put("[哈哈]", R.mipmap.d_haha);
		EMOTION_CLASSIC_MAP.put("[爱你]", R.mipmap.d_aini);
		EMOTION_CLASSIC_MAP.put("[挖鼻屎]", R.mipmap.d_wabishi);
		EMOTION_CLASSIC_MAP.put("[吃惊]", R.mipmap.d_chijing);
		EMOTION_CLASSIC_MAP.put("[晕]", R.mipmap.d_yun);
		EMOTION_CLASSIC_MAP.put("[泪]", R.mipmap.d_lei);
		EMOTION_CLASSIC_MAP.put("[馋嘴]", R.mipmap.d_chanzui);
		EMOTION_CLASSIC_MAP.put("[抓狂]", R.mipmap.d_zhuakuang);
		EMOTION_CLASSIC_MAP.put("[哼]", R.mipmap.d_heng);
		EMOTION_CLASSIC_MAP.put("[可爱]", R.mipmap.d_keai);
		EMOTION_CLASSIC_MAP.put("[怒]", R.mipmap.d_nu);
		EMOTION_CLASSIC_MAP.put("[汗]", R.mipmap.d_han);
		EMOTION_CLASSIC_MAP.put("[害羞]", R.mipmap.d_haixiu);
		EMOTION_CLASSIC_MAP.put("[睡觉]", R.mipmap.d_shuijiao);
		EMOTION_CLASSIC_MAP.put("[钱]", R.mipmap.d_qian);
		EMOTION_CLASSIC_MAP.put("[偷笑]", R.mipmap.d_touxiao);
		EMOTION_CLASSIC_MAP.put("[笑cry]", R.mipmap.d_xiaoku);
		EMOTION_CLASSIC_MAP.put("[doge]", R.mipmap.d_doge);
		EMOTION_CLASSIC_MAP.put("[喵喵]", R.mipmap.d_miao);
		EMOTION_CLASSIC_MAP.put("[酷]", R.mipmap.d_ku);
		EMOTION_CLASSIC_MAP.put("[衰]", R.mipmap.d_shuai);
		EMOTION_CLASSIC_MAP.put("[闭嘴]", R.mipmap.d_bizui);
		EMOTION_CLASSIC_MAP.put("[鄙视]", R.mipmap.d_bishi);
		EMOTION_CLASSIC_MAP.put("[花心]", R.mipmap.d_huaxin);
		EMOTION_CLASSIC_MAP.put("[鼓掌]", R.mipmap.d_guzhang);
		EMOTION_CLASSIC_MAP.put("[悲伤]", R.mipmap.d_beishang);
		EMOTION_CLASSIC_MAP.put("[思考]", R.mipmap.d_sikao);
		EMOTION_CLASSIC_MAP.put("[生病]", R.mipmap.d_shengbing);
		EMOTION_CLASSIC_MAP.put("[亲亲]", R.mipmap.d_qinqin);
		EMOTION_CLASSIC_MAP.put("[怒骂]", R.mipmap.d_numa);
		EMOTION_CLASSIC_MAP.put("[太开心]", R.mipmap.d_taikaixin);
		EMOTION_CLASSIC_MAP.put("[懒得理你]", R.mipmap.d_landelini);
		EMOTION_CLASSIC_MAP.put("[右哼哼]", R.mipmap.d_youhengheng);
		EMOTION_CLASSIC_MAP.put("[左哼哼]", R.mipmap.d_zuohengheng);
		EMOTION_CLASSIC_MAP.put("[嘘]", R.mipmap.d_xu);
		EMOTION_CLASSIC_MAP.put("[委屈]", R.mipmap.d_weiqu);
		EMOTION_CLASSIC_MAP.put("[吐]", R.mipmap.d_tu);
		EMOTION_CLASSIC_MAP.put("[可怜]", R.mipmap.d_kelian);
		EMOTION_CLASSIC_MAP.put("[打哈气]", R.mipmap.d_dahaqi);
		EMOTION_CLASSIC_MAP.put("[挤眼]", R.mipmap.d_jiyan);
		EMOTION_CLASSIC_MAP.put("[失望]", R.mipmap.d_shiwang);
		EMOTION_CLASSIC_MAP.put("[顶]", R.mipmap.d_ding);
		EMOTION_CLASSIC_MAP.put("[疑问]", R.mipmap.d_yiwen);
		EMOTION_CLASSIC_MAP.put("[困]", R.mipmap.d_kun);
		EMOTION_CLASSIC_MAP.put("[感冒]", R.mipmap.d_ganmao);
		EMOTION_CLASSIC_MAP.put("[拜拜]", R.mipmap.d_baibai);
		EMOTION_CLASSIC_MAP.put("[黑线]", R.mipmap.d_heixian);
		EMOTION_CLASSIC_MAP.put("[阴险]", R.mipmap.d_yinxian);
		EMOTION_CLASSIC_MAP.put("[打脸]", R.mipmap.d_dalian);
		EMOTION_CLASSIC_MAP.put("[傻眼]", R.mipmap.d_shayan);
		EMOTION_CLASSIC_MAP.put("[猪头]", R.mipmap.d_zhutou);
		EMOTION_CLASSIC_MAP.put("[熊猫]", R.mipmap.d_xiongmao);
		EMOTION_CLASSIC_MAP.put("[兔子]", R.mipmap.d_tuzi);
	}

	/**
	 * 根据名称获取当前表情图标R值
	 * @param EmotionType 表情类型标志符
	 * @param imgName 名称
	 * @return
	 */
	public static int getImgByName(int EmotionType,String imgName) {
		Integer integer=null;
		switch (EmotionType){
			case EMOTION_CLASSIC_TYPE:
				integer = EMOTION_CLASSIC_MAP.get(imgName);
				break;
			default:
				LogUtils.e("the emojiMap is null!! Handle Yourself ");
				break;
		}
		return integer == null ? -1 : integer;
	}

	/**
	 * 根据类型获取表情数据
	 * @param EmotionType
	 * @return
	 */
	public static ArrayMap<String, Integer> getEmojiMap(int EmotionType){
		ArrayMap EmojiMap=null;
		switch (EmotionType){
			case EMOTION_CLASSIC_TYPE:
				EmojiMap=EMOTION_CLASSIC_MAP;
				break;
			default:
				EmojiMap=EMPTY_MAP;
				break;
		}
		return EmojiMap;
	}
}
