package com.kuanquan.lyrics_demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.*;
import android.widget.*;
import com.kuanquan.lyrics.formats.LyricsFileReader;
import com.kuanquan.lyrics.model.LyricsLineInfo;
import com.kuanquan.lyrics.model.LyricsInfo;
import com.kuanquan.lyrics.utils.FileUtils;
import com.kuanquan.lyrics.utils.LyricsIOUtils;
import java.io.*;
import java.util.TreeMap;

public class MainActivity extends BaseActivity {
    private TextView mLrcTextView;    // 歌词内容

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 进入双行歌词视图测试
        findViewById(R.id.float_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), FloatActivity.class);
            startActivity(intent);
        });

        // 进入多行歌词视图测试
        findViewById(R.id.many_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ManyActivity.class);
            startActivity(intent);
        });

        // 选择歌词文件：LRC、KRC、KSC、HRC格式
        findViewById(R.id.select_file).setOnClickListener(view -> {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            } else {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("file/*");
            }
            startActivityForResult(intent, 1);
        });
        mLrcTextView = findViewById(R.id.lrc_com);
    }

    // 按照升序存储每一行的歌词
    private TreeMap<Integer, LyricsLineInfo> mLyricsInfoLyricsLineInfoTreeMap;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            String path;
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(MainActivity.this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
            }
            if (path != null && !path.equals("")) {
                String ext = FileUtils.getFileExt(path);
                if (!ext.equals("lrc") && !ext.equals("krc") && !ext.equals("ksc") && !ext.equals("hrc")) {
                    Toast.makeText(this, "请选择支持的歌词文件！", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadFile(path);
            }
        }
    }

    /**
     * 加载文件
     */
    @SuppressLint("StaticFieldLeak")
    private void loadFile(final String filePath) {
        new AsyncTask<String, Integer, String>() {

            // 接收线程任务执行结果、将执行结果显示到UI组件
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (mLyricsInfoLyricsLineInfoTreeMap != null && mLyricsInfoLyricsLineInfoTreeMap.size() > 0) {
                    String lrcCom = ""; // 获取到的歌词
                    for (int i = 0; i < mLyricsInfoLyricsLineInfoTreeMap.size(); i++) {
                        lrcCom += mLyricsInfoLyricsLineInfoTreeMap.get(i).getLineLyrics() + "\n";
                    }
                    mLrcTextView.setText(lrcCom);
                }
            }

            // 接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
            @Override
            protected String doInBackground(String... strings) {
                LyricsFileReader lyricsFileReader = LyricsIOUtils.getLyricsFileReader(filePath);
                try {
                    LyricsInfo lyricsInfo = lyricsFileReader.readFile(new File(filePath));
                    if (lyricsInfo != null) {
                        mLyricsInfoLyricsLineInfoTreeMap = lyricsInfo.getLyricsLineInfoTreeMap();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute("");
    }
}
