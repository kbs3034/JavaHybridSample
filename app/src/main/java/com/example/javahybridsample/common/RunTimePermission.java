package com.example.javahybridsample.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 런타임 권한 요청을 돕는 헬퍼 클래스입니다.
 * 작성자: banseogg
 */
public class RunTimePermission
{ 
	private static RunTimePermission m_Instance;
	public static RunTimePermission getInstance()
	{
		if (m_Instance == null)
			m_Instance = new RunTimePermission();
		
		return m_Instance;
	}
	
	private final int MY_PERMISSIONS_REQUEST = 1;

	
	// 권한 확인 (권한이 있으면 true 리턴)
	public boolean checkPermission(Context context, String permission)
	{
		int permissoionCheck = ContextCompat.checkSelfPermission(context, permission);
		
		if (permissoionCheck == PackageManager.PERMISSION_GRANTED)
			return true;
		else
			return false;
	}
	
	// 권한 요청 (단건)
	public void requestPermission(Activity activity, String permission)
	{
		if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
		{
			//  한번이라도 거절한 경우 (다시보지않기 기능 추가된 다이얼로그 팝업)
			ActivityCompat.requestPermissions(activity, new String[]{permission}, MY_PERMISSIONS_REQUEST);
		}
		else
		{
			// 권한 요청
			ActivityCompat.requestPermissions(activity, new String[]{permission}, MY_PERMISSIONS_REQUEST);
		}
	}
	
	// 권한 요청 (다건)
	public void requestPermission(Activity activity, String[] permission)
	{
		// 권한 요청
		ActivityCompat.requestPermissions(activity, permission, MY_PERMISSIONS_REQUEST);
	}

}
