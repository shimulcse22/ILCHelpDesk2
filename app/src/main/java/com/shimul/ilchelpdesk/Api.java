package com.shimul.ilchelpdesk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @POST("registration")
    Call<DefaultResponseBody> registration(@Body RequestModel requestModel);

    @POST("login")
    Call<DefaultResponseBody> userLogin(@Body LoginRequest loginRequest);

    @GET("schools")
    Call<DefaultResponseBody<List<School>>> getSchools();
    @POST("ticket/create")
    Call<DefaultResponseBody> ticketCreate(@Body TicketRequest ticketRequest);


}
