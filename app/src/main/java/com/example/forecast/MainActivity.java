package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addCityIv, moreIv;
    LinearLayout pointLayout;
    ViewPager mianVp;
    //ViewPager的数据源
    List<Fragment> fragmentList;
    //表示需要显示的城市的集合
    List<String> cityList;
    //表示ViewPager指示器显示的集合
    List<ImageView> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv=(ImageView)findViewById(R.id.iv_add);
        moreIv=(ImageView)findViewById(R.id.iv_more);
        LinearLayout ll_point=(LinearLayout)findViewById(R.id.main_layout_point);
        mianVp=(ViewPager)findViewById(R.id.main_vp);
        pointLayout=findViewById(R.id.main_layout_point);
        //添加点击事件
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        fragmentList=new ArrayList<>();
        cityList=new ArrayList<>();
        imageList=new ArrayList<>();

        if (cityList.size()==0){
            cityList.add("北京");
            cityList.add("上海");
            cityList.add("沈阳");
        }
        //初始化ViewPager页面
        initPager();
        CityFragmentPagerAdapter cityFragmentPagerAdapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mianVp.setAdapter(cityFragmentPagerAdapter);
        //初始化ViewPAger小点点
        initPoint();
        //设置最后一个城市信息
        mianVp.setCurrentItem(fragmentList.size()-1);
        //监听圆点颜色随fragmengt改变
        setPagerListener();
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
        for (int i=0;i<fragmentList.size();i++){
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            llParams.setMargins(0,0,20,0);
            imageList.add(pIv);
            pointLayout.addView(pIv);
        }
        imageList.get(imageList.size()-1).setImageResource(R.mipmap.a2);
    }

    private void initPager() {
        //创建Fragment对象，添加到ViewPager数据源当中
        for (int i=0;i<cityList.size();i++){
            CityWeatherFragment cwFrag=new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
                break;
            case R.id.iv_more:
                break;
        }
    }
}
