package com.example.smart_car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        tcp_Client_INI();
        //右转方向键按下
        final Button btn_r=(Button)findViewById(R.id.btn_r);
        btn_r.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"R");
                // TODO Auto-generated method stub
                int action = event.getAction();
                   if (action == MotionEvent.ACTION_DOWN) {
               // 按下 处理相关逻辑

                   } else if (action == MotionEvent.ACTION_UP) {
                    // 松开 todo 处理相关逻辑  

                       }
                return false;
            }
        });
        //左转按钮
        final Button btn_l=(Button)findViewById(R.id.btn_l);
        btn_l.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"L");

                return false;
            }
        });
        //停止按钮
        final Button btn_stop=(Button)findViewById(R.id.btn_stop);
        btn_stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"S");

                return false;
            }
        });
        //前进按钮
        final Button btn_up=(Button)findViewById(R.id.btn_up);
        btn_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"U");

                return false;
            }
        });
        //后退按钮
        final Button btn_down=(Button)findViewById(R.id.btn_down);
        btn_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"D");

                return false;
            }
        });
        //持续后退按钮
        final Button btn_down_long=(Button)findViewById(R.id.btn_down_long);
        btn_down_long.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"DL");

                return false;
            }
        });
        //持续前进按钮
        final Button btn_up_long=(Button)findViewById(R.id.btn_up_long);
        btn_up_long.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event,"UL");

                return false;
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (Exception e) {

        }
    }
    //按钮按下修改状态显示文本
    protected void btn_touch(MotionEvent event, String keycode){
        TextView btn_car=(TextView)findViewById(R.id.car_btn);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            send_tcp(keycode);
            // 按下 处理相关逻辑
            switch (keycode){
                case "U":
                    btn_car.setText("前进");
                    break;
                case "D":
                    btn_car.setText("后退");
                    break;
                case "L":
                    btn_car.setText("左转");
                    break;
                case "R":
                    btn_car.setText("右转");
                    break;
                case "UL":
                    btn_car.setText("持续前进");
                    break;
                case "DL":
                    btn_car.setText("持续后退");
                    break;
                case "S":
                    btn_car.setText("停止");
                    break;
                default:
                    break;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            // 松开 todo 处理相关逻辑  
            btn_car.setText("未按键");

            switch (keycode){
                case "UL":
                    break;
                case "DL":
                    break;
                default:
                    send_tcp("S");
                    break;
            }
        }
    }

    //向TCP服务器发送数据
    protected void send_tcp(String send_str) {
        try {
            if (socket!=null&&socket.isConnected()) {
                //通过客户端的套接字对象Socket方法，获取字节输出流，将数据写向服务器
                OutputStream out = socket.getOutputStream();
                out.write(send_str.getBytes());
            }else {
                TextView car_tcp=(TextView)findViewById(R.id.car_tcp);
                car_tcp.setText("连接已经关闭");
            }
        }
        catch (Exception ex){
            TextView car_tcp=(TextView)findViewById(R.id.car_tcp);
            car_tcp.setText("失败:"+ex.getMessage());
        }
    }
  Socket socket;
    //初始化TCP连接
    protected void tcp_Client_INI() {

        try { //创建Socket对象，连接服务器
            Socket socket = new Socket("192.168.43.229", 8080);
            if(socket!=null&&socket.isConnected()) {
                receive_tcp();
            }
        } catch (Exception e) {
            TextView car_tcp=(TextView)findViewById(R.id.car_tcp);
            car_tcp.setText("初始化失败:"+e.getMessage());
        }
    }
    //接收TCP信息
    protected void receive_tcp(){
        //创建一个新线程
        new Thread(){
            @Override
            public void run(){
                try{
                    while (socket.isConnected()){
                        //读取服务器发回的数据，使用socket套接字对象中的字节输入流
                        InputStream in=socket.getInputStream();
                        byte[] data=new byte[1024];
                        int len=in.read(data);
                        String re_str=new String(data,0,len);

                        Message message = handler.obtainMessage();
                        message.what=100;
                        message.obj = re_str;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){

                    Message message = handler.obtainMessage();
                    message.what=100;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }

        }.start();

    }
    //收到的消息代码转化为消息实时显示
    protected void car_status(String tcp_redata){
        String msg="";
        switch (tcp_redata){
            case "S":
                msg="停止";
                break;
            case "L":
                msg="左转";
                break;
            case "R":
                msg="右转";
                break;
            case "D":
                msg="后退";
                break;
            case "U":
                msg="前进";
                break;
        }
        if(msg!=""){
            Message message = handler.obtainMessage();
            message.what=101;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }
    //一个消息管道
    Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==100){//TCP接收线程的消息
                TextView tv=(TextView)findViewById(R.id.car_rec);//收到的消息
                tv.setText(msg.obj.toString());
            }else if(msg.what==101){
                TextView tv=(TextView)findViewById(R.id.car_go);//行驶状态
                tv.setText(msg.obj.toString());
            }
        }
    };
}
