package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BoardFragment boardFragment;
    ChatFragment chatFragment;
    MyGalleryFragment myGalleryFragment;

    TextView main_tv;

    Button search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        main_tv = findViewById(R.id.main_text);
        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        boardFragment = new BoardFragment();
        chatFragment = new ChatFragment();
        myGalleryFragment = new MyGalleryFragment();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_frame_layout, boardFragment)
                .commitAllowingStateLoss();

        bottomNavigationView.setSelectedItemId(R.id.tab1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:
                        main_tv.setText("게시판");
                        search_btn.setVisibility(View.VISIBLE);
                        search_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity2.this, SearchActivity.class);
                                startActivity(intent);
                            }
                        });
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.main_frame_layout, boardFragment)
                                .commitAllowingStateLoss();
                        return true;

                    case R.id.tab2:
                        main_tv.setText("채팅");
                        search_btn.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.main_frame_layout, chatFragment)
                                .commitAllowingStateLoss();
                        return true;

                    case R.id.tab3:
                        main_tv.setText("마이 갤러리");
                        search_btn.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.main_frame_layout, myGalleryFragment)
                                .commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });

    }
}