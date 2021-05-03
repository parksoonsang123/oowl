package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BoardDetailActivity extends AppCompatActivity {

    DatabaseReference reference;

    ImageView back;
    Button remake;
    Button del;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);



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


        Intent intent = getIntent();
        final String postid = intent.getStringExtra("id");

        reference = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                nick.setText(item.getNickname());
                time.setText(item.getWritetime());
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


                for(int i=0;i<item.getImageurilist().size();i++){
                    ImageFragment imageFragment = new ImageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("imgRes", item.getImageurilist().get(i));
                    imageFragment.setArguments(bundle);
                    fragmentAdapter.addItem(imageFragment);
                }
                fragmentAdapter.notifyDataSetChanged();

                viewPager.setAdapter(fragmentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}