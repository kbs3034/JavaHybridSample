package com.example.javahybridsample.activity.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javahybridsample.dynamiclink.DynamicLinkData;
import com.example.javahybridsample.rxbus.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 *
 * BaseActivity Class
 * 작성자: banseogg
 * compositeDisposable - 푸시, 다이나믹링크를 통해 앱 진입시, PushData, DynamicData 등 RXbus subscribe 를 발생시키고,
 * 업무에 맞는 비즈니스로직을 수행하며, 액티비티 종료시 한번에 dispose된다.
 * disposableMap - 푸시, 다이나믹링크를 제외한 일회성 콜백함수를 담아 놓는 맵이다.
 * 예를들면, 브릿지를 통해 activity result로 처리해야하는 콜백 함수등을 RXbus로 간결하게 처리하고,
 * 콜백함수가 정상적으로 수행되고 나면, disposableMap에서 해당 disposoble을 dispose한다.
 * receiverList - sms리시버등 일회성으로 등록하는 리시버가 있으면, 해당리시버를 등록하고,
 * 액티비티가 종료될 때 리스트에 있는 리시버를 일괄 해제하도록 한다.
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity {
	public WebView webView;

	public List<BroadcastReceiver> receiverList = new ArrayList<>();

	public CompositeDisposable compositeDisposable = new CompositeDisposable();
	public Map<String, Disposable> disposableMap = new HashMap<String, Disposable>();

	@SuppressLint("ResourceType")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//DynamicLink
		RxBus.getInstance().listen(DynamicLinkData.class).subscribe(it -> {
			//다이나믹 링크 들어왔을 때, 여기서 비지니스 로직 처리
		});
	}

	@Override
	public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
		receiverList.add(receiver);
		return super.registerReceiver(receiver, filter);
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		super.unregisterReceiver(receiver);
		receiverList.remove(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if(webView != null) {
			webView.removeAllViews();
			webView.destroy();
		}

		while(receiverList.size() > 0) {
			unregisterReceiver(receiverList.get(0));
		}
		//push,dynamiclink등등
		compositeDisposable.dispose();

		//push, dynamiclink 등을 제외한 일회성 콜백
		disposableMap.forEach((key, disposable) -> {
			disposable.dispose();
		});
		disposableMap.clear();
	}
}
