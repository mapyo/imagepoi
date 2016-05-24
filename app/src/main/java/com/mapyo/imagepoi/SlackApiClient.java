package com.mapyo.imagepoi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class SlackApiClient {
    private static SlackApiClient sInstance;
    private static final String ENDPOINT = "https://slack.com/";
    private Retrofit mRetrofit;

    private SlackApiClient() {
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

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static SlackApiClient getInstance() {
        if (sInstance == null) {
            sInstance = new SlackApiClient();
        }
        return sInstance;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
