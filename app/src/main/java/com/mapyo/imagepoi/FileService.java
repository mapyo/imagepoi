package com.mapyo.imagepoi;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FileService {
    @Multipart
    @POST("api/files.upload")
    Call<FilesUploadApiResponse> upload(
            @Query("token") String token,
            @Query("channels") String channels,
            @Part("file\"; filename=\"poi_poi.png\" ") RequestBody file
    );
}

