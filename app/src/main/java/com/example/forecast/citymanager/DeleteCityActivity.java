package com.example.forecast.citymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.forecast.R;
import com.example.forecast.database.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivCancle, ivRight;
    private ListView lvDelete;
    List<String> citys;//listView的数据源
    List<String> removeCitys=new ArrayList<>();
    private DeleteListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        ivCancle = findViewById(R.id.delete_iv_cancle);
        ivRight = findViewById(R.id.delete_iv_right);
        lvDelete = findViewById(R.id.delete_lv);
        //设置监听事件
        ivCancle.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        //设置适配器
        citys= DBManager.queryCity();
        adapter = new DeleteListViewAdapter(this, citys, removeCitys);
        lvDelete.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_iv_cancle:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息")
                        .setMessage("你确定要舍弃更改吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();//关闭当前的activity
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < removeCitys.size(); i++) {
                     String city=removeCitys.get(i);
                     //调用删除城市的函数
                    DBManager.deleteData(city);
                }
                //删除成功后返回上一级页面
                finish();
                break;
        }
    }
}
