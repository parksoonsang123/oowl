package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BoardFragment boardFragment;
    ChatFragment chatFragment;
    MyGalleryFragment myGalleryFragment;

    TextView main_tv;

    Button search_btn;
    Button setting_btn;

    DrawerLayout drawerLayout;
    Switch aSwitch;
    LinearLayout menu2;
    LinearLayout menu3;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        drawerLayout = findViewById(R.id.drawer_layout);
        aSwitch = findViewById(R.id.alram_switch);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                if(item.getAlram().equals("1")){
                    aSwitch.setChecked(true);
                }
                else{
                    aSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//체크된 상태 취소시 코드
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UsersItem item = snapshot.getValue(UsersItem.class);
                            item.setAlram("1");
                            reference.setValue(item);
                            //Toast.makeText(MainActivity2.this, "알림을 켰습니다.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{//체크된 상태로 만들시 코드

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UsersItem item = snapshot.getValue(UsersItem.class);
                            item.setAlram("0");
                            reference.setValue(item);
                            //Toast.makeText(MainActivity2.this, "알림을 껐습니다.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 기능
                AlertDialog.Builder dlg_logout = new AlertDialog.Builder(MainActivity2.this, R.style.MyDialogTheme);
                dlg_logout.setTitle("로그아웃"); //제목
                dlg_logout.setMessage("로그아웃 하시겠습니까?"); // 메시지

                dlg_logout.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
                        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UsersItem item = snapshot.getValue(UsersItem.class);
                                item.setLogin("0");
                                reference1.setValue(item);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        MySharedPreferences.setPref(((LoginActivity)LoginActivity.context_login),"","",false);
                        mFirebaseAuth.signOut();
                        finishAffinity();
                        startActivity(new Intent(MainActivity2.this,LoginActivity.class));
                    }
                });
                dlg_logout.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dlg_logout.show();


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