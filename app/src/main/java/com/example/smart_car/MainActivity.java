package com.example.smart_car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn_car=(Button)findViewById(R.id.btn_car);
        btn_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text_state=(TextView)findViewById(R.id.text_state);
                text_state.setText("点击了小车按钮！");
                //Intent是一种运行时绑定（run-time binding）机制，它能在程序运行过程中连接两个不同的组件。

                Intent intent = new Intent(MainActivity.this , CarActivity.class);
////启动
                startActivity(intent);
            }
        });
        final Button btn_air=(Button)findViewById(R.id.btn_air);
        btn_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text_state=(TextView)findViewById(R.id.text_state);
                text_state.setText("点击了飞机按钮！");
            }
        });
    }
}
