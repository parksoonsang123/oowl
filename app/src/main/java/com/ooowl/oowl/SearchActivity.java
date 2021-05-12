package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    ImageView back;
    EditText editText;

    BoardAdapter adapter;
    private ArrayList<PostItem> list = new ArrayList<>();

    DatabaseReference reference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userId;
    String searching;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userId = mAuth.getCurrentUser().getUid();

        editText = findViewById(R.id.search_edit);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imageView = findViewById(R.id.search_image);

        back = findViewById(R.id.search_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.search_result);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case 0:
                        searching = editText.getText().toString();
                        if(searching.length() < 1){
                            Toast.makeText(SearchActivity.this, "한 글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(R.drawable.search3);
                            list.clear();
                            adapter = new BoardAdapter(getApplicationContext(), list);
                            recyclerView.setAdapter(adapter);
                            return true;
                        }

                        reference = FirebaseDatabase.getInstance().getReference("Post");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    PostItem item1 = snapshot1.getValue(PostItem.class);
                                    if(item1.getContents().contains(searching) || item1.getTitle().contains(searching)){
                                        list.add(item1);
                                    }
                                }

                                if(list.size() == 0){
                                    imageView.setImageResource(R.drawable.nofind);
                                    adapter = new BoardAdapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);
                                }
                                else{
                                    imageView.setImageResource(0);
                                    Collections.sort(list, new Ascending());

                                    adapter = new BoardAdapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);
                                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

    }

    class Ascending implements Comparator<PostItem> {

        @Override
        public int compare(PostItem o1, PostItem o2) {
            return o2.getWritetime().compareTo(o1.getWritetime());
        }

    }

}