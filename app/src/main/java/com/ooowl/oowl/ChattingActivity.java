package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChattingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChattingAdapter adapter;
    EditText input;
    Button send;

    FirebaseAuth auth;
    String userid;

    private ArrayList<ChattingItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        final String where = intent.getStringExtra("where");
        final String postid = intent.getStringExtra("postid");
        final String sellid = intent.getStringExtra("sellid");
        final String myid = intent.getStringExtra("myid");
        final String chatid = intent.getStringExtra("chatid");

        recyclerView = findViewById(R.id.rv_content);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        input = findViewById(R.id.et_chat);
        send = findViewById(R.id.bt_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(postid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostItem item = snapshot.getValue(PostItem.class);
                        String ct = input.getText().toString();

                        if(ct.equals("") || ct == null){
                            Toast.makeText(ChattingActivity.this, "메세지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chat").child(postid).child(chatid).push();

                            HashMap result = new HashMap<>();

                            result.put("chatid", chatid);
                            result.put("id", userid);
                            result.put("time", chatid);
                            result.put("contents", ct);

                            reference1.setValue(result);

                            input.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        if(where.equals("1")){  //채팅거래
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(postid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PostItem item = snapshot.getValue(PostItem.class);
                    String sellid2 = item.getUserid();

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chat").child(postid).child(chatid);
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                ChattingItem item1 = snapshot1.getValue(ChattingItem.class);
                                if(item1.getId().equals(userid)){
                                    list.add(new ChattingItem(item1.getContents(), item1.getId(), item1.getTime(), item1.getChatid(), Code.ViewType.SECOND));
                                }
                                else{
                                    list.add(new ChattingItem(item1.getContents(), item1.getId(), item1.getTime(), item1.getChatid(), Code.ViewType.FIRST));
                                }

                            }

                            adapter = new ChattingAdapter(getApplicationContext(), list);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else{   //채팅목록
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chat").child(postid).child(chatid);
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        ChattingItem item1 = snapshot1.getValue(ChattingItem.class);
                        if(item1.getId().equals(userid)){
                            list.add(new ChattingItem(item1.getContents(), item1.getId(), item1.getTime(), item1.getChatid(), Code.ViewType.SECOND));
                        }
                        else{
                            list.add(new ChattingItem(item1.getContents(), item1.getId(), item1.getTime(), item1.getChatid(), Code.ViewType.FIRST));
                        }

                    }

                    adapter = new ChattingAdapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}