package com.example.expandtextview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.expandtextview.activity.PlayVideoActivity;
import com.example.expandtextview.activity.WatchLiveActivity;
import com.example.expandtextview.adapter.CircleAdapter;
import com.example.expandtextview.bean.CircleBean;
import com.example.expandtextview.bean.CommentListBean;
import com.example.expandtextview.bean.LikeBean;
import com.example.expandtextview.bean.LikeListBean;
import com.example.expandtextview.bean.WeatherEvent;
import com.example.expandtextview.util.AssetsUtil;
import com.example.expandtextview.util.CommonUtils;
import com.example.expandtextview.util.Constants;
import com.example.expandtextview.util.GlideSimpleTarget;
import com.example.expandtextview.util.KeyboardUtil;
import com.example.expandtextview.util.RxBus;
import com.example.expandtextview.util.StorageUtil;
import com.example.expandtextview.util.ToastUtils;
import com.example.expandtextview.util.Utils;
import com.example.expandtextview.view.LikePopupWindow;
import com.example.expandtextview.view.OnPraiseOrCommentClickListener;
import com.example.expandtextview.view.ScrollSpeedLinearLayoutManger;
import com.example.expandtextview.view.SpaceDecoration;
import com.github.ielse.imagewatcher.ImageWatcher;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @时间: 2019/7/22 10:53
 * @描述: 仿微信朋友圈实现
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CircleAdapter.Click, ImageWatcher.OnPictureLongPressListener, ImageWatcher.Loader {
    private RecyclerView recyclerView;
    private CircleAdapter circleAdapter;
    private String content;
    private LikePopupWindow likePopupWindow;
    private EditText etComment;
    private LinearLayout llComment;
    private TextView tvSend;
    private LinearLayout llScroll;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCommentItemOffset;
    private int commentPosition;
    protected final String TAG = this.getClass().getSimpleName();
    CompositeDisposable compositeDisposable;
    private ScrollSpeedLinearLayoutManger layoutManger;
    private List<CircleBean.DataBean> dataBeans;
    ImageWatcher imageWatcher;
    private int isLike;
    private int comPosition;
    private String to_user_id;
    private String to_user_name;
    private String circle_id;
    private String userId;
    private String userName;
    private RxPermissions rxPermissions;
    private static MyHandler myHandler;
    private static String dstPath;
    private ImageView ivBg;
    private ImageView ivUser;
    private TextView tvName;
    private ConstraintLayout clMessage;
    private ImageView ivMessage;
    private TextView tvMessage;
    private List<LikeListBean> likeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        initAdapter();
        setListener();
        initRxBus();
    }

    private void initRxBus() {
        compositeDisposable = new CompositeDisposable();
        RxBus.getInstance().toObservable(WeatherEvent.class)
                .subscribe(new Observer<WeatherEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(WeatherEvent weatherEvent) {
                        Log.e("weather", weatherEvent.getTemperature() + "-**-" + weatherEvent.getCityName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setListener() {
        tvSend.setOnClickListener(this);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                content = etComment.getText().toString();
                if (etComment.getText().length() == 500) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.the_content_of_the_comment_cannot_exceed_500_words), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        recyclerView.setOnTouchListener((view, motionEvent) -> {
            if (llComment.getVisibility() == View.VISIBLE) {
                updateEditTextBodyVisible(View.GONE);
                return true;
            }
            return false;
        });
        circleAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            userId = Objects.requireNonNull(circleAdapter.getItem(position)).getId();
            userName = Objects.requireNonNull(circleAdapter.getItem(position)).getUser_name();
            isLike = Objects.requireNonNull(circleAdapter.getItem(position)).getIs_like();
            likeBean = new ArrayList<>(0);
            likeBean  = circleAdapter.getData().get(position).getLike_list();
            comPosition = position;
            switch (view.getId()) {
                case R.id.iv_edit:
                    //评论弹框
                    showLikePopupWindow(view, position);
                    break;
                case R.id.tv_delete:
                    //删除朋友圈
                    deleteCircleDialog();
                    break;
                    //点击视频按钮跳转到视频播放界面
                case R.id.video_view:
                    Intent intent = new Intent(this, PlayVideoActivity.class);
                    intent.putExtra("url", Objects.requireNonNull(circleAdapter.getItem(position)).getVideo());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }

    private void showLikePopupWindow(View view, int position) {
        //item 底部y坐标
        final int mBottomY = getCoordinateY(view) + view.getHeight();
        if (likePopupWindow == null) {
            likePopupWindow = new LikePopupWindow(this, isLike);
        }
        likePopupWindow.setOnPraiseOrCommentClickListener(new OnPraiseOrCommentClickListener() {
            @Override
            public void onPraiseClick(int position) {
                //调用点赞接口
                getLikeData();
                likePopupWindow.dismiss();
            }

            @Override
            public void onCommentClick(int position) {
                llComment.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                etComment.setHint("说点什么");
                to_user_id = null;
                KeyboardUtil.showSoftInput(MainActivity.this);
                likePopupWindow.dismiss();
                etComment.setText("");
                view.postDelayed(() -> {
                    int y = getCoordinateY(llComment) - 20;
                    //评论时滑动到对应item底部和输入框顶部对齐
                    recyclerView.smoothScrollBy(0, mBottomY - y);
                }, 300);
            }

            @Override
            public void onClickFrendCircleTopBg() {

            }

            @Override
            public void onDeleteItem(String id, int position) {

            }

        }).setTextView(isLike).setCurrentPosition(position);
        if (likePopupWindow.isShowing()) {
            likePopupWindow.dismiss();
        } else {
            likePopupWindow.showPopupWindow(view);
        }
    }

    /**
     * 请求点赞接口，这里写的模拟数据
     */
    private void getLikeData() {
        if (likeBean != null ) {
            List<CircleBean.DataBean> list = circleAdapter.getData();
            if (isLike == 1) {
                Iterator it = list.get(comPosition).getLike_list().iterator();
                List<LikeListBean> likeListBeans = new ArrayList<>();
                while (it.hasNext()) {
                    LikeListBean info = (LikeListBean) it.next();
                    if (info.getUser_id().equals(String.valueOf(userId))) {
                        it.remove();
                    } else {
                        likeListBeans.add(info);
                    }
                }
                LikeListBean likeListBean = new LikeListBean();
                likeListBean.setUser_id(userId+"");
                likeListBean.setUser_name(userName);
                list.get(comPosition).setIs_like(0);
                list.get(comPosition).setLike_list(likeListBeans);
                ToastUtils.ToastShort("取消点赞");
            } else {
                LikeListBean likeListBean = new LikeListBean();
                likeListBean.setUser_id(userId + "");
                likeListBean.setUser_name(userName);
                list.get(comPosition).getLike_list().add(likeListBean);
                list.get(comPosition).setIs_like(1);
                ToastUtils.ToastShort("点赞成功");
            }
            circleAdapter.notifyDataSetChanged();
        }
    }


    private void deleteCircleDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("提示");
        alert.setMessage("你确定要删除吗?");
        alert.setNegativeButton("取消", null);
        alert.setPositiveButton("确定", (dialog, which) -> {
            //调接口删除,这里写死
            dialog.dismiss();
            startActivity(new Intent(this, WatchLiveActivity.class));
        });
        alert.show();
    }


    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = llScroll.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            llScroll.getWindowVisibleDisplayFrame(r);
            int statusBarH = Utils.getStatusBarHeight();//状态栏高度
            int screenH = llScroll.getRootView().getHeight();
            if (r.top != statusBarH) {
                //r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                r.top = statusBarH;
            }
            int keyboardH = screenH - (r.bottom - r.top);
            Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

            if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                return;
            }
            currentKeyboardH = keyboardH;
            editTextBodyHeight = llComment.getHeight();
            if (keyboardH < 150) {//说明是隐藏键盘的情况
                MainActivity.this.updateEditTextBodyVisible(View.GONE);
                return;
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        llComment = findViewById(R.id.ll_comment);
        etComment = findViewById(R.id.et_comment);
        tvSend = findViewById(R.id.tv_send_comment);
        llScroll = findViewById(R.id.ll_scroll);
        imageWatcher = findViewById(R.id.imageWatcher);
        //初始化仿微信图片滑动加载器
        imageWatcher.setTranslucentStatus(Utils.calcStatusBarHeight(this));
        imageWatcher.setErrorImageRes(R.mipmap.error_picture);
        imageWatcher.setOnPictureLongPressListener(this);
        imageWatcher.setLoader(this);
        getPermissions();
        myHandler = new MyHandler(this);
    }

    /**
     * 初始化数据
     *
     * @param
     */
    private void initData() {
        dataBeans = new ArrayList<>();
        dataBeans = AssetsUtil.getStates(this);
    }

    /**
     * 设置adapter
     */
    private void initAdapter() {
        View headCircle = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_circle_head, null);
        initHead(headCircle);
        circleAdapter = new CircleAdapter(dataBeans, imageWatcher, llComment, etComment, this);
        layoutManger = new ScrollSpeedLinearLayoutManger(this);
        recyclerView.setLayoutManager(layoutManger);
        layoutManger.setSpeedSlow();
        circleAdapter.addHeaderView(headCircle);
        recyclerView.addItemDecoration(new SpaceDecoration(this));
        recyclerView.setAdapter(circleAdapter);
    }

    /**
     * 初始化消息提醒布局
     * @param headCircle
     */
    private void   initHead(View headCircle) {
        ivBg = headCircle.findViewById(R.id.iv_bg);
        ivUser = headCircle.findViewById(R.id.iv_user);
        tvName = headCircle.findViewById(R.id.tv_name);
        clMessage = headCircle.findViewById(R.id.cl_message);
        ivMessage = headCircle.findViewById(R.id.message_avatar);
        tvMessage = headCircle.findViewById(R.id.message_detail);
        ivBg.setOnClickListener(this);
        ivUser.setOnClickListener(this);
        //这里是调用接口数据，本例子写的假数据，可以自己根据需求来写
        clMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("10条新信息");
    }

    public void updateEditTextBodyVisible(int visibility) {
        llComment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            llComment.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(etComment.getContext(), etComment);

        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(etComment.getContext(), etComment);
        }
    }

    /**
     * 获取控件左上顶点Y坐标
     *
     * @param view
     * @return
     */
    private int getCoordinateY(View view) {
        int[] coordinate = new int[2];
        view.getLocationOnScreen(coordinate);
        return coordinate[1];
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_send_comment) {
            if (TextUtils.isEmpty(etComment.getText().toString())) {
                Toast.makeText(MainActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                return;
            }
            //请求接口，在成功回调方法拼接评论信息,这里写死
            getComment();
            setViewTreeObserver();
        }
    }

    private void getComment() {
        List<CircleBean.DataBean> list = circleAdapter.getData();
        CommentListBean commentListBean = new CommentListBean();
        //userId为当前用户id,这里只是一个例子所以没有登录注册
        commentListBean.setUser_id(userId);
        //userName为当前用户名称
        commentListBean.setUser_name(userName);
        commentListBean.setTo_user_name(TextUtils.isEmpty(to_user_name) ? "" : to_user_name);
        commentListBean.setTo_user_id(TextUtils.isEmpty(to_user_id) ? "" : to_user_id);
        commentListBean.setCircle_id(circle_id);
        commentListBean.setContent(content);
        if (TextUtils.isEmpty(to_user_id)) {
            ToastUtils.ToastShort("评论成功");
            list.get(comPosition).getComments_list().add(commentListBean);
        } else {
            ToastUtils.ToastShort("回复成功");
            list.get(commentPosition).getComments_list().add(commentListBean);
        }
        circleAdapter.notifyDataSetChanged();
        KeyboardUtil.hideSoftInput(MainActivity.this);
        llComment.setVisibility(View.GONE);
        etComment.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        RxBus.rxBusUnbund(compositeDisposable);
        myHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void Commend(int position, CommentListBean bean) {
        circle_id = bean.getCircle_id();
        commentPosition = position;
        to_user_name = bean.getUser_name();
        to_user_id = bean.getUser_id();
    }

    @Override
    public void load(Context context, Uri uri, ImageWatcher.LoadCallback loadCallback) {
        Glide.with(context).asBitmap().load(uri.toString()).into(new GlideSimpleTarget(loadCallback));
    }

    @Override
    public void onPictureLongPress(ImageView v, Uri uri, int pos) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("保存图片");
        alert.setMessage("你确定要保存图片吗?");
        alert.setNegativeButton("取消", null);
        alert.setPositiveButton("确定", (dialog, which) -> {
            rxPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            if (uri != null) {// Always true pre-M
                                savePhoto(uri);
                            }
                        } else {
                            ToastUtils.ToastShort("缺少必要权限,请授予权限");
                        }
                    });
            dialog.dismiss();

        });
        alert.show();
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;
        private Bitmap bitmap;

        private MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 1) {
                    try {
                        bitmap = (Bitmap) msg.obj;
                        if (Utils.saveBitmap(bitmap, dstPath, false)) {
                            try {
                                ContentValues values = new ContentValues(2);
                                values.put(MediaStore.Images.Media.MIME_TYPE, Constants.MIME_JPEG);
                                values.put(MediaStore.Images.Media.DATA, dstPath);
                                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                ToastUtils.ToastShort(activity, getResources().getString(R.string.picture_save_to));
                            } catch (Exception e) {
                                ToastUtils.ToastShort(activity, getResources().getString(R.string.picture_save_fail));
                            }
                        } else {
                            ToastUtils.ToastShort(activity, getResources().getString(R.string.picture_save_fail));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.ToastShort(activity, getResources().getString(R.string.picture_save_fail));
                    }
                }
            }
        }
    }

    private void getPermissions() {
        rxPermissions = new RxPermissions(this);
    }

    /**
     * 长按保存图片
     * @param uri 图片url地址
     */
    private void savePhoto(Uri uri) {
        Glide.with(MainActivity.this).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                String picPath = StorageUtil.getSystemImagePath();
                dstPath = picPath + (System.currentTimeMillis() / 1000) + ".jpeg";
                Message message = Message.obtain();
                message.what = 1;
                message.obj = resource;
                myHandler.sendMessage(message);
                return false;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }
        }).submit();
    }
}
