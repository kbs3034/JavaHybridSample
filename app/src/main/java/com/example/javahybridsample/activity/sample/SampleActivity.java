package com.example.javahybridsample.activity.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.javahybridsample.api.data.sample.SampleUser;
import com.example.javahybridsample.databinding.ActivitySampleBinding;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST API 샘플 호출과 웹뷰 화면으로 이동하는 샘플 액티비티입니다.
 * 작성자: banseogg
 */
public class SampleActivity extends AppCompatActivity {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private ActivitySampleBinding binding;
    private SampleViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        viewModel.result.observe(this, jsonObject -> {
            TextView tv_result = (TextView) binding.cardApiTestResult.getChildAt(0);
            tv_result.setText(jsonObject.toString());
        });

        binding.cardApiTestBtn1.setOnClickListener(view -> {
                viewModel.doGetListResources();
            }
        );

        binding.cardApiTestBtn2.setOnClickListener(view -> {
            SampleUser sampleUser = new SampleUser();
                    viewModel.createUser(sampleUser);
                }
        );

        binding.cardApiTestBtn3.setOnClickListener(view -> {
                    viewModel.doGetUserListForJsonObject("1");
                }
        );

        binding.cardApiTestBtn4.setOnClickListener(view -> {
                    viewModel.doCreateUserWithField("morpheus","leader");
                }
        );

        binding.cardApiTestBtn5.setOnClickListener(view -> {
                    viewModel.doGetUserList("1");
                }
        );

        binding.cardWebviewBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this,SampleWebviewActivity.class);
            intent.putExtra("url","file:///android_asset/index_test.html");
            startActivity(intent);
        });

    }
}
