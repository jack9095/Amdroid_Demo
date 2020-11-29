package com.kuanquan.datastore;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.liyi.highlight.HighlightTextView;

public class MainActivity extends AppCompatActivity {

    private HighlightTextView highlighttextView;
    private HighlightTextView tv1, tv2, tv3, tv4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);

        initView();

//        Drawable d = getResources().getDrawable(R.mipmap.ic_launcher);
//        d.setBounds(0, 0,50, 50);
//
//        highlighttextView.addContent("西边的太阳渐渐落去，一缕阳光仍然没力的穿过那厚厚云层，穿过那棵高高的杨桃树，照射到坐在门口老人身上，老人脸上显得是那样的无奈。此刻，他双手托着下巴，眼里流露着孤独无助的茫然神色。在他的心目中，黄昏岁月还能坚持多长呢？\n" +
//                "\n" +
//                "老人住一目破旧屋，五十岁时，他花掉了多年的积蓄，与一位女人结婚生了一个男孩。可是，好景不长，当孩子三岁时，年轻老婆耐不住清贫，被人贩拐走了。老人求天不应叫地不灵，面对这心肠寸断的日子，老人只好忍辱受屈，父子俩相依为命。六十一岁时，政府为他办了低保三百元，生活过得依然苦涩，但是，他很感谢共产党，共产党好，使自己能够有活下去的勇气。在这十多年间，他勤俭节约，艰难度日，终于把孩子拉扯大。如今，老人七十古来稀，变成了一位弯腰驼背的人，分田到户那一亩多地，从此，再也没有能力耕作了。从早到晚，一人孤独坐在家门口，望日而出望日而落。从那凹陷眼睛中，人们隐约地看到，老人家心中隐藏着一种对生活的渴望。")
//                // 设置文字颜色（12：文字颜色转变的开始位置，14：文字颜色转变的结束位置）
//                // 同时提供方法：.addFontColorStyleByKey(@ColorInt int color, String key)
//                .addFontColorStyle(Color.YELLOW, 12, 14)
//                // 设置文字背景色
//                // 同时提供方法：.addBgColorStyleByKey(@ColorInt int color, String key)
//                .addBgColorStyle(Color.BLUE, 0, 10)
//                        // 直接搜索“文字内容”中的所有“关键字”，对所有“关键字”添加超链接
//                        // 同时提供方法：.addURLStyle(String url, int start, int end)
//                .addURLStyleByKey("http://www.baidu.com", "关键字")
//        // 直接搜索“文字内容”中的所有“关键字”，对所有“关键字”添加点击事件
//        // 同时提供方法：
////        .addClickStyle(HighlightTextView.OnHighlightClickListener listener, boolean isNeedUnderLine,int start,int end)
//        .addClickStyleByKey(new HighlightTextView.OnHighlightClickListener() {
//            @Override
//            public void onTextClick(int position, View v) {
//                Toast.makeText(MainActivity.this, "关键字", Toast.LENGTH_SHORT).show();
//            }
//        }, true, "关键字")// true：是否添加下划线
//                // 将文字替换为图片（ImageSpan.ALIGN_BOTTOM：图片与文字底部对齐）
//                // 同时提供方法：.addImageStyle(ImageSpan span, int start, int end)
//                // drawable也可以是span、bitmap、resouceId
//                .addImageStyleByKey(d, ImageSpan.ALIGN_BOTTOM, "关键字")
//                // 设置字体样式
//                // .addTypefaceStyle(int style, int start, int end)
//                // .addTypefaceStyleByKey(int style, String key)
//                // 加删除线
//                // .addStrikethroughStyle(int start, int end)
//                // .addStrikethroughStyleByKey(String key)
//                // 执行
//                .build();
    }

    private void initView() {
//        tv1.addBgColorStyle(Color.BLUE, 0, 10)
//                .addFontColorStyle(Color.YELLOW, 12, 14)
//                .build();
        tv2.addContent("\n\n2、路飞上上去就是一拳，一招猿王枪，多弗朗明哥差点招架不住，多弗朗明哥立马觉醒果实能力：'十六发神圣凶弹.神诛杀!'路飞勃然变色，" +
                "直接开大，‘大猿王枪’，多弗朗明哥败！")
                .addClickStyleByKey(new HighlightTextView.OnHighlightClickListener() {
                    @Override
                    public void onTextClick(int position, View v) {
                        Toast.makeText(MainActivity.this, "我是多弗朗明哥", Toast.LENGTH_SHORT).show();
                    }
                }, true, "多弗朗明哥")
                .build();


        tv3.addContent("\n\n3、路飞上上去就是一拳，一招猿王枪，多弗朗明哥差点招架不住，多弗朗明哥立马觉醒果实能力：'十六发神圣凶弹.神诛杀!'路飞勃然变色，" +
                "直接开大，‘大猿王枪’，多弗朗明哥败！")
                .addURLStyleByKey("http://www.baidu.com", "猿王枪")
                .build();

        Drawable d = getResources().getDrawable(R.mipmap.ic_launcher);
        d.setBounds(0, 0,50, 50);
        tv4.addContent("\n\n4、路飞上上去就是一拳，一招猿王枪，多弗朗明哥差点招架不住，多弗朗明哥立马觉醒果实能力：'十六发神圣凶弹.神诛杀!'路飞勃然变色，" +
                "直接开大，‘大猿王枪’，多弗朗明哥败！")
                .addImageStyleByKey(d, ImageSpan.ALIGN_BOTTOM, "多弗朗明哥")
                .addClickStyleByKey(new HighlightTextView.OnHighlightClickListener() {
                    @Override
                    public void onTextClick(int position, View v) {
                        Toast.makeText(MainActivity.this, "猿王枪", Toast.LENGTH_SHORT).show();
                    }
                }, true, "猿王枪")
                .build();
    }
}
