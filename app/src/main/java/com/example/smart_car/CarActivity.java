package com.example.smart_car;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class CarActivity extends AppCompatActivity {

    //页面创建时
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        tcp_Client_INI();
        server_information_get();
        //右转方向键按下
        final Button btn_r = (Button) findViewById(R.id.btn_r);
        btn_r.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CR");
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
        final Button btn_l = (Button) findViewById(R.id.btn_l);
        btn_l.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CL");

                return false;
            }
        });
        //停止按钮
        final Button btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CS");

                return false;
            }
        });
        //前进按钮
        final Button btn_up = (Button) findViewById(R.id.btn_up);
        btn_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CU");

                return false;
            }
        });
        //后退按钮
        final Button btn_down = (Button) findViewById(R.id.btn_down);
        btn_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CD");

                return false;
            }
        });
        //持续后退按钮
        final Button btn_down_long = (Button) findViewById(R.id.btn_down_long);
        btn_down_long.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "DL");

                return false;
            }
        });
        //持续前进按钮
        final Button btn_up_long = (Button) findViewById(R.id.btn_up_long);
        btn_up_long.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "UL");

                return false;
            }
        });
        //连接网络按钮点击
        final Button btn_conn = (Button) findViewById(R.id.btn_conn);
        btn_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcp_Client_INI();
            }
        });
        //鸣笛按钮按下
        final Button btn_beep=(Button)findViewById(R.id.btn_beep);
        btn_beep.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "BP");
                return false;
            }
        });
        //灯光按钮按下
        final Button btn_light=(Button)findViewById(R.id.btn_light);
        btn_light.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "LU");
                return false;
            }
        });
        //油门按钮按下
        final Button btn_gas=(Button)findViewById(R.id.btn_gas);
        btn_gas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CA");
                return false;
            }
        });
        //油门按钮按下
        final Button btn_brake=(Button)findViewById(R.id.btn_brake);
        btn_brake.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_touch(event, "CC");
                return false;
            }
        });
    }

    //页面关闭时
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (Exception e) {

        }
    }

    //按钮按下修改状态显示文本
    protected void btn_touch(final MotionEvent event, String keycode) {
        TextView btn_car = (TextView) findViewById(R.id.car_btn);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            // 按下 处理相关逻辑
            switch (keycode) {
                case "CU":
                    btn_car.setText("前进");
                    send_tcp(keycode);
                    break;
                case "CD":
                    btn_car.setText("后退");
                    send_tcp(keycode);
                    break;
                case "CL":
                    btn_car.setText("左转");
                    send_tcp(keycode);
                    break;
                case "CR":
                    btn_car.setText("右转");
                    send_tcp(keycode);
                    break;
                case "UL":
                    keycode="CU";
                    btn_car.setText("持续前进");
                    send_tcp(keycode);
                    break;
                case "DL":
                    keycode="CD";
                    btn_car.setText("持续后退");
                    send_tcp(keycode);
                    break;
                case "CS":
                    btn_car.setText("停止");
                    send_tcp(keycode);
                    break;
                case "BP":
                    btn_car.setText("鸣笛");
                    send_tcp(keycode);
                    break;
                case "LU":
                    btn_car.setText("灯光");
                    if(light_status){
                        keycode="LS";
                    }else {
                        keycode="LU";
                    }
                    send_tcp(keycode);
                    break;
                case "CA":
                    btn_car.setText("油门");
                    gas_status=true;
                    new Thread(){
                        @Override
                        public void run(){
                            while (gas_status){
                                try {
                                    send_tcp("CA");
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                    break;
                case "CC":
                    brake_status=true;
                    btn_car.setText("刹车");
                    new Thread(){
                        @Override
                        public void run(){
                            while (brake_status){
                                try {
                                    send_tcp("CC");
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                    break;
                default:
                    break;
            }

        } else if (action == MotionEvent.ACTION_UP) {
            // 松开 todo 处理相关逻辑  
            btn_car.setText("未按键");

            switch (keycode) {
                case "UL":
                    break;
                case "DL":
                    break;
                case "BP":
                    send_tcp("BS");
                    break;
                case "LU":
                    break;
                case "CA":
                    gas_status=false;
                    break;
                case "CC":
                    brake_status=false;
                    break;
                case "CL":
                    send_tcp("CT");
                    break;
                case "CR":
                    send_tcp("CT");
                    break;
                case "CS":
                    break;
                default:
                    send_tcp("CS");
                    break;
            }
        }
    }

    //向TCP服务器发送数据
    protected void send_tcp(final String send_str) {
        new Thread(){
            @Override
            public void run(){
                try {
                    if (socket != null && socket.isConnected()) {
                        //通过客户端的套接字对象Socket方法，获取字节输出流，将数据写向服务器
                        OutputStream out = socket.getOutputStream();
                        out.write(send_str.getBytes());
                        Message message = handler.obtainMessage();
                        message.what = 102;
                        message.obj = "消息发送成功";
                        handler.sendMessage(message);
                    } else {
                        Message message = handler.obtainMessage();
                        message.what = 102;
                        message.obj = "连接已经关闭";
                        handler.sendMessage(message);
                    }
                } catch (Exception ex) {
                    Message message = handler.obtainMessage();
                    message.what = 102;
                    message.obj = ex.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    //网络连接
    Socket socket;
    //灯光状态
    boolean light_status=false;
    //加速按钮状态
    boolean gas_status=false;
    //减速状态
    boolean brake_status=false;

    //初始化TCP连接
    protected void tcp_Client_INI() {
        try { //创建Socket对象，连接服务器
            TextView ip_tv=(TextView)findViewById(R.id.server_ip);
            TextView port_tv=(TextView)findViewById(R.id.server_port);
            final String ip=ip_tv.getText().toString();
            final int port=Integer.parseInt(port_tv.getText().toString());
            new Thread(){
                @Override
                public void run(){
                    while(socket==null){
                        try {
                            socket=new Socket(ip,port);
                            try {
                                receive_tcp();
                            }catch (Exception ex){
                                Message message = handler.obtainMessage();
                                message.what = 102;//网络状态
                                message.obj = ex.getMessage();
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            Message message = handler.obtainMessage();
                            message.what = 102;//网络状态
                            message.obj = e.getMessage();
                            handler.sendMessage(message);
                        }
                    }
                    Message message = handler.obtainMessage();
                    message.what = 102;//网络状态
                    message.obj = "已经连接";
                    handler.sendMessage(message);
                }
            }.start();


        } catch (Exception e) {
            TextView car_tcp = (TextView) findViewById(R.id.car_tcp);
            car_tcp.setText("初始化失败:" + e.getMessage());
        }
    }

    //接收TCP信息
    protected void receive_tcp() {
        //创建一个新线程
        new Thread() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {
                        //读取服务器发回的数据，使用socket套接字对象中的字节输入流
                        InputStream in = socket.getInputStream();
                        byte[] data = new byte[1024];
                        int len = in.read(data);
                        String re_str = new String(data, 0, len);
                        car_status(re_str);
                        Message message = handler.obtainMessage();
                        message.what = 100;//100是收到的消息
                        message.obj = re_str;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {

                    Message message = handler.obtainMessage();
                    message.what = 102;//102是网络状态
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }

        }.start();

    }

    //收到的消息代码转化为消息实时显示
    protected void car_status(String tcp_redata) {
        String msg = "";
        switch (tcp_redata) {
            case "CS":
                msg = "停止";
                break;
            case "CL":
                msg = "左转";
                break;
            case "CR":
                msg = "右转";
                break;
            case "CD":
                msg = "后退";
                break;
            case "CU":
                msg = "前进";
                break;
            case "LU":{
                Message message = handler.obtainMessage();
                message.what = 103;//灯光状态
                message.obj = "LU";
                handler.sendMessage(message);
            }
                break;
            case "LS": {
                Message message = handler.obtainMessage();
                message.what = 103;//灯光状态
                message.obj = "LS";
                handler.sendMessage(message);
            }
                break;
        }
        if (msg != "") {
            Message message = handler.obtainMessage();
            message.what = 101;//小车行驶状态
            message.obj = msg;
            handler.sendMessage(message);
        }
    }

    //一个消息管道
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {//TCP接收线程的消息
                TextView tv = (TextView) findViewById(R.id.car_rec);//收到的消息
                tv.setText(msg.obj.toString());
            } else if (msg.what == 101) {
                TextView tv = (TextView) findViewById(R.id.car_go);//行驶状态
                tv.setText(msg.obj.toString());
            }else if(msg.what==102){
                TextView tv = (TextView) findViewById(R.id.car_tcp);//网络状态
                tv.setText(msg.obj.toString());
            }else if(msg.what==103){
                Button btl = (Button) findViewById(R.id.btn_light);//灯光状态
                if(msg.obj.toString()=="LU"){
                    btl.setText("关灯");
                    btl.setBackgroundColor(Color.parseColor("#FFD700"));
                    light_status=true;
                }else {
                    btl.setText("开灯");
                    btl.setBackgroundColor(Color.parseColor("#DAA520"));
                    light_status=false;
                }

            }
        }
    };

    //读配置文件
    protected String read_proper(String keycode) {
        Properties prop = new Properties();
        String re_data=null;
        try {
            prop.load(getApplicationContext().getAssets().open("config.properties"));
                re_data = prop.getProperty(keycode);
        } catch (Exception e) {

            re_data = null;
        }
        return re_data;
    }


    //服务器信息获取
    protected void server_information_get(){
        TextView ip_tv=(TextView)findViewById(R.id.server_ip);
        TextView port_tv=(TextView)findViewById(R.id.server_port);
        String ip_str=read_proper("server_ip");
        String port_str=read_proper("server_port");
        if(ip_str!=null){
            ip_tv.setText(ip_str);
        }
        if(port_str!=null){
            port_tv.setText(port_str);
        }
    }

}
