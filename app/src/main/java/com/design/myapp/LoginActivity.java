package com.design.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back,tv_register;
    private Button btn_login;
    private String userName,psw,spPsw;
    private EditText et_user_name,et_psw;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {

        tv_register=findViewById(R.id.btn_register);

        btn_login=findViewById(R.id.btn_login2);
        et_user_name=findViewById(R.id.et_number);
        et_psw=findViewById(R.id.et_password);


        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();

                String md5Psw= MD5Utils.md5(psw);

                spPsw=readPsw(userName);

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "Input Username!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this, "Input Password!", Toast.LENGTH_SHORT).show();
                    return;

                }else if(md5Psw.equals(spPsw)){

                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    saveLoginStatus(true, userName);

                    Intent data=new Intent();

                    data.putExtra("isLogin",true);

                    setResult(RESULT_OK,data);

                    LoginActivity.this.finish();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    return;
                }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw))){
                    Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(LoginActivity.this, "can not find username!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String readPsw(String userName){

        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);

        return sp.getString(userName , "");
    }

    private void saveLoginStatus(boolean status,String userName){

        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);

        SharedPreferences.Editor editor=sp.edit();

        editor.putBoolean("isLogin", status);

        editor.putString("loginUserName", userName);

        editor.commit();
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){

            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){

                et_user_name.setText(userName);

                et_user_name.setSelection(userName.length());
            }
        }
    }
}

