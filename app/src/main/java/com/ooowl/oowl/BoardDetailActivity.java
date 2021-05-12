package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardDetailActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String userid;

    DatabaseReference reference;
    DatabaseReference reference2;
    DatabaseReference reference3;
    DatabaseReference reference4;
    DatabaseReference reference5;
    DatabaseReference reference6;
    DatabaseReference reference7;
    DatabaseReference reference8;

    FirebaseStorage storage;

    ImageView back;
    Button remake;
    Button del;
    Button chat;

    TextView nick;
    TextView time;
    TextView jjimcnt;

    TextView trans;
    TextView trade;
    TextView suggest;

    TextView title;
    TextView contents;

    TextView price;

    ViewPager viewPager;

    Button jjim;

    ImageView[] btn = new ImageView[5];

    private ArrayList<UsersItem> Userlist = new ArrayList<>();

    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);


        btn[0] = findViewById(R.id.btn1);
        btn[1] = findViewById(R.id.btn2);
        btn[2] = findViewById(R.id.btn3);
        btn[3] = findViewById(R.id.btn4);
        btn[4] = findViewById(R.id.btn5);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        final String postid = intent.getStringExtra("pid");
        final String postuserid = intent.getStringExtra("uid");

        nick = findViewById(R.id.detail_nick);
        time = findViewById(R.id.detail_time);
        jjimcnt = findViewById(R.id.detail_jjimcnt);

        trans = findViewById(R.id.transyesno);
        trade = findViewById(R.id.tradeyesno);
        suggest = findViewById(R.id.suggest);

        title = findViewById(R.id.detail_title);
        contents = findViewById(R.id.detail_content);

        price = findViewById(R.id.detail_price);

        back = findViewById(R.id.detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        remake = findViewById(R.id.detail_remake);
        del = findViewById(R.id.detail_del);
        chat = findViewById(R.id.chat);

        if(!userid.equals(postuserid)){
            remake.setVisibility(View.GONE);
            del.setVisibility(View.GONE);
        }
        else{
            chat.setVisibility(View.GONE);
        }


        profile = findViewById(R.id.your_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1;
                if(!postuserid.equals(userid)) {
                    intent1 = new Intent(BoardDetailActivity.this, YourGallery.class);
                    intent1.putExtra("your_id", postuserid);
                    startActivity(intent1);
                }
                else{   //마이 갤러리 프래그먼트로 이동




                }
            }
        });
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(postuserid);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                if(item.getProfileuri() != null){
                    Glide.with(BoardDetailActivity.this).load(item.getProfileuri()).into(profile);
                }
                else{
                    profile.setImageResource(R.drawable.mypageimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int flag = -1;
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            ChatListItem item = snapshot1.getValue(ChatListItem.class);
                            if(item.getMyid().equals(userid) && item.getPostid().equals(postid)){   //이미 채팅중
                                flag = 1;
                                break;
                            }
                        }

                        if(flag == -1){

                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ChatList").push();
                            final String chatid = reference1.getKey();

                            HashMap result = new HashMap<>();

                            result.put("chatid", chatid);
                            result.put("postid", postid);
                            result.put("sellid", postuserid);
                            result.put("myid", userid);

                            reference1.setValue(result);

                            Intent intent1 = new Intent(BoardDetailActivity.this, ChattingActivity.class);
                            intent1.putExtra("postid", postid);
                            intent1.putExtra("chatid", chatid);
                            intent1.putExtra("sellid", postuserid);
                            intent1.putExtra("myid", userid);
                            intent1.putExtra("where", "1");
                            startActivity(intent1);
                            finish();
                        }
                        else{
                            Toast.makeText(BoardDetailActivity.this, "이미 채팅 중 입니다!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        remake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference8 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
                reference8.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostItem item = snapshot.getValue(PostItem.class);
                        Intent intent1 = new Intent(BoardDetailActivity.this, WriteBoardActivity.class);
                        intent1.putExtra("postid", postid);
                        startActivity(intent1);
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setMessage("삭제하시겠습니까?");

                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                imagedelete(postid);


                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });


        jjim = findViewById(R.id.jjim);
        jjim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2 = FirebaseDatabase.getInstance().getReference("JJim").child(userid).child(postid);
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        JJimItem item = snapshot.getValue(JJimItem.class);

                        if(item == null){//찜한적없음
                            HashMap result = new HashMap<>();
                            result.put("press", "1");
                            reference2.setValue(result);

                            jjim.setBackgroundResource(R.drawable.heart);
                            jjimplus(postid);
                        }
                        else{
                            reference2.removeValue();

                            jjim.setBackgroundResource(R.drawable.heart2);
                            jjimminus(postid);

                            /*if(item.getPress().equals("1")){
                                HashMap result = new HashMap<>();
                                result.put("press", "0");
                                reference2.setValue(result);

                                jjim.setBackgroundResource(R.drawable.heart2);
                                jjimminus(postid);
                            }
                            else{
                                HashMap result = new HashMap<>();
                                result.put("press", "1");
                                reference2.setValue(result);

                                jjim.setBackgroundResource(R.drawable.heart);
                                jjimplus(postid);
                            }*/
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("JJim").child(userid).child(postid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JJimItem item = snapshot.getValue(JJimItem.class);
                if(item == null){
                    jjim.setBackgroundResource(R.drawable.heart2);
                }
                else{
                    jjim.setBackgroundResource(R.drawable.heart);
                    /*if(item.getPress().equals("1")){
                        jjim.setBackgroundResource(R.drawable.heart);
                    }
                    else{
                        jjim.setBackgroundResource(R.drawable.heart2);
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                nick.setText(item.getNickname());

                Long t = Long.parseLong(item.getWritetime());
                time.setText(formatTimeString(t));

                jjimcnt.setText(item.getJjimcnt());

                if(item.getTransyesno().equals("YES")){
                    trans.setTextColor(Color.GREEN);
                }
                else{
                    trans.setTextColor(Color.RED);
                }

                if(item.getTradeyesno().equals("YES")){
                    trade.setTextColor(Color.GREEN);
                }
                else{
                    trade.setTextColor(Color.RED);
                }

                if(item.getSuggest().equals("YES")){
                    suggest.setTextColor(Color.GREEN);
                }
                else{
                    suggest.setTextColor(Color.RED);
                }

                trans.setText(item.getTransyesno());
                trade.setText(item.getTradeyesno());
                suggest.setText(item.getSuggest());

                title.setText(item.getTitle());
                contents.setText(item.getContents());

                String ppp = "";
                String p = item.getPrice();
                String pp = p.replace("₩", "");
                if(pp.length() == 4){
                    ppp = pp.substring(0,1)+","+pp.substring(1,pp.length()) + "원";
                }
                else if(pp.length() == 5){
                    ppp = pp.substring(0,2)+","+pp.substring(2,pp.length()) + "원";
                }
                price.setText(ppp);


                viewPager = findViewById(R.id.detail_viewpager);
                ImageFragmentAdapter fragmentAdapter = new ImageFragmentAdapter(getSupportFragmentManager());

                final int size = item.getImageurilist().size();

                if(size == 1){
                    btn[0].setVisibility(View.GONE);
                    btn[1].setVisibility(View.GONE);
                    btn[2].setVisibility(View.GONE);
                    btn[3].setVisibility(View.GONE);
                    btn[4].setVisibility(View.GONE);
                }
                else if(size == 2){
                    btn[0].setVisibility(View.VISIBLE);
                    btn[1].setVisibility(View.VISIBLE);
                    btn[2].setVisibility(View.GONE);
                    btn[3].setVisibility(View.GONE);
                    btn[4].setVisibility(View.GONE);
                }
                else if(size == 3){
                    btn[0].setVisibility(View.VISIBLE);
                    btn[1].setVisibility(View.VISIBLE);
                    btn[2].setVisibility(View.VISIBLE);
                    btn[3].setVisibility(View.GONE);
                    btn[4].setVisibility(View.GONE);
                }
                else if(size == 4){
                    btn[0].setVisibility(View.VISIBLE);
                    btn[1].setVisibility(View.VISIBLE);
                    btn[2].setVisibility(View.VISIBLE);
                    btn[3].setVisibility(View.VISIBLE);
                    btn[4].setVisibility(View.GONE);
                }
                else if(size == 5){
                    btn[0].setVisibility(View.VISIBLE);
                    btn[1].setVisibility(View.VISIBLE);
                    btn[2].setVisibility(View.VISIBLE);
                    btn[3].setVisibility(View.VISIBLE);
                    btn[4].setVisibility(View.VISIBLE);
                }

                ImageFragment[] imageFragment = new ImageFragment[5];
                for(int i=0;i<size;i++){
                    imageFragment[i] = new ImageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("imgRes", item.getImageurilist().get(i));
                    imageFragment[i].setArguments(bundle);
                    fragmentAdapter.addItem(imageFragment[i]);
                }
                fragmentAdapter.notifyDataSetChanged();

                viewPager.setAdapter(fragmentAdapter);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for(int i=0;i<size;i++){
                            if(i != position){
                                btn[i].setBackgroundResource(R.drawable.btn2);
                            }
                            else{
                                btn[i].setBackgroundResource(R.drawable.btn1);
                            }
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void jjimplus(String postid){

        String jc = jjimcnt.getText().toString();
        int jcc = Integer.parseInt(jc) + 1;
        String jc2 = jcc+"";
        jjimcnt.setText(jc2);

        reference4 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                String jjimcnt = item.getJjimcnt();
                int cnt = Integer.parseInt(jjimcnt) + 1;
                String jjimcnt2 = cnt + "";
                item.setJjimcnt(jjimcnt2);
                reference4.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void jjimminus(String postid){

        String jc = jjimcnt.getText().toString();
        int jcc = Integer.parseInt(jc) - 1;
        String jc2 = jcc+"";
        jjimcnt.setText(jc2);

        reference4 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                String jjimcnt = item.getJjimcnt();
                int cnt = Integer.parseInt(jjimcnt) - 1;
                String jjimcnt2 = cnt + "";
                item.setJjimcnt(jjimcnt2);
                reference4.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void imagedelete(final String postid){
        reference3 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                for(int i=0;i<item.getImagenamelist().size();i++){
                    storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("images/"+item.getImagenamelist().get(i));
                    storageRef.delete();
                }
                jjimdelete(postid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void jjimdelete(final String postid){
        Userlist.clear();

        reference3 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final PostItem item = snapshot.getValue(PostItem.class);
                if(!item.getJjimcnt().equals("0")){
                    reference5 = FirebaseDatabase.getInstance().getReference("Users");
                    reference5.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Userlist.add(snapshot1.getValue(UsersItem.class));
                            }

                            for(int i=0; i<Userlist.size(); i++){
                                reference6 = FirebaseDatabase.getInstance().getReference("JJim").child(Userlist.get(i).getIdToken()).child(postid);
                                if(reference6 != null){
                                    reference6.removeValue();
                                }
                            }

                            postdelete(postid);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    postdelete(postid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void postdelete(String postid){
        reference7 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference7.removeValue();
        finish();
    }

    public String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg = "";
        if (diffTime < BoardAdapter.TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= BoardAdapter.TIME_MAXIMUM.SEC) < BoardAdapter.TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= BoardAdapter.TIME_MAXIMUM.MIN) < BoardAdapter.TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= BoardAdapter.TIME_MAXIMUM.HOUR) < BoardAdapter.TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= BoardAdapter.TIME_MAXIMUM.DAY) < BoardAdapter.TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

}



















