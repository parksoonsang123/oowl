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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
    LinearLayout menu4;

    LinearLayout menu11;
    LinearLayout menu12;

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
        menu4 = findViewById(R.id.menu4);

        menu12 = findViewById(R.id.menu12);
        menu11 = findViewById(R.id.menu11);

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
                AlertDialog.Builder signout = new AlertDialog.Builder(MainActivity2.this, R.style.MyDialogTheme);
                signout.setTitle("회원 탈퇴");
                signout.setMessage("탈퇴 하시겠습니까..?");
                signout.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish = -1;
                        String userid = mFirebaseAuth.getCurrentUser().getUid();
                        jjimdelete(userid);
                    }
                });
                signout.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                signout.show();

                //drawerLayout 집어넣기
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //문의하기





                //drawerLayout 집어넣기
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        menu11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임 변경하기

                EditDialog editDialog = new EditDialog(MainActivity2.this, mFirebaseAuth.getCurrentUser().getUid());
                editDialog.show();

                //drawerLayout 집어넣기
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        menu12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //비밀번호 변경하기

                EditDialog2 editDialog2 = new EditDialog2(MainActivity2.this, mFirebaseAuth.getCurrentUser().getUid());
                editDialog2.show();

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


    public void jjimdelete(final String userid){
        final ArrayList<String> postlist = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("JJim").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String item = snapshot1.getKey();
                    postlist.add(item);
                }
                int i=0;
                for(i=0;i<postlist.size();i++){
                    jjimminus(postlist.get(i), i, postlist.size()-1, userid);
                }

                /*if(i == postlist.size()){
                    reference.removeValue();
                    followdelete(userid);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void jjimminus(String postid, final int cur, final int max, final String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                int jcnt = Integer.parseInt(item.getJjimcnt())-1;
                item.setJjimcnt(jcnt+"");
                reference.setValue(item);

                if(cur == max){
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("JJim").child(userid);
                    reference2.removeValue();
                    followdelete(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void followdelete(final String userid){
        final ArrayList<String> followlist = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String item = snapshot1.getKey();
                    followlist.add(item);
                }
                int i=0;
                for(i=0;i<followlist.size();i++){
                    youfollowerminus(followlist.get(i), i, followlist.size()-1, userid);
                }

                /*if(i == followlist.size()){
                    reference.removeValue();
                    followerdelete(userid);
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void followdelete2(String userid, String youid, int cur, int max){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(youid).child(userid);
        reference.removeValue();

        if(cur == max){
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Follower").child(userid);
            reference2.removeValue();
            followerdelete2(userid);
        }
    }
    public void youfollowerminus(final String userid, final int cur, final int max, final String userid2){
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                int fnum = Integer.parseInt(item.getFollower()) - 1;
                item.setFollower(fnum+"");
                reference1.setValue(item);

                if(cur == max){
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Follow").child(userid2);
                    reference2.removeValue();
                    followerdelete(userid2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void followerdelete3(final String userid, String userid2, int cur, int max){
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Follower").child(userid2).child(userid);
        reference2.removeValue();

        if(cur == max){
            chatlistdelete(userid);
        }
    }
    public void followerdelete2(final String userid){
        final ArrayList<String> followinglist = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follower");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String item = snapshot1.getKey();
                    followinglist.add(item);
                }

                for(int i=0;i<followinglist.size();i++){
                    followerdelete3(userid, followinglist.get(i), i, followinglist.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void followerdelete(final String userid){
        final ArrayList<String> followerlist = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follower").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String item = snapshot1.getKey();
                    followerlist.add(item);
                }

                int i=0;
                for(i=0;i<followerlist.size();i++){
                    youfollowingminus(followerlist.get(i), userid, i, followerlist.size()-1, followerlist);
                    //followdelete2(userid, followerlist.get(i));
                }

                /*if(i == followerlist.size()){
                    reference.removeValue();
                    chatlistdelete(userid);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void youfollowingminus(final String userid, final String userid2, final int cur, final int max, final ArrayList<String> followerlist){
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                int fnum = Integer.parseInt(item.getFollowing()) - 1;
                item.setFollowing(fnum+"");
                reference1.setValue(item);

                if(cur == max){
                    for(int i=0;i<followerlist.size();i++){
                        followdelete2(userid2, followerlist.get(i), i, followerlist.size()-1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void chatlistdelete(final String userid){
        final ArrayList<String> chatlist = new ArrayList<>();
        final ArrayList<String> postlist = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ChatListItem item = snapshot1.getValue(ChatListItem.class);
                    if(item.getMyid().equals(userid) || item.getSellid().equals(userid)){
                        chatlist.add(item.getChatid());
                        postlist.add(item.getPostid());
                    }
                }

                int i=0;
                for(i=0;i<chatlist.size();i++){
                    chatlistdelete2(chatlist.get(i), i, chatlist.size()-1, chatlist, userid, postlist);
                    //chatdelete(chatlist.get(i));
                }

                /*if(i == chatlist.size()){
                    postdelete(userid);
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void chatlistdelete2(String chatid, int cur, int max, ArrayList<String> chatlist, String userid, ArrayList<String> postlist){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList").child(chatid);
        reference.removeValue();

        if(cur == max){
            for(int i=0;i<chatlist.size();i++){
                chatdelete(chatlist.get(i), i, chatlist.size()-1, userid, postlist.get(i));
            }
        }
    }
    public void chatdelete(String chatid, int cur, int max, String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat").child(postid).child(chatid);
        reference.removeValue();

        if(cur == max){
            postdelete(userid);
        }
    }

    public void jjimdelete2(final String postid, final int cur, final int max, final ArrayList<String> postlist, final String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("JJim");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String item = snapshot1.getKey();
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("JJim").child(item).child(postid);
                    reference2.removeValue();
                }

                if(cur == max){
                    for(int j=0;j<postlist.size();j++){
                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Post").child(postlist.get(j));
                        reference3.removeValue();
                    }
                    UserTokenListdelete(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void postdelete(final String userid){
        final ArrayList<String> postlist = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    PostItem item = snapshot1.getValue(PostItem.class);
                    if(item.getUserid().equals(userid)){
                        for(int i=0;i<item.getImagenamelist().size();i++){
                            //imagedelete(item.getImagenamelist().get(i));
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("images/"+item.getImagenamelist().get(i));
                            storageRef.delete();
                        }
                        postlist.add(item.getPostid());
                    }
                }
                int i=0;
                for(i=0;i<postlist.size();i++){
                    jjimdelete2(postlist.get(i), i, postlist.size()-1, postlist, userid);
                }

                /*if(i == postlist.size()){
                    int j = 0;
                    for(j=0;j<postlist.size();j++){
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Post").child(postlist.get(j));
                        reference2.removeValue();
                    }

                    if(j == postlist.size()){
                        UserTokenListdelete(userid);
                    }

                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void imagedelete(String imagename){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("images/"+imagename);
        storageRef.delete();
    }

    public void UserTokenListdelete(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserTokenList").child(userid);
        reference.removeValue();

        Usersdelete(userid);
    }

    public void Usersdelete(String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                if(item.getProfileimagename() != null){
                    //imagedelete(item.getProfileimagename());
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("images/"+item.getProfileimagename());
                    storageRef.delete();

                    reference.removeValue();
                    lastdelete();
                }
                else{
                    reference.removeValue();
                    lastdelete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void lastdelete(){
        MySharedPreferences.setPref(getApplicationContext(), "", "", false);
        mFirebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.exit(0);
                }
            }
        });
    }
}