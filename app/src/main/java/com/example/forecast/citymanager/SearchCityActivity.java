package com.example.forecast.citymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forecast.MainActivity;
import com.example.forecast.R;
import com.example.forecast.base.BaseActivity;
import com.example.forecast.bean.WeatherBean;
import com.google.gson.Gson;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener{
    private EditText searchEt;
    private ImageView searchIv;
    private GridView searchGv;
    String[] hotCitys = {"北京", "上海", "广州", "深圳", "珠海", "佛山", "南京", "苏州", "厦门", "长沙", "成都", "福州",
            "杭州", "武汉", "青岛", "西安", "太原", "沈阳", "重庆", "天津", "南宁"};
    private ArrayAdapter<String> adapter;
    private String url1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    private String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        searchEt = findViewById(R.id.search_edit_city);
        searchIv = findViewById(R.id.search_iv_search);
        searchGv = findViewById(R.id.search_gv);
        searchIv.setOnClickListener(this);
        //设置适配器
        adapter = new ArrayAdapter<String>(this,R.layout.item_hotcity,hotCitys);
        searchGv.setAdapter(adapter);
setListener();
    }

    private void setListener() {
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city=hotCitys[position];
                String url=url1+city+url2;
                loadData(url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_iv_search:
                city = searchEt.getText().toString();
                if (!TextUtils.isEmpty(city)){
                    //判断是否能找到这个城市
                    String url=url1+ city +url2;
                    loadData(url);
                }else {
                    Toast.makeText(this,"输入的内容不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        if (weatherBean.getError()==0){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//清空原来的栈,并开启一个新栈
            intent.putExtra("city",city);
            startActivity(intent);
        }else {
            Toast.makeText(this,"暂时未收入此城市天气信息",Toast.LENGTH_SHORT).show();
        }
    }
}
