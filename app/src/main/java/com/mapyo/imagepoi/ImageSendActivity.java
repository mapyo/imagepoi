package com.mapyo.imagepoi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageSendActivity extends AppCompatActivity {

    static final String ENDPOINT = "https://slack.com/";
    private String mSlackToken;
    private String mSlackChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_send);
        setTitle(getString(R.string.uploading));
        mSlackToken = getString(R.string.slack_token);
        mSlackChannel = getString(R.string.slack_channel);

        Uri imageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        File uploadFile = new File(FileUtil.getFilePath(getApplicationContext(), imageUri));

        sendSlack(uploadFile);
    }


    private void sendSlack(File uploadFile) {
        showMessage(getString(R.string.upload_started));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(
                                BuildConfig.DEBUG ?
                                        HttpLoggingInterceptor.Level.BODY :
                                        HttpLoggingInterceptor.Level.BASIC

                        ))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), uploadFile);

        retrofit.create(FileService.class)
                .upload(mSlackToken, mSlackChannel, body)
                .enqueue(new Callback<FilesUploadApiResponse>() {
                    @Override
                    public void onResponse(Call<FilesUploadApiResponse> call, Response<FilesUploadApiResponse> response) {
                        if (response.body().mOk) {
                            showSuccess();
                        } else {
                            showFailed();
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<FilesUploadApiResponse> call, Throwable throwable) {
                        showFailed();
                        finish();
                    }
                });
    }

    private void showSuccess() {
        showMessage(getString(R.string.upload_finished, mSlackChannel));
    }

    private void showFailed() {
        showMessage(getString(R.string.upload_failed));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
