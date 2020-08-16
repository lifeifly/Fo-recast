package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.forecast.citymanager.CityManagerActivity;
import com.example.forecast.citymanager.SearchCityActivity;
import com.example.forecast.database.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addCityIv, moreIv;
    LinearLayout pointLayout;
    ViewPager mianVp;
    RelativeLayout mainRoot;
    //ViewPager的数据源
    List<Fragment> fragmentList;
    //表示需要显示的城市的集合
    List<String> cityList;
    //表示ViewPager指示器显示的集合
    List<ImageView> imageList;
    private CityFragmentPagerAdapter cityFragmentPagerAdapter;
    private SharedPreferences bg_pref;
    private int bgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = (ImageView) findViewById(R.id.iv_add);
        moreIv = (ImageView) findViewById(R.id.iv_more);
        LinearLayout ll_point = (LinearLayout) findViewById(R.id.main_layout_point);
        mianVp = (ViewPager) findViewById(R.id.main_vp);
        pointLayout = findViewById(R.id.main_layout_point);
        mainRoot=findViewById(R.id.main_rootlayout);
        //换背景
        exchangeBg();
        //添加点击事件
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        cityList = DBManager.queryCity();//获取数据库包含的城市信息列表
        imageList = new ArrayList<>();

        if (cityList.size() == 0) {
            cityList.add("北京");
            cityList.add("上海");
            cityList.add("沈阳");
        }
        //因为可能搜索界面点击跳转到此界面,会传值，尝试获取下
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if (!cityList.contains(city) && city != null) {
            cityList.add(city);
        }
        //初始化ViewPager页面
        initPager();
        cityFragmentPagerAdapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mianVp.setAdapter(cityFragmentPagerAdapter);
        //初始化ViewPAger小点点
        initPoint();
        //设置最后一个城市信息
        mianVp.setCurrentItem(fragmentList.size() - 1);
        //监听圆点颜色随fragmengt改变
        setPagerListener();
    }
//换壁纸的函数
    public void exchangeBg(){
        bg_pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = bg_pref.getInt("bg",2);
        switch(bgNum){
            case 0:
                mainRoot.setBackgroundResource(R.mipmap.bg);
                break;
            case 1:
                mainRoot.setBackgroundResource(R.mipmap.bg2);
                break;
            case 2:
                mainRoot.setBackgroundResource(R.mipmap.bg3);
                break;
        }
    }
    private void setPagerListener() {
        //设置监听事件
        mianVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageList.size(); i++) {
                    imageList.get(i).setImageResource(R.mipmap.a1);
                }
                imageList.get(position).setImageResource(R.mipmap.a2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        //创建小圆点ViewPager页面的函数
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            llParams.setMargins(0, 0, 20, 0);
            imageList.add(pIv);
            pointLayout.addView(pIv);
        }
        imageList.get(imageList.size() - 1).setImageResource(R.mipmap.a2);
    }

    private void initPager() {
        //创建Fragment对象，添加到ViewPager数据源当中
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city", cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_add:
                intent = new Intent(MainActivity.this, CityManagerActivity.class);
                break;
            case R.id.iv_more:
                intent = new Intent(this, MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    //当页面重新获取焦点之前执行的,此处完成ViewPager的更新
    @Override
    protected void onRestart() {
        super.onRestart();
        //获取数据库当中还剩下的数据集合
        List<String> list = DBManager.queryCity();
        if (cityList.size() == 0) {
            cityList.add("北京");
        }
        cityList.clear();
        cityList.addAll(list);
        //剩余城市也要创建Fragment页面
        fragmentList.clear();
        initPager();
        cityFragmentPagerAdapter.notifyDataSetChanged();
        //页面数量发生改变指示器 的数量也会改变
        imageList.clear();
        pointLayout.removeAllViews();//将布局中所有元素一处
        initPoint();
        mianVp.setCurrentItem(fragmentList.size()-1);
    }
}
