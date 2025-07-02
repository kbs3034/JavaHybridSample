package com.example.javahybridsample.activity.sample;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javahybridsample.api.APIClient;
import com.example.javahybridsample.api.APIInterface;
import com.example.javahybridsample.api.data.sample.SampleUser;
import com.example.javahybridsample.api.data.sample.SampleUserList;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 샘플 API 호출을 처리하는 ViewModel 입니다.
 * 작성자: banseogg
 */
public class SampleViewModel extends ViewModel {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public MutableLiveData<JSONObject> result = new MutableLiveData<JSONObject>();
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    /*
    * 샘플 retro fit api ()
    * */
    public void doGetListResources() {
        Call<ResponseBody> call = apiInterface.doGetListResources();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                logger.info("TAG",response.code()+"");
                result.postValue(toJson(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                logger.info("failure");
                call.cancel();
            }
        });
    }

    /*
     * 샘플 retro fit api
     * */
    public void createUser(SampleUser user) {
        Call<ResponseBody> call = apiInterface.createUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                logger.info("TAG",response.code()+"");
                result.postValue(toJson(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                logger.info("failure");
                call.cancel();
            }
        });
    }

    /*
     * 샘플 retro fit api (SampleUserList Object로 받기)
     * */
    public void doGetUserList(String page) {
        Call<SampleUserList> call = apiInterface.doGetUserList(page);
        call.enqueue(new Callback<SampleUserList>() {
            @Override
            public void onResponse(Call<SampleUserList> call, Response<SampleUserList> response) {
                logger.info("TAG",response.code()+"");
                SampleUserList list = response.body();
                Gson gson =new Gson();
                try {
                    result.postValue(new JSONObject(gson.toJson(list)));
                } catch (JSONException e) {
                    logger.info("convert error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SampleUserList> call, Throwable t) {
                logger.info("failure");
                call.cancel();
            }
        });
    }

    public void doGetUserListForJsonObject(String page) {
        Call<ResponseBody> call = apiInterface.doGetUserListForJsonObject(page);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                logger.info("TAG",response.code()+"");
                result.postValue(toJson(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                logger.info("failure");
                call.cancel();
            }
        });
    }

    public void doCreateUserWithField(String name, String job) {
        Call<ResponseBody> call = apiInterface.doCreateUserWithField(name, job);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                logger.info("TAG",response.code()+"");
                result.postValue(toJson(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                logger.info("failure");
                call.cancel();
            }
        });
    }

    private JSONObject toJson(Response<ResponseBody> response) {
        JSONObject result;
        try {
            result = new JSONObject(response.body().string());
        } catch (IOException |JSONException e) {
            e.printStackTrace();
            result = new JSONObject();
        }
        return result;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
