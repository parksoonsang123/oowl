package com.ooowl.oowl;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BoardFragment extends Fragment {

    DatabaseReference reference;

    private RecyclerView boardRecyclerView;
    private ArrayList<PostItem> list = new ArrayList<>();
    private BoardAdapter adapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userId;

    FirebaseStorage storage;

    Button btn1;
    Button btn2;
    Button btn22;
    Button btn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_board, container, false);

        btn1 = view.findViewById(R.id.seq_new);
        btn2 = view.findViewById(R.id.seq_price);
        btn22 = view.findViewById(R.id.seq_price2);
        btn3 = view.findViewById(R.id.seq_jjim);

        FloatingActionButton fab = view.findViewById(R.id.board_floatingactionbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), WriteBoardActivity.class);
                startActivity(intent);
            }

        });

        boardRecyclerView = view.findViewById(R.id.board_recyclerview1);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(manager);

        reference = FirebaseDatabase.getInstance().getReference("Post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    PostItem item1 = snapshot1.getValue(PostItem.class);
                    list.add(item1);
                }

                if(btn1.getCurrentTextColor() == Color.WHITE){
                    Collections.sort(list, new Ascending());
                }
                else if(btn2.getCurrentTextColor() == Color.WHITE){
                    Collections.sort(list, new Ascending2());
                }
                else if(btn22.getCurrentTextColor() == Color.WHITE){
                    Collections.sort(list, new Ascending22());
                }
                else if(btn3.getCurrentTextColor() == Color.WHITE){
                    Collections.sort(list, new Ascending3());
                }

                adapter = new BoardAdapter(view.getContext(), list);
                boardRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //정렬
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn1.getCurrentTextColor() == Color.WHITE){
                    btn1.setBackgroundResource(R.drawable.info_btn2);
                    btn1.setTextColor(Color.WHITE);
                    btn2.setBackgroundResource(R.drawable.info_btn1);
                    btn2.setTextColor(Color.BLACK);
                    btn22.setBackgroundResource(R.drawable.info_btn1);
                    btn22.setTextColor(Color.BLACK);
                    btn3.setBackgroundResource(R.drawable.info_btn1);
                    btn3.setTextColor(Color.BLACK);
                }
                else{
                    btn1.setBackgroundResource(R.drawable.info_btn2);
                    btn1.setTextColor(Color.WHITE);
                    btn2.setBackgroundResource(R.drawable.info_btn1);
                    btn2.setTextColor(Color.BLACK);
                    btn22.setBackgroundResource(R.drawable.info_btn1);
                    btn22.setTextColor(Color.BLACK);
                    btn3.setBackgroundResource(R.drawable.info_btn1);
                    btn3.setTextColor(Color.BLACK);

                    Collections.sort(list, new Ascending());
                    adapter = new BoardAdapter(view.getContext(), list);
                    boardRecyclerView.setAdapter(adapter);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2.getCurrentTextColor() == Color.WHITE){
                    btn2.setBackgroundResource(R.drawable.info_btn2);
                    btn2.setTextColor(Color.WHITE);
                    btn1.setBackgroundResource(R.drawable.info_btn1);
                    btn1.setTextColor(Color.BLACK);
                    btn22.setBackgroundResource(R.drawable.info_btn1);
                    btn22.setTextColor(Color.BLACK);
                    btn3.setBackgroundResource(R.drawable.info_btn1);
                    btn3.setTextColor(Color.BLACK);
                }
                else{
                    btn2.setBackgroundResource(R.drawable.info_btn2);
                    btn2.setTextColor(Color.WHITE);
                    btn1.setBackgroundResource(R.drawable.info_btn1);
                    btn1.setTextColor(Color.BLACK);
                    btn22.setBackgroundResource(R.drawable.info_btn1);
                    btn22.setTextColor(Color.BLACK);
                    btn3.setBackgroundResource(R.drawable.info_btn1);
                    btn3.setTextColor(Color.BLACK);

                    Collections.sort(list, new Ascending2());
                    adapter = new BoardAdapter(view.getContext(), list);
                    boardRecyclerView.setAdapter(adapter);
                }
            }
        });

        btn22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn22.getCurrentTextColor() == Color.WHITE){
                    btn22.setBackgroundResource(R.drawable.info_btn2);
                    btn22.setTextColor(Color.WHITE);
                    btn1.setBackgroundResource(R.drawable.info_btn1);
                    btn1.setTextColor(Color.BLACK);
                    btn2.setBackgroundResource(R.drawable.info_btn1);
                    btn2.setTextColor(Color.BLACK);
                    btn3.setBackgroundResource(R.drawable.info_btn1);
                    btn3.setTextColor(Color.BLACK);
                }
                else{
                    btn22.setBackgroundResource(R.drawable.info_btn2);
                    btn22.setTextColor(Color.WHITE);
                    btn1.setBackgroundResource(R.drawable.info_btn1);
                    btn1.setTextColor(Color.BLACK);
                    btn2.setBackgroundResource(R.drawable.info_btn1);
                    btn2.setTextColor(Color.BLACK);
                    btn3.setBackgroundResource(R.drawable.info_btn1);
                    btn3.setTextColor(Color.BLACK);

                    Collections.sort(list, new Ascending22());
                    adapter = new BoardAdapter(view.getContext(), list);
                    boardRecyclerView.setAdapter(adapter);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn3.getCurrentTextColor() == Color.WHITE){
                    btn3.setBackgroundResource(R.drawable.info_btn2);
                    btn3.setTextColor(Color.WHITE);
                    btn2.setBackgroundResource(R.drawable.info_btn1);
                    btn2.setTextColor(Color.BLACK);
                    btn22.setBackgroundResource(R.drawable.info_btn1);
                    btn22.setTextColor(Color.BLACK);
                    btn1.setBackgroundResource(R.drawable.info_btn1);
                    btn1.setTextColor(Color.BLACK);
                }
                else{
                    btn3.setBackgroundResource(R.drawable.info_btn2);
                    btn3.setTextColor(Color.WHITE);
                    btn2.setBackgroundResource(R.drawable.info_btn1);
                    btn2.setTextColor(Color.BLACK);
                    btn22.setBackgroundResource(R.drawable.info_btn1);
                    btn22.setTextColor(Color.BLACK);
                    btn1.setBackgroundResource(R.drawable.info_btn1);
                    btn1.setTextColor(Color.BLACK);

                    Collections.sort(list, new Ascending3());
                    adapter = new BoardAdapter(view.getContext(), list);
                    boardRecyclerView.setAdapter(adapter);
                }
            }
        });


        return view;
    }

    class Ascending implements Comparator<PostItem> {

        @Override
        public int compare(PostItem o1, PostItem o2) {
            return o2.getWritetime().compareTo(o1.getWritetime());
        }

    }

    class Ascending2 implements Comparator<PostItem> {

        @Override
        public int compare(PostItem o1, PostItem o2) {
            return o1.getPrice().compareTo(o2.getPrice());
        }

    }

    class Ascending22 implements Comparator<PostItem> {

        @Override
        public int compare(PostItem o1, PostItem o2) {
            return o2.getPrice().compareTo(o1.getPrice());
        }

    }

    class Ascending3 implements Comparator<PostItem> {

        @Override
        public int compare(PostItem o1, PostItem o2) {
            return o2.getJjimcnt().compareTo(o1.getJjimcnt());
        }

    }

}