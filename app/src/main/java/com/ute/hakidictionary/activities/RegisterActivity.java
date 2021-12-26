package com.ute.hakidictionary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ute.hakidictionary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextView txtLogin;
    Button btnRegister;
    EditText edtName, edtEmail, edtPass, edtRepass;
    String name, email, pass, repass;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnhXa();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString().trim();
                email = edtEmail.getText().toString().trim();
                pass = edtPass.getText().toString().trim();
                repass = edtRepass.getText().toString().trim();
                if (validate(name, email, pass, repass)){
                    register();
                }
            }
        });
    }

    public void AnhXa(){
        txtLogin = findViewById(R.id.txt_login);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        edtRepass = findViewById(R.id.edt_repass);
        btnRegister = findViewById(R.id.btn_register);
    }

    public void register(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("email", edtEmail.getText().toString().trim());
            jsonObject.put("name", edtName.getText().toString().trim());
            jsonObject.put("pass", edtPass.getText().toString().trim());
            //Toast.makeText(RegisterActivity.this, edtEmail.getText().toString().trim(), Toast.LENGTH_SHORT).show();
        }catch (JSONException e){
            e.printStackTrace();
        }
        AndroidNetworking.post("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/user/createUser")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(RegisterActivity.this, response.getString("status"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public Boolean validate(String name, String email, String pass, String repass){
        if(name.length() == 0){
            edtName.requestFocus();
            edtName.setError("Tên không được để trống!");
            return false;
        }
        else if(email.length() == 0){
            edtEmail.requestFocus();
            edtEmail.setError("Email không được để trống!");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.requestFocus();
            edtEmail.setError("Email chưa đúng định dạng!");
            return false;
        }
        else if(pass.length() == 0){
            edtPass.requestFocus();
            edtPass.setError("Mật khẩu không được để trống!");
            return false;
        }
        else if(!repass.equalsIgnoreCase(pass)){
            edtRepass.requestFocus();
            edtRepass.setError("Mật khẩu nhập lại không khớp");
            return false;
        }
        return true;
    }
}