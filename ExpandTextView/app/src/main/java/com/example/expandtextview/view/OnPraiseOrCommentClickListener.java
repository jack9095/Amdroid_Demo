package com.example.expandtextview.view;

/**
 * @author zhaojin
 * @date 2018/8/10
 */
public interface OnPraiseOrCommentClickListener {
    void onPraiseClick(int position);

    void onCommentClick(int position);

    void onClickFrendCircleTopBg();

    void onDeleteItem(String id, int position);
}
