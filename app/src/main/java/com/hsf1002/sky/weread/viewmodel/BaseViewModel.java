package com.hsf1002.sky.weread.viewmodel;

import android.content.Context;

import com.hsf1002.sky.weread.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-9.
 */

public class BaseViewModel {
    protected Context context;
    private List<Disposable> disposables = new ArrayList<>();

    public BaseViewModel(Context context) {
        this.context = context;
    }

    public void addDisposable(Disposable disposable)
    {
        this.disposables.add(disposable);
    }

    public Map tokenMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("access-token", SharedPreUtils.getInstance().getString("token", "weyue"));
        map.put("app-type", "Android");

        return map;
    }

    public void onDestroy()
    {
        if (disposables.size() > 0)
        {
            for (Disposable disposable:disposables)
            {
                disposable.dispose();
            }
            disposables.clear();
        }
    }
}
