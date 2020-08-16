package com.example.forecast.citymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forecast.R;

import java.util.List;

public class DeleteListViewAdapter extends BaseAdapter {
    List<String> citys;
Context context;
List<String> removeCitys;//要删除的城市记录
    public DeleteListViewAdapter(Context context,List<String> citys,List<String> removeCitys) {
        this.removeCitys=removeCitys;
        this.context=context;
        this.citys = citys;
    }

    @Override
    public int getCount() {
        return citys.size();
    }

    @Override
    public Object getItem(int position) {
        return citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_delete_layout,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        final String city=citys.get(position);
        holder.itemTv.setText(city);
        holder.itemIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citys.remove(city);
                removeCitys.add(city);
                notifyDataSetChanged();//删除后提示adapter更新
            }
        });
        return convertView;
    }
    class ViewHolder{

        TextView itemTv;
        ImageView itemIv;
        ViewHolder(View view){
            itemTv=view.findViewById(R.id.item_delete_tv);
            itemIv=view.findViewById(R.id.item_delte_iv);
        }
    }
}
