package com.mapyo.imagepoi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ImageSendPresenter {
    private ImageSendView mView;

    ImageSendPresenter(ImageSendView view) {
        mView = view;
    }

    public void sendImageToSlack(File uploadFile, String slackToken, final String slackChannel) {

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), uploadFile);

        SlackApiClient.getInstance()
                .create(FileService.class)
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
