package com.studypact.studypact.action;

import com.studypact.studypact.model.LoginModel;
import com.studypact.studypact.util.Constants;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Kartikk on 11/13/2016.
 */

public interface LoginAction {
    @POST(Constants.server_url+Constants.login)
    Call<Void> postLoginDetails(LoginModel loginModel);
}
