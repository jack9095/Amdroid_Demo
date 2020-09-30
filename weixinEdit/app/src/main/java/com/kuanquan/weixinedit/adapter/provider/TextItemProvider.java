package com.kuanquan.weixinedit.adapter.provider;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kuanquan.weixinedit.R;
import com.kuanquan.weixinedit.model.EventModel;
import com.kuanquan.weixinedit.model.ProviderMultiEntity;
import com.kuanquan.weixinedit.util.ToastUtils;
import com.kuanquan.weixinedit.widget.ExpandTextView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

public class TextItemProvider extends BaseItemProvider<ProviderMultiEntity> {

    private ExpandTextView tvContent;

    public TextItemProvider() {
        addChildClickViewIds(R.id.iv_edit);
    }

    // item 类型
    @Override
    public int getItemViewType() {
        return ProviderMultiEntity.TYPE_TEXT;
    }

    // 返回 item 布局 layout
    @Override
    public int getLayoutId() {
        return R.layout.item_text;
    }

    /*
     * （可选）
     * 重写返回自己的 ViewHolder。
     * 默认返回 BaseViewHolder()
     */
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    // 设置 item 数据
    @Override
    public void convert(BaseViewHolder helper, ProviderMultiEntity data) {
        tvContent = helper.getView(R.id.tv_content);
        //用户名
        if (data.name != null && !data.name.equals("")) {
            helper.setText(R.id.tv_name, data.name);
        }
        //评论时间
        if (data.createon != null && !data.createon.equals("")) {
            helper.setText(R.id.tv_time, data.createon);
        }

        //评论内容
        if (TextUtils.isEmpty(data.content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(data.content);
        }
    //        if (helper.getAdapterPosition() % 2 == 0) {
    //            helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
    //        } else {
    //            helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
    //        }
    }

    @Override
    public void onClick(BaseViewHolder helper, View view, ProviderMultiEntity data, int position) {
//        Tips.show("Click: " + position);
    }

    @Override
    public boolean onLongClick(BaseViewHolder helper, View view, ProviderMultiEntity data, int position) {
//        Tips.show("Long Click: " + position);
        return true;
    }

    /**
     * 子控件点击
     *
     * @param helper
     * @param view
     * @param data
     * @param position
     */
    @Override
    public void onChildClick(@NotNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        if (view.getId() == R.id.iv_edit) {
//            ToastUtils.ToastShort(view.getContext(), "点击评论");
            EventBus.getDefault().post(new EventModel(view, position));
        }
    }
}
