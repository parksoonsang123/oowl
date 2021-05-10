package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class YourGallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mg_id;
    private String yourid;
    private TextView num_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_gallery);
        Intent intent = getIntent();
        yourid = intent.getStringExtra("your_id");
        mg_id = findViewById(R.id.your_id);
        mg_id.setText(yourid);
        num_f = findViewById(R.id.num_follower);

        recyclerView = findViewById(R.id.your_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostItem postItem = snapshot.getValue(PostItem.class);
                    if(postItem.getPostid().equals(yourid)){
                        for(int i=0;i<postItem.getImageurilist().size();i++){
                            arrayList.add(postItem.getImageurilist().get(i));
                        }
                        mg_id.setText(postItem.getNickname());
                        num_f.setText(postItem.getJjimcnt());
                        break;
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
}