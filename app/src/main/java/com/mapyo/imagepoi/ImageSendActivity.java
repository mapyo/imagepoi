package com.mapyo.imagepoi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

public class ImageSendActivity extends AppCompatActivity implements ImageSendView {

    private ImageSendPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_send);
        setTitle(getString(R.string.uploading));
        String slackToken = getString(R.string.slack_token);
        String slackChannel = getString(R.string.slack_channel);

        mPresenter = new ImageSendPresenter(this);

        Uri imageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        File uploadFile = new File(FileUtil.getFilePath(getApplicationContext(), imageUri));

        showMessage(getString(R.string.upload_started));
        mPresenter.sendImageToSlack(uploadFile, slackToken, slackChannel);
    }

    @Override
    public void showSuccess(String slackChannel) {
        showMessage(getString(R.string.upload_finished, slackChannel));
        finish();
    }

    @Override
    public void showFailed() {
        showMessage(getString(R.string.upload_failed));
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
