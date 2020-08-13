package com.example.forecast.citymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forecast.R;
import com.example.forecast.database.DBManager;
import com.example.forecast.database.DatabaseBean;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener{
private ImageView addIv,deleteIv,backIv;
private ListView listView;
private List<DatabaseBean> databaseBeanList;//作为列表的数据源
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv=(ImageView)findViewById(R.id.iv_add);
        deleteIv=(ImageView)findViewById(R.id.city_iv_delete);
        backIv=(ImageView)findViewById(R.id.city_iv_back);
        listView=(ListView)findViewById(R.id.lv_city);
        databaseBeanList=new ArrayList<>();
        //添加点击事件
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        //设置适配器
        adapter = new ListViewAdapter(this,databaseBeanList);
        listView.setAdapter(adapter);
    }
//获取数据库中真实 的数据源，添加到原有的数据库中，并提示adapter更新
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseBean> list = DBManager.getAllInfo();
        databaseBeanList.clear();
        databaseBeanList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
                if(DBManager.getCityCount()>5){
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"存储超过5个",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this, DeleteCityActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
