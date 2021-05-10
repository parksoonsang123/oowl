package com.ooowl.oowl;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyGalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mg_id;
    private TextView jjimcnt;
    private TextView num_follower;
    private TextView num_following;

    String userid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_my_gallery, container, false);

        recyclerView = view.findViewById(R.id.mg_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(view.getContext(),3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        userid = mAuth.getUid();
        mg_id = view.findViewById(R.id.mg_id);
        jjimcnt = view.findViewById(R.id.num_jjim);

        num_follower = view.findViewById(R.id.num_follower);
        num_following = view.findViewById(R.id.num_following);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostItem postItem = snapshot.getValue(PostItem.class);
                    if(postItem.getUserid().equals(userid)){
                        for(int i=0;i<postItem.getImageurilist().size();i++){
                            arrayList.add(postItem.getImageurilist().get(i));
                        }
                        mg_id.setText(postItem.getNickname());
                        jjimcnt.setText(postItem.getJjimcnt());
                    }
                }

                adapter = new GalleryAdapter(arrayList, view.getContext());

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //내가 팔로잉
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Follow").child(userid);
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt = 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1 != null){
                        cnt++;
                    }
                }
                num_following.setText(cnt+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //나를 팔로잉
        DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("Follower").child(userid);
        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt = 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1 != null){
                        cnt++;
                    }
                }
                num_follower.setText(cnt+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }




}