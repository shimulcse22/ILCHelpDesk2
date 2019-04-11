package com.shimul.ilchelpdesk;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements  View.OnClickListener{
    private EditText etUserName,etPassword,etMail,etMobile;
    private Button btSignUp,textViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUserName = findViewById(R.id.editTextName);
        etMail =findViewById(R.id.editTextEmail);
        etMobile =findViewById(R.id.editTextMobile);
        etPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);



        }
        private void userRegisration(){
            String name = etUserName.getText().toString().trim();
            String email = etMail.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            RequestModel model = new RequestModel();
            model.setName(name);
            model.setEmail(email);
            model.setMobile(mobile);
            model.setPassword(password);

            Call<DefaultResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .registration(model);

            call.enqueue(new Callback<DefaultResponseBody>() {
                @Override
                public void onResponse(Call<DefaultResponseBody> call, Response<DefaultResponseBody> response) {
                    if(response.isSuccessful()){
                        DefaultResponseBody dr = response.body();
                        Toast.makeText(SignUpActivity.this,dr.getResponseMessage(),Toast.LENGTH_LONG).show();
                        }

                }

                @Override
                public void onFailure(Call<DefaultResponseBody> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.buttonSignUp:
                    userRegisration();
                    break;
                case R.id.textViewLogin:
                    startActivity(new Intent(this,LoginActivity.class));
                    break;

            }
        }

}
