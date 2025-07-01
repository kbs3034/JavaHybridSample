package com.example.javahybridsample.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;;
import androidx.lifecycle.MutableLiveData;

import com.example.javahybridsample.R;
import com.example.javahybridsample.activity.sample.SampleActivity;
import com.example.javahybridsample.common.RunTimePermission;
import com.example.javahybridsample.databinding.ActivityIntroBinding;
import com.example.javahybridsample.dialog.CommonBottomSheetDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class IntroActivity extends AppCompatActivity {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private ActivityResultLauncher<String> requestPermissionLauncher;

    private ActivityIntroBinding binding;

    private MutableLiveData<Boolean> isPermissionChecked = new MutableLiveData<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * view binding
         * */
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         * 권한 체크
         * */
        checkPermission();

        /*
         * 권한체크 이후 sample activity 이동
         * */
        isPermissionChecked.observe(this, isGranted -> {
            if(isGranted) {
                Intent intent = new Intent(this, SampleActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    /**
     * 앱 필요한 권한을 체크하고 요청한다.
     * */
    private void checkPermission() {
        //저장된 위치정보가 없을 때, 최초 실행시 초기 위치 설정을 위해 위치정보수집 권한을 요청한다. 거부시 현재위치 시청으로 초기 설정합니다.
        if(CheckRuntimePermission()){
            isPermissionChecked.postValue(true);
        }
    }

    protected boolean CheckRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionsList = new ArrayList<String>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!RunTimePermission.getInstance().checkPermission(this, Manifest.permission.READ_PHONE_NUMBERS)) {
                    permissionsList.add(Manifest.permission.READ_PHONE_NUMBERS);
                }
            } else {
                if (!RunTimePermission.getInstance().checkPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                    permissionsList.add(Manifest.permission.READ_PHONE_STATE);
                }
            }
            if (!RunTimePermission.getInstance().checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!RunTimePermission.getInstance().checkPermission(this, Manifest.permission.CAMERA)) {
                permissionsList.add(Manifest.permission.CAMERA);
            }

            if (!RunTimePermission.getInstance().checkPermission(this, Manifest.permission.READ_CONTACTS)) {
                permissionsList.add(Manifest.permission.READ_CONTACTS);
            }
            if (!RunTimePermission.getInstance().checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (permissionsList.isEmpty()) {
                return true;
            } else if (permissionsList.size() == 1) {
                RunTimePermission.getInstance().requestPermission(this, permissionsList.get(0));
                return false;
            } else {
                String[] PERMISSIONS = new String[permissionsList.size()];
                permissionsList.toArray(PERMISSIONS);

                RunTimePermission.getInstance().requestPermission(this, PERMISSIONS);
                return false;
            }
        }

        return true;
    }

    private void showPermissionMsgPop(String permissions[]) {
        String alertMsg = "앱 사용을 위해 아래 권한을 허용해 주시기 바랍니다.\n\n";
        //저장공간 접근, 전화걸기, 카메라
        for (int i = 0; i < permissions.length; ++i) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (permissions[i].equals("android.permission.READ_PHONE_NUMBERS")) {
                    alertMsg += "전화걸기";
                } else {
                    if (permissions[i].equals("android.permission.READ_PHONE_STATE")) {
                        alertMsg += "전화걸기";
                    }
                }
            }
            if (permissions[i].equals("android.permission.WRITE_EXTERNAL_STORAGE"))
                alertMsg += "저장공간 접근";
            else if (permissions[i].equals("android.permission.CAMERA"))
                alertMsg += "카메라";
            else if (permissions[i].equals("android.permission.READ_CONTACTS"))
                alertMsg += "연락처";
            else if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE"))
                alertMsg += "사진첩";

            if (permissions.length > i + 1)
                alertMsg += ", ";

        }
        CommonBottomSheetDialog bottomSheetDialog = new CommonBottomSheetDialog();
        bottomSheetDialog.setBottomSheetOption(bottomSheetDialog.new BottomSheetOption("",alertMsg,false, "종료", "확인"));

        bottomSheetDialog.setClickListener(new CommonBottomSheetDialog.BottomSheetButtonClickListener() {
            @Override
            public void onLeftButtonClicked(View view) {
                System.exit(0);
            }

            @Override
            public void onRightButtonClicked(View view) {
                checkPermission();
            }
        });
        bottomSheetDialog.show(getSupportFragmentManager(), "permissionBottomSheet");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode > 0 && permissions.length > 0 && grantResults.length > 0) {
            ArrayList<String> reqPermissionList = new ArrayList<String>();

            for (int i = 0; i < permissions.length; ++i) {
                if (permissions[i].equals("android.permission.READ_PHONE_STATE")) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        reqPermissionList.add("android.permission.READ_PHONE_STATE");
                    }
                } else if (permissions[i].equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        reqPermissionList.add("android.permission.WRITE_EXTERNAL_STORAGE");
                    }
                } else if (permissions[i].equals("android.permission.CAMERA")) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        reqPermissionList.add("android.permission.CAMERA");
                    }
                } else if (permissions[i].equals("android.permission.READ_CONTACTS")) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        reqPermissionList.add("android.permission.READ_CONTACTS");
                    }
                } else if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE")) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        reqPermissionList.add("android.permission.READ_EXTERNAL_STORAGE");
                    }
                }

            }

            if (reqPermissionList.size() > 0) {
                String[] PERMISSIONS = new String[reqPermissionList.size()];
                reqPermissionList.toArray(PERMISSIONS);
                showPermissionMsgPop(PERMISSIONS);
            } else {
                checkPermission();
            }
        }
    }

    /**
     * 위치정보 수집 권한 설정 바텀 시트팝업을 호출한다.
     * */
    private void showLocationPermissionBottomSheet() {
        CommonBottomSheetDialog bottomSheetDialog = new CommonBottomSheetDialog();
        bottomSheetDialog.setBottomSheetOption(bottomSheetDialog.new BottomSheetOption("",getString(R.string.location_permission_comment),false, "거부", "설정"));

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {

            } else {
                checkPermission();
            }
        });

        bottomSheetDialog.setClickListener(new CommonBottomSheetDialog.BottomSheetButtonClickListener() {
            @Override
            public void onLeftButtonClicked(View view) {
                checkPermission();
            }

            @Override
            public void onRightButtonClicked(View view) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });
        bottomSheetDialog.show(getSupportFragmentManager(), "locationPermissionBottomSheet");
    }
}
