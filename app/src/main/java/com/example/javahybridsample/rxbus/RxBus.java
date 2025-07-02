package com.example.javahybridsample.rxbus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
/**
 * 앱 전역 이벤트 전달을 위한 RxJava 버스입니다.
 * 작성자: banseogg
 */
public final class RxBus {
    private static volatile RxBus instance;

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    PublishSubject publisher = PublishSubject.create();

    public void publish(Object event) {
        if (publisher.hasObservers())
            publisher.onNext(event);
    }

    @SuppressWarnings("unchecked")
    public Observable<?> listen(Class eventType){
        return publisher.ofType(eventType);
    }
}