package com.ooowl.oowl.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAobyZY14:APA91bHOmfq7042blgv6noy_MY8rJo_N7seRsguHMKfMU8OAw8ByNO2KI6JfdHJmRW24GXCsXhPagTb0L8GlQx-ixKQWLXJ3uUSvarcZoWEjXkIUFK30dzwPUwD-PQb54yu1Ft0wL1jy"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationData body);
}
