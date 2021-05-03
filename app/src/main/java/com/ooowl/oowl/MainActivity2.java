package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BoardFragment boardFragment;
    ChatFragment chatFragment;
    MyGalleryFragment myGalleryFragment;

    TextView main_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        main_tv = findViewById(R.id.main_text);

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
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.main_frame_layout, boardFragment)
                                .commitAllowingStateLoss();
                        return true;

                    case R.id.tab2:
                        main_tv.setText("채팅");
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.main_frame_layout, chatFragment)
                                .commitAllowingStateLoss();
                        return true;

                    case R.id.tab3:
                        main_tv.setText("마이 갤러리");
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