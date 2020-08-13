package com.example.forecast.base;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;




/**
 * xutils加载网络的步骤
 * 1.声明整体模式
 * 2.执行网络请求操作
 */
public class BaseFragment extends Fragment implements Callback.CommonCallback<String>{
    public void loadData(String path) {
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }


    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
