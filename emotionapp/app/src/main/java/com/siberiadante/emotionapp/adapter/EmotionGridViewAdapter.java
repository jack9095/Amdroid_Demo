package com.siberiadante.emotionapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.siberiadante.emotionapp.R;
import com.siberiadante.emotionapp.utils.EmotionUtils;

import java.util.List;

/**
 * @Created by SiberiaDante
 * @Describe：
 * @Time: 2017/6/26
 * @Email: 994537867@qq.com
 * @GitHub: https://github.com/SiberiaDante
 * @博客园： http://www.cnblogs.com/shen-hua/
 */
public class EmotionGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<String> emotionNames;
	private int itemWidth;
    private int emotion_map_type;
	
	public EmotionGridViewAdapter(Context context, List<String> emotionNames, int itemWidth, int emotion_map_type) {
		this.context = context;
		this.emotionNames = emotionNames;
		this.itemWidth = itemWidth;
		this.emotion_map_type=emotion_map_type;
	}
	
	@Override
	public int getCount() {
		// +1 最后一个为删除按钮
		return emotionNames.size() + 1;
	}

	@Override
	public String getItem(int position) {
		return emotionNames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv_emotion = new ImageView(context);
		// 设置内边距
		iv_emotion.setPadding(itemWidth/8, itemWidth/8, itemWidth/8, itemWidth/8);
		LayoutParams params = new LayoutParams(itemWidth, itemWidth);
		iv_emotion.setLayoutParams(params);
		
		//判断是否为最后一个item
		if(position == getCount() - 1) {
			iv_emotion.setImageResource(R.drawable.compose_emotion_delete);
		} else {
			String emotionName = emotionNames.get(position);
			iv_emotion.setImageResource(EmotionUtils.getImgByName(emotion_map_type,emotionName));
		}
		
		return iv_emotion;
	}

}
