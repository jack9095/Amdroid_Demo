package cn.bluemobi.dylan.circlemenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CircleMenu";

//    private ImageView iv18;
//    private ImageView iv17;
    private ImageView iv16;
    private ImageView iv15;
    private ImageView iv14;
    private ImageView iv13;
    private ImageView iv12;
    private ImageView iv11;

    private List<ImageView> imageViews2 = new ArrayList<>();
    private final int radius2 = 300;

    private void assignViews() {
//        iv18 = findViewById(R.id.iv18);
//        imageViews2.add(iv18);
//        iv17 = findViewById(R.id.iv17);
//        imageViews2.add(iv17);
        iv16 = findViewById(R.id.iv16);
        imageViews2.add(iv16);
        iv15 = findViewById(R.id.iv15);
        imageViews2.add(iv15);
        iv14 = findViewById(R.id.iv14);
        imageViews2.add(iv14);
        iv13 = findViewById(R.id.iv13);
        imageViews2.add(iv13);
        iv12 = findViewById(R.id.iv12);
        imageViews2.add(iv12);
        iv11 = findViewById(R.id.iv11);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
    }

    public void onClick(View v) {
        if (v.getId() == iv11.getId()) {
            Boolean isShowing = (Boolean) iv11.getTag();
            if (null == isShowing || isShowing == false) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv11, "rotation", 0, 45);
//                objectAnimator.setDuration(500);
                objectAnimator.start();

                ObjectAnimator alpha = ObjectAnimator.ofFloat(iv11, "alpha", 1f, 0.0f);
                alpha.setDuration(2100);
                alpha.start();
                iv11.setTag(true);
                showCircleMenu();
            } else {
                iv11.setTag(false);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv11, "rotation", 45, 0);
                objectAnimator.setDuration(500);
                objectAnimator.start();
                closeCircleMenu();
            }
        } else {
            Toast.makeText(this, "点击了第" + imageViews2.indexOf(v) + "个", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 显示圆形菜单
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showCircleMenu() {
        for (int i = 0; i < imageViews2.size(); i++) {
            PointF point = new PointF();
            int avgAngle = (360 / (imageViews2.size() - 1));
//            int avgAngle = (360 / (imageViews2.size()));
            int angle = avgAngle * i;
            Log.e(TAG, "angle=" + angle);
            point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius2;
            point.y = (float) Math.sin(angle * (Math.PI / 180)) * radius2;
            Log.e(TAG, point.toString());
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageViews2.get(i), "translationX", 0, point.x);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageViews2.get(i), "translationY", 0, point.y);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);
            animatorSet.play(objectAnimatorX).with(objectAnimatorY);
            animatorSet.start();
        }
    }

    /**
     * 关闭圆形菜单
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void closeCircleMenu() {
        for (int i = 0; i < imageViews2.size(); i++) {
            PointF point = new PointF();
            int avgAngle = (360 / (imageViews2.size() - 1));
//            int avgAngle = (360 / (imageViews2.size()));
            int angle = avgAngle * i;
            Log.d(TAG, "angle=" + angle);
            point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius2;
            point.y = (float) Math.sin(angle * (Math.PI / 180)) * radius2;

            Log.d(TAG, point.toString());
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageViews2.get(i), "translationX", point.x, 0);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageViews2.get(i), "translationY", point.y, 0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);
            animatorSet.play(objectAnimatorX).with(objectAnimatorY);
            animatorSet.start();
        }
    }

}
