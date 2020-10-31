package com.kuanquan.dragtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DragGridView dragGridView = findViewById(R.id.dragGridView);

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(i);
        }

        dragGridView.setAdapter(new MyAdapter(list));

    }


    private static class MyAdapter extends BaseAdapter implements DragGridView.DragGridBaseAdapter {
        private ArrayList<Integer> mList;
        public int mHidePosition = -1;

        private int[] colors = {android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light};

        public MyAdapter(ArrayList<Integer> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Integer getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
                holder = MyHolder.create(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }

            holder.mTextView.setText(getItem(position).toString());
            holder.mImageView.setImageResource(colors[getItem(position) % 3]);
            //隐藏被拖动的
            final View finalConvertView = convertView;
            if (position == mHidePosition) {
                convertView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finalConvertView.setVisibility(View.INVISIBLE);
                    }
                },0);

            } else {
                finalConvertView.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public void reorderItems(int oldPosition, int newPosition) {
            int temp = mList.get(oldPosition);
            if (oldPosition < newPosition) {
                for (int i = oldPosition; i < newPosition; i++) {
                    Collections.swap(mList, i, i + 1);
                }
            } else if (oldPosition > newPosition) {
                for (int i = oldPosition; i > newPosition; i--) {
                    Collections.swap(mList, i, i - 1);
                }
            }

            mList.set(newPosition, temp);
        }

        @Override
        public void setHideItem(int hidePosition) {
            mHidePosition = hidePosition;
            notifyDataSetChanged();
        }

        @Override
        public void deleteItem(int deletePosition) {
            if (null != mList && deletePosition < mList.size()) {
                mList.remove(deletePosition);
                notifyDataSetChanged();
            }
        }
    }

    private static class MyHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public MyHolder(ImageView imageView, TextView textView) {
            this.mImageView = imageView;
            this.mTextView = textView;
        }

        public static MyHolder create(View rootView) {
            ImageView image = rootView.findViewById(R.id.image);
            TextView text = rootView.findViewById(R.id.text);
            return new MyHolder(image, text);
        }
    }

}
