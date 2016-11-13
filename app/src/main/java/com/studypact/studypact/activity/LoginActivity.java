package com.studypact.studypact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.studypact.studypact.R;
import com.studypact.studypact.action.LoginAction;
import com.studypact.studypact.appinstance.AppInstance;
import com.studypact.studypact.model.LoginModel;
import com.studypact.studypact.util.Constants;
import com.studypact.studypact.util.Util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kartikk on 11/13/2016.
 */

public class LoginActivity extends AppCompatActivity {

    EditText first_name_et, last_name_et, phone_et, addr_et;
    Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Util.getBooleanFromStore("is_reg")) {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        first_name_et = (EditText) findViewById(R.id.firstname_edittext);
        last_name_et = (EditText) findViewById(R.id.lastname_edittext);
        phone_et = (EditText) findViewById(R.id.phone_edittext);
        addr_et = (EditText) findViewById(R.id.streetaddr_2_te);
        next_button = (Button) findViewById(R.id.login_next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });
    }

    public void postData() {
        if(!first_name_et.getText().toString().trim().isEmpty() && !last_name_et.getText().toString().trim().isEmpty() && !phone_et.getText().toString().trim().isEmpty()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.server_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LoginAction service = retrofit.create(LoginAction.class);
            final LoginModel loginModel = new LoginModel(first_name_et.getText().toString(),
                    last_name_et.getText().toString(),
                    phone_et.getText().toString());
            service.postLoginDetails(loginModel).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!response.body().equals("1")) {
                        // Next screen
                        Util.putIntoStore("user", loginModel.first_name + " " + loginModel.last_name);
                        Util.putBooleanFromStore("is_reg", true);
                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Util.showToast("Opps!! Something went wrong");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Util.showToast("Opps!! Something went wrong");
                }
            });
        }
    }
}
