package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OnlyImage extends AppCompatActivity {

    ViewPager viewPager;
    OnlyImageViewPagerAdapter viewPagerAdapter;
    private List<String> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_image);

        Intent intent = getIntent();
        String postid = intent.getStringExtra("postid");
        final String pos = intent.getStringExtra("position");

        viewPager = findViewById(R.id.onlyimage_vp);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem item = snapshot.getValue(PostItem.class);
                mDataList = item.getImageurilist();
                viewPagerAdapter = new OnlyImageViewPagerAdapter(OnlyImage.this, mDataList);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(Integer.parseInt(pos));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}