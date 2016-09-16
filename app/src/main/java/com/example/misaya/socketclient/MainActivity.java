package com.example.misaya.socketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.WildcardType;

public class MainActivity extends Activity {

    EditText username,password;
    Button btn_regist,btn_login,btn_reset;

    Handler handler;
    ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Creat();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 0x123){
                    String return_msg = msg.obj.toString();         //服务器传来的信息
                    switch (return_msg){
                        case "R_SUCCESS":
                            Toast.makeText(getApplicationContext(),"注册成功!",Toast.LENGTH_LONG).show();break;
                        case "R_FAIL":
                            Toast.makeText(getApplicationContext(),"注册失败!",Toast.LENGTH_LONG).show();break;
                        case "REPEAT":
                            Toast.makeText(getApplicationContext(),"注册失败!用户名重复!",Toast.LENGTH_LONG).show();break;
                        case "L_SUCCESS":
                            Toast.makeText(getApplicationContext(),"登入成功!",Toast.LENGTH_LONG).show();break;
                        case "L_FAIL":
                            Toast.makeText(getApplicationContext(),"登入失败!",Toast.LENGTH_LONG).show();break;
                        default:
                            Toast.makeText(getApplicationContext(),"Connection Error!!!",Toast.LENGTH_LONG).show();break;
                    }
                }
            }
        };

        clientThread = new ClientThread(handler);

        new Thread(clientThread).start();

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNull cn = new CheckNull(username.getText().toString(),password.getText().toString());
                if(cn.check()){
                    try{                                                //发送 客户端 -> EditText -> msg -> 服务器
                        Message msg = new Message();
                        msg.what = 0x345;
                        Bundle bundle = new Bundle();
                        bundle.putString("user",username.getText().toString());
                        bundle.putString("pass",password.getText().toString());
                        bundle.putString("type","REGISTER");
                        msg.setData(bundle);
                        clientThread.revHandler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"用户名或密码含有空字符!",Toast.LENGTH_LONG).show();
                }
                username.setText("");
                password.setText("");
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNull cn = new CheckNull(username.getText().toString(),password.getText().toString());
                if(cn.check()){
                    try{                                                //发送 客户端 -> EditText -> msg -> 服务器
                        Message msg = new Message();
                        msg.what = 0x345;
                        Bundle bundle = new Bundle();
                        bundle.putString("user",username.getText().toString());
                        bundle.putString("pass",password.getText().toString());
                        bundle.putString("type","LOGIN");
                        msg.setData(bundle);
                        clientThread.revHandler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"用户名或密码含有空字符!",Toast.LENGTH_LONG).show();
                }
                username.setText("");
                password.setText("");
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
            }
        });
    }

    public void Creat(){
        username = (EditText) findViewById(R.id.et_user);
        password = (EditText) findViewById(R.id.et_pass);
        btn_regist = (Button) findViewById(R.id.btn_regist);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_reset = (Button) findViewById(R.id.btn_reset);
    }
}
