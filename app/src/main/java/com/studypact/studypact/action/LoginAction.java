package com.studypact.studypact.action;

import com.studypact.studypact.model.LoginModel;
import com.studypact.studypact.util.Constants;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Kartikk on 11/13/2016.
 */

public interface LoginAction {
    @POST(Constants.login)
    Call<String> postLoginDetails(@Body LoginModel loginModel);
}
