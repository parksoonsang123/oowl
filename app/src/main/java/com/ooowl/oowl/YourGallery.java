package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class YourGallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrayList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mg_id;
    private String yourid;
    private TextView num_follower;
    private TextView num_following;

    Button follow_btn;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_gallery);

        userid = mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        //해당 프로필 uid
        yourid = intent.getStringExtra("your_id");

        mg_id = findViewById(R.id.your_id);
        num_follower = findViewById(R.id.num_follower);
        num_following = findViewById(R.id.num_following);

        //내가 팔로잉
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(yourid);
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                num_following.setText(item.getFollowing());
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
                        for(int i=0;i<postItem.getImageurilist().size();i++){
                            arrayList.add(postItem.getImageurilist().get(i));
                        }
                        mg_id.setText(postItem.getNickname());
                    }
                }

                adapter = new GalleryAdapter(arrayList, getApplicationContext());

                recyclerView.setAdapter(adapter);





            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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