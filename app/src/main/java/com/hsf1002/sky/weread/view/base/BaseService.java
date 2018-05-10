package com.hsf1002.sky.weread.view.base;

import android.app.Service;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-9.
 */

public abstract class BaseService extends Service {
    private CompositeDisposable compositeDisposable;

    protected void addDisposable(Disposable disposable)
    {
        if (compositeDisposable == null)
        {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (compositeDisposable != null)
        {
            compositeDisposable.dispose();
        }
    }
}
