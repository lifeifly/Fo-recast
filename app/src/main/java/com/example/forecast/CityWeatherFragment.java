package com.example.forecast;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.forecast.bean.WeatherBean;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    private TextView tempTv, cityTv, conditionTv, windTv, tempRangeTv, dateTv, clothIndexTv, carIndexTv, coldTv, sportTv, raysIndexTv;
    private ImageView dayIv;
    private LinearLayout futureLayout;
    private String url1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    private String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    private List<WeatherBean.ResultsBean.IndexBean> indexList;
    private String city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        //可以通过Activity传值获取到当前fragment加载的时某个地方的天气情况
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        String url = url1 + city + url2;
        //调用父类获取数据的方法
        loadData(url);
        return view;
    }

    @Override
    public void onSuccess(String result) {
        parseShowData(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    private void parseShowData(String result) {
//        使用gson解析数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.ResultsBean resultsBean = weatherBean.getResults().get(0);
//        获取指数信息集合列表
        indexList = resultsBean.getIndex();
//        设置TextView
        dateTv.setText(weatherBean.getDate());
        cityTv.setText(resultsBean.getCurrentCity());
//        获取今日天气情况
        WeatherBean.ResultsBean.WeatherDataBean todayDataBean = resultsBean.getWeather_data().get(0);
        windTv.setText(todayDataBean.getWind());
        tempRangeTv.setText(todayDataBean.getTemperature());
        conditionTv.setText(todayDataBean.getWeather());
//     获取实时天气温度情况，需要处理字符串
        String[] split = todayDataBean.getDate().split("：");
        String todayTemp = split[1].replace(")", "");
        tempTv.setText(todayTemp);
//        设置显示的天气情况图片
        Picasso.with(getActivity()).load(todayDataBean.getDayPictureUrl()).into(dayIv);
//        获取未来三天的天气情况，加载到layout当中
        List<WeatherBean.ResultsBean.WeatherDataBean> futureList = resultsBean.getWeather_data();
        futureList.remove(0);
        for (int i = 0; i < futureList.size(); i++) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
            TextView iconTv = itemView.findViewById(R.id.item_center_tv_con);
            TextView itemprangeTv = itemView.findViewById(R.id.item_center_tv_temp);
            ImageView iIv = itemView.findViewById(R.id.item_center_iv);
//          获取对应的位置的天气情况
            WeatherBean.ResultsBean.WeatherDataBean dataBean = futureList.get(i);
            idateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getWeather());
            itemprangeTv.setText(dataBean.getTemperature());
            Picasso.with(getActivity()).load(dataBean.getDayPictureUrl()).into(iIv);
        }
    }

    private void initView(View view) {
        //用于初始化控件操作
        tempTv = view.findViewById(R.id.frag_tv_currentemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_tempature);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.frag_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washercar);
        sportTv = view.findViewById(R.id.frag_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_rays);
        coldTv = view.findViewById(R.id.frag_index_tv_cold);
        dayIv = view.findViewById(R.id.frag_iv_today);
        futureLayout = view.findViewById(R.id.frag_center_layout);
        //设置点击事件
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        sportTv.setOnClickListener(this);
        coldTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.frag_index_tv_dress:
                builder.setTitle("穿衣指数");
                WeatherBean.ResultsBean.IndexBean indexBean = indexList.get(0);
                String msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg)
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("感冒指数");
                indexBean = indexList.get(2);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg)
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("紫外线指数");
                indexBean = indexList.get(4);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg)
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.frag_index_tv_washercar:
                builder.setTitle("洗车指数");
                indexBean = indexList.get(1);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg)
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.frag_index_tv_sport:
                builder.setTitle("运动指数");
                indexBean = indexList.get(3);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg)
                        .setPositiveButton("确定", null)
                        .show();
                break;
            default:
                break;
        }
    }
}
