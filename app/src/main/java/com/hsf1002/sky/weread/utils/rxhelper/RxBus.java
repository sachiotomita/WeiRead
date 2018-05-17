package com.hsf1002.sky.weread.utils.rxhelper;

import java.io.ObjectInputStream;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hefeng on 18-5-16.
 */

public class RxBus {

    private static volatile RxBus sInstance;
    private final PublishSubject<Object> eventBus = PublishSubject.create();

    public static RxBus getInstance()
    {
        if (sInstance == null)
        {
            synchronized (RxBus.class)
            {
                if (sInstance == null)
                {
                    sInstance = new RxBus();
                }
            }
        }

        return sInstance;
    }

    public void post(Object event)
    {
        eventBus.onNext(event);
    }

    public void post(int code, Object event)
    {
        Message msg = new Message(code, event);
        eventBus.onNext(msg);
    }

    public <T> Observable<T> toObservable(Class<T> cls)
    {
        return eventBus.ofType(cls);
    }

    class Message{
        int code;
        Object event;

        public Message(int code,Object event){
            this.code = code;
            this.event = event;
        }
    }

}
