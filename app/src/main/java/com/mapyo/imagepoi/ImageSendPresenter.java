package com.mapyo.imagepoi;

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

class ImageSendPresenter {
    private ImageSendView mView;
    private static final String ENDPOINT = "https://slack.com/";

    ImageSendPresenter(ImageSendView mView) {
        mView = mView;
    }

    public void sendImageToSlack(File uploadFile, String slackToken, final String slackChannel) {

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
                .upload(slackToken, slackChannel, body)
                .enqueue(new Callback<FilesUploadApiResponse>() {
                    @Override
                    public void onResponse(Call<FilesUploadApiResponse> call, Response<FilesUploadApiResponse> response) {
                        if (response.body().mOk) {
                            mView.showSuccess(slackChannel);
                        } else {
                            mView.showFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<FilesUploadApiResponse> call, Throwable throwable) {
                        mView.showFailed();
                    }
                });
    }
}
