package com.example.misaya.socketclient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ClientThread implements Runnable{
    private Handler handler;
    public Handler revHandler;
    BufferedReader br = null;
    OutputStream os = null;
    String ip = "192.168.23.2";

    public ClientThread(Handler handler){   //构造函数
        this.handler = handler;
    }

    public void run(){
        try {
            Socket socket = new Socket(ip, 30000);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();          //初始化 输入 输出

            new Thread(){                      //接收 服务器 -> content -> msg -> 客户端
                @Override
                public void run(){
                    String content = null;
                    try{
                        while((content = br.readLine()) != null){
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();

            Looper.prepare();
            revHandler = new Handler() {                    //向 服务器 发送
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0x345){
                        try {
                            String user = msg.getData().getString("user");
                            String pass = msg.getData().getString("pass");
                            String type = msg.getData().getString("type");
                            String post = user + " " + pass + " " + type;
                            os.write((post + "\n").getBytes("utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();

        }catch (SocketTimeoutException e1){
            e1.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
