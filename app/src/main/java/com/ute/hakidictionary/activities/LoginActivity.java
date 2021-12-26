package com.ute.hakidictionary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPass;
    Button btnLogin;
    TextView txtRegister;
    List<User> userList;
    User userLogin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLogin = null;
        userList = new ArrayList<>();

        loadData();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        edtUsername = findViewById(R.id.edt_username);
        edtPass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);

        txtRegister = findViewById(R.id.txt_register);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        sharedPreferences = LoginActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
//        Lay tk mk da dang nhap
        edtUsername.setText(sharedPreferences.getString("email", ""));
        edtPass.setText(sharedPreferences.getString("matkhau", ""));


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUsername.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                Boolean loginResult = checkUser();
                if (email.equalsIgnoreCase("") || pass.equalsIgnoreCase("")){

                }
                else{
                    if (loginResult){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        //Lưu email và pass
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("matkhau", pass);
                        editor.putInt("userId", userLogin.getId());
                        editor.commit();

                        //Vào trang chủ
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userId", userLogin.getId());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu chưa đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        SharedPreferences myPref = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        String email = myPref.getString("email", "");
        String pass = myPref.getString("matkhau", "");
        if(!(email.equals("") && pass.equals(""))) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
    private boolean checkUser() {
        String email = edtUsername.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        if(userList == null || userList.isEmpty()) return false;
        for(User user : userList) {
            if(email.equals(user.getEmail()) && pass.equals(user.getPass())) {
                userLogin = user;
                return true;
            }
        }
        return false;
    }

    private void loadData() {
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/user")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i=0; i<response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                User user = new User();
                                user.setId(obj.getInt("id"));
                                user.setName(obj.getString("name"));
                                user.setEmail(obj.getString("email"));
                                user.setPass(obj.getString("pass"));
                                userList.add(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error: ", anError + "");
                    }
                });
    }
}