package com.example.forecast.citymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.forecast.R;
import com.example.forecast.bean.WeatherBean;
import com.example.forecast.database.DatabaseBean;
import com.google.gson.Gson;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    List<DatabaseBean> databaseBeanList;
    ViewHolder holder;

    public ListViewAdapter(Context context, List<DatabaseBean> databaseBeanList) {
        this.context = context;
        this.databaseBeanList = databaseBeanList;
    }
//返回数据的长度
    @Override
    public int getCount() {
        return databaseBeanList.size();
    }
//返回指定位置对应的元素
    @Override
    public Object getItem(int position) {
        return databaseBeanList.get(position);
    }
//返回对应的位置
    @Override
    public long getItemId(int position) {
        return position;
    }
//构建每一条视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DatabaseBean bean = databaseBeanList.get(position);
        holder.cityTv.setText(bean.getCity());
        WeatherBean weatherBean = new Gson().fromJson(bean.getContent(), WeatherBean.class);
        WeatherBean.ResultsBean.WeatherDataBean dataBean = weatherBean.getResults().get(0).getWeather_data().get(0);
        holder.contv.setText("天气："+dataBean.getWeather());
        String[] split=dataBean.getDate().split("：");
        String todayTemp=split[1];
        holder.currnetTempTv.setText(todayTemp);
        holder.windTv.setText(dataBean.getWind());
        holder.tempRangeTv.setText(dataBean.getTemperature());
        return convertView;
    }
        class ViewHolder{
        TextView cityTv,contv,currnetTempTv,windTv,tempRangeTv;

        public ViewHolder(View view){
            cityTv=(TextView)view.findViewById(R.id.item_city_tv_city);
            contv=(TextView)view.findViewById(R.id.item_city_tv_condition);
            currnetTempTv=(TextView)view.findViewById(R.id.item_city_tv_temp);
            tempRangeTv=(TextView)view.findViewById(R.id.item_city_temprange);
            windTv=(TextView)view.findViewById(R.id.item_city_wind);
        }
    }
}
