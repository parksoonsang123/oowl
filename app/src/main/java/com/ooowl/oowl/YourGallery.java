package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourGallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrayList2 = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mg_id;
    private String yourid;
    private TextView num_follower;
    private TextView num_following;
    private LinearLayout btn_fer;
    private LinearLayout btn_fing;

    Button follow_btn;
    String userid;

    CircleImageView profile;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_gallery);

        imageView = findViewById(R.id.your_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userid = mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        //해당 프로필 uid
        yourid = intent.getStringExtra("your_id");

        mg_id = findViewById(R.id.your_id);
        num_follower = findViewById(R.id.num_follower);
        num_following = findViewById(R.id.num_following);
        profile = findViewById(R.id.profile);

        //내가 팔로잉 && 프로필
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(yourid);
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                num_following.setText(item.getFollowing());

                if(item.getProfileuri() == null){
                    profile.setImageResource(R.drawable.mypageimage);
                }
                else{
                    //java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
                    Activity activity = YourGallery.this;
                    if(activity.isFinishing()){
                        return;
                    }

                    Glide.with(YourGallery.this).load(item.getProfileuri()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //나를 팔로잉
        DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("Users").child(yourid);
        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                num_follower.setText(item.getFollower());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        follow_btn = findViewById(R.id.apply_f);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Follow").child(userid).child(yourid);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FollowItem item = snapshot.getValue(FollowItem.class);
                if(item == null){
                    follow_btn.setBackgroundResource(R.drawable.follow_btn1);
                    follow_btn.setTextColor(Color.BLACK);
                }
                else{
                    follow_btn.setBackgroundResource(R.drawable.follow_btn2);
                    follow_btn.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!yourid.equals(userid)){
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(userid).child(yourid);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FollowItem item = snapshot.getValue(FollowItem.class);
                            if(item == null){
                                follower_plus(yourid);
                                following_plus(userid);
                                HashMap result = new HashMap<>();
                                result.put("follow", "1");
                                reference.setValue(result);
                                follow_btn.setBackgroundResource(R.drawable.follow_btn2);
                                follow_btn.setTextColor(Color.WHITE);

                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Follower").child(yourid).child(userid);
                                HashMap result2 = new HashMap<>();
                                result2.put("follow", "1");
                                reference1.setValue(result2);

                            }
                            else{
                                follower_minus(yourid);
                                following_minus(userid);
                                reference.removeValue();
                                follow_btn.setBackgroundResource(R.drawable.follow_btn1);
                                follow_btn.setTextColor(Color.BLACK);

                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Follower").child(yourid).child(userid);
                                reference1.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });


        recyclerView = findViewById(R.id.your_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostItem postItem = snapshot.getValue(PostItem.class);
                    if(postItem.getUserid().equals(yourid)){
                        arrayList.add(postItem.getImageurilist().get(0));
                        arrayList2.add(postItem.getPostid());
                        mg_id.setText(postItem.getNickname());
                    }
                }

                adapter = new GalleryAdapter(arrayList, arrayList2, getApplicationContext());

                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_fer = findViewById(R.id.btn_fer);
        btn_fer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(YourGallery.this, FollowerMenu.class);
                intent1.putExtra("userid",yourid);
                startActivity(intent1);
            }
        });
        btn_fing = findViewById(R.id.btn_fing);
        btn_fing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(YourGallery.this, FollowingMenu.class);
                intent1.putExtra("userid",yourid);
                startActivity(intent1);
            }
        });
    }


    private void follower_plus(String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);

                String ff = item.getFollower();
                int ff2 = Integer.parseInt(ff) + 1;
                String ff3 = ff2+"";
                item.setFollower(ff3);

                reference.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void follower_minus(String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);

                String ff = item.getFollower();
                int ff2 = Integer.parseInt(ff) - 1;
                String ff3 = ff2+"";
                item.setFollower(ff3);

                reference.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void following_plus(String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);

                String ff = item.getFollowing();
                int ff2 = Integer.parseInt(ff) + 1;
                String ff3 = ff2+"";
                item.setFollowing(ff3);

                reference.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void following_minus(String userid){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);

                String ff = item.getFollowing();
                int ff2 = Integer.parseInt(ff) - 1;
                String ff3 = ff2+"";
                item.setFollowing(ff3);

                reference.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}