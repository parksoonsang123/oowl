package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BoardFragment boardFragment;
    ChatFragment chatFragment;
    MyGalleryFragment myGalleryFragment;

    TextView main_tv;

    Button search_btn;
    Button setting_btn;

    DrawerLayout drawerLayout;
    LinearLayout menu1;
    LinearLayout menu2;
    LinearLayout menu3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //알림설정 기능






                //drawerLayout 집어넣기
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 기능





                //drawerLayout 집어넣기
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원탈퇴 기능





                //drawerLayout 집어넣기
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        main_tv = findViewById(R.id.main_text);
        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                else{
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
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
                        //toolbar.setVisibility(View.GONE);
                        setting_btn.setVisibility(View.GONE);
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
                        setting_btn.setVisibility(View.GONE);
                        search_btn.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.main_frame_layout, chatFragment)
                                .commitAllowingStateLoss();
                        return true;

                    case R.id.tab3:
                        main_tv.setText("마이 갤러리");
                        setting_btn.setVisibility(View.VISIBLE);
                        setting_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                                    drawerLayout.openDrawer(Gravity.RIGHT);
                                }
                                else{
                                    drawerLayout.openDrawer(Gravity.RIGHT);
                                }
                            }
                        });
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            super.onBackPressed();
        }
    }

}