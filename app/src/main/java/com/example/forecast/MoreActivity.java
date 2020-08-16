package com.example.forecast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.forecast.base.BaseActivity;
import com.example.forecast.database.DBManager;

import static java.util.ResourceBundle.clearCache;

public class MoreActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backIv;
    private TextView bgTv, cacheTv, versionTv, shareTv;
    private RadioGroup moreRg;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        backIv = (ImageView) findViewById(R.id.more_title_iv);
        moreRg = (RadioGroup) findViewById(R.id.more_rg);
        bgTv = (TextView) findViewById(R.id.more_content_bg);
        cacheTv = (TextView) findViewById(R.id.more_content_cache);
        versionTv = (TextView) findViewById(R.id.more_content_version);
        shareTv = (TextView) findViewById(R.id.more_content_share);

        backIv.setOnClickListener(this);
        bgTv.setOnClickListener(this);
        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);

        String versionName = getVerionName();
        versionTv.setText("当前版本：   " + versionName);

        preferences = getSharedPreferences("bg_pref", MODE_PRIVATE);
        setBgListener();
    }

    private void setBgListener() {
        //设置改变背景图片，将要设置的背景写入SharePreference中的单选按钮的监听
        moreRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取目前的默认壁纸
                int bg = preferences.getInt("bg", 0);
                SharedPreferences.Editor editor = preferences.edit();
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (checkedId) {
                    case R.id.more_rb_green:
                        if (bg == 0) {
                            Toast.makeText(MoreActivity.this, "你选择的为当前壁纸，无须改变", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg", 0);
                        editor.commit();
                        break;
                    case R.id.more_rb_pink:
                        if (bg == 1) {
                            Toast.makeText(MoreActivity.this, "你选择的为当前壁纸，无须改变", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg", 1);
                        editor.commit();
                        break;
                    case R.id.more_rb_blue:
                        if (bg == 2) {
                            Toast.makeText(MoreActivity.this, "你选择的为当前壁纸，无须改变", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg", 2);
                        editor.commit();
                        break;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_title_iv:
                finish();
                break;
            case R.id.more_content_bg:
                if (moreRg.getVisibility() == View.VISIBLE) {
                    moreRg.setVisibility(View.GONE);
                } else {
                    moreRg.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.more_content_cache:
                clearDataCache();
                break;
            case R.id.more_content_share:
                shareSoftware("说天气app是一款超萌超可爱的天气预报软件，画面简约，播报天气情况非常精准，快来下载吧！");
                break;
        }
    }

    private void shareSoftware(String s) {
        //分享软件
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, s);
        startActivity(Intent.createChooser(intent, "说天气"));
    }

    private void clearDataCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息：").setMessage("确定要清楚缓存吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllData();
                        Toast.makeText(MoreActivity.this, "cache cleared", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).setPositiveButton("取消", null)
                .create().show();
    }

    private String getVerionName() {
        //获取应用的版本名称
        String verionName = null;
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            verionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verionName;
    }


}
