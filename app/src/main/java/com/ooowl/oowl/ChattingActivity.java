package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ooowl.oowl.Notification.APIService;
import com.ooowl.oowl.Notification.Client;
import com.ooowl.oowl.Notification.MyResponse;
import com.ooowl.oowl.Notification.NotificationData;
import com.ooowl.oowl.Notification.SendData;
import com.ooowl.oowl.Notification.Token;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        final PostItem item = snapshot.getValue(PostItem.class);
                        final String ct = input.getText().toString();

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

                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatList").child(chatid);
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ChatListItem item1 = snapshot.getValue(ChatListItem.class);

                                    String s1 = item1.getMyid();
                                    String s2 = item1.getSellid();

                                    if(s1.equals(userid)){
                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("UserTokenList").child(s2);
                                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                final Token item2 = snapshot.getValue(Token.class);

                                                //채팅보냈을때 알림
                                                Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                                                        apiService.sendNotification(new NotificationData(new SendData(ct, postid, chatid, userid), item2.getToken()))
                                                                .enqueue(new Callback<MyResponse>() {
                                                                    @Override
                                                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                        if(response.code() == 200){
                                                                            if(response.body().success == 1){
                                                                                Log.e("Notification", "success");
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                                                    }
                                                                });
                                                    }
                                                };

                                                Thread tr = new Thread(runnable);
                                                tr.start();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else if(s2.equals(userid)){
                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("UserTokenList").child(s1);
                                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                final Token item2 = snapshot.getValue(Token.class);

                                                //채팅보냈을때 알림
                                                Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                                                        apiService.sendNotification(new NotificationData(new SendData(ct, postid, chatid, userid), item2.getToken()))
                                                                .enqueue(new Callback<MyResponse>() {
                                                                    @Override
                                                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                        if(response.code() == 200){
                                                                            if(response.body().success == 1){
                                                                                Log.e("Notification", "success");
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                                                    }
                                                                });
                                                    }
                                                };


                                                Thread tr = new Thread(runnable);
                                                tr.start();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
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
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
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
        else{   //채팅목록, 알림
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
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private String makeTimeStamp(long in){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(in);
    }
}