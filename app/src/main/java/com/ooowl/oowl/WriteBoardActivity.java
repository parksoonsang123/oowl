package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class WriteBoardActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage;

    EditText writetitle;
    EditText writecontents;
    Button completebtn;
    ImageView write_back;

    RadioGroup rg1;
    RadioGroup rg2;
    RadioGroup rg3;
    RadioButton rb0;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;

    EditText address;

    SeekBar sb;
    TextView price;

    RecyclerView recyclerView;
    WriteBoardAdapter adapter;

    private ArrayList<WriteBoardItem> list = new ArrayList<>();
    //imageurilist
    private ArrayList<String> list2 = new ArrayList<>();
    //imagenamelist
    private ArrayList<String> list3 = new ArrayList<>();

    //업로드된 사진 갯수
    private int cnt = 0;
    private int flag = -1;

    long time;
    long imagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);

        Intent intent = getIntent();
        final String postid = intent.getStringExtra("postid");

        sb = findViewById(R.id.seekBar);
        price = findViewById(R.id.seekbar_text);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    price.setText("₩0");
                }
                else{
                    price.setText("₩" + String.valueOf(progress * 10) + "00");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        writetitle = findViewById(R.id.write_title);
        writecontents = findViewById(R.id.write_contents);
        completebtn = findViewById(R.id.write_complete_btn);
        address = findViewById(R.id.address);

        write_back = findViewById(R.id.write_back);
        write_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rg1 = findViewById(R.id.rg1);
        rg2 = findViewById(R.id.rg2);
        rg3 = findViewById(R.id.rg3);


        recyclerView = findViewById(R.id.write_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);


        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select1 = rg1.getCheckedRadioButtonId();
                int select2 = rg2.getCheckedRadioButtonId();
                int select3 = rg3.getCheckedRadioButtonId();

                if(select1 == -1){
                    Toast.makeText(WriteBoardActivity.this, "가격 제안 가능 여부를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(select2 == -1){
                    Toast.makeText(WriteBoardActivity.this, "배송 가능 여부를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(select3 == -1){
                    Toast.makeText(WriteBoardActivity.this, "직거래 가능 여부를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                cnt = 0;
                flag = -1;
                list2.clear();
                list3.clear();

                if(list.size() >= 2){
                    storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com");

                    final ProgressDialog progressDialog = new ProgressDialog(WriteBoardActivity.this, R.style.MyDialogTheme);
                    progressDialog.setTitle("이미지 업로드");
                    progressDialog.show();

                    for(int i=0;i<list.size()-1;i++){
                        imagename = System.currentTimeMillis();
                        list3.add(imagename+"");
                        Uri file = list.get(i).getImageuri();
                        final StorageReference riversRef = storageRef.child("images/"+imagename);
                        UploadTask uploadTask = riversRef.putFile(file);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(WriteBoardActivity.this, "업로드 실패!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                progressDialog.setMessage("업로드 중...");
                            }
                        });

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }

                                return riversRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){

                                    cnt++;
                                    Uri downloadUri = task.getResult();
                                    list2.add(downloadUri.toString());


                                    if(cnt == list.size() - 1){ //이미지파일 업로드 완료 후 동작
                                        progressDialog.dismiss();
                                        postimage();
                                    }
                                }
                                else{
                                    //실패
                                }
                            }
                        });
                    }


                }
                else{
                    Toast.makeText(WriteBoardActivity.this, "사진을 올려주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        if(postid == null){
            list.add(new WriteBoardItem(null, Code.ViewType.SECOND));
            adapter = new WriteBoardAdapter(list, this);
            adapter.setOnPlusClickListener(new WriteBoardAdapter.OnPlusClickListener() {
                @Override
                public void onPlusClick(View v, int position) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0);
                }
            });
            adapter.setOnOtherClickListener(new WriteBoardAdapter.OnOtherClickListener() {
                @Override
                public void onOtherClick(View v, int position) {
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            });

            recyclerView.setAdapter(adapter);
        }
        else{
            databaseReference2 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PostItem item = snapshot.getValue(PostItem.class);

                    writetitle.setText(item.getTitle());
                    writecontents.setText(item.getContents());

                    for(int i=0;i<item.getImageurilist().size();i++){
                        Uri uri = Uri.parse(item.getImageurilist().get(i));
                        list.add(new WriteBoardItem(uri, Code.ViewType.FIRST));
                    }
                    list.add(new WriteBoardItem(null, Code.ViewType.SECOND));
                    adapter = new WriteBoardAdapter(list, getApplicationContext());
                    adapter.setOnPlusClickListener(new WriteBoardAdapter.OnPlusClickListener() {
                        @Override
                        public void onPlusClick(View v, int position) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0);
                        }
                    });
                    adapter.setOnOtherClickListener(new WriteBoardAdapter.OnOtherClickListener() {
                        @Override
                        public void onOtherClick(View v, int position) {
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이미지 선택 시 실행
        if(requestCode == 0 && resultCode == RESULT_OK){
            if(data.getClipData() == null && data.getData() != null){   //이미지 1개 선택
                int lastindex = list.size() - 1;
                if(lastindex != -1){
                    list.remove(lastindex);
                }
                list.add(new WriteBoardItem(data.getData(), Code.ViewType.FIRST));
                list.add(new WriteBoardItem(null, Code.ViewType.SECOND));
                adapter = new WriteBoardAdapter(list, this);
                adapter.setOnPlusClickListener(new WriteBoardAdapter.OnPlusClickListener() {
                    @Override
                    public void onPlusClick(View v, int position) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0);
                    }
                });
                adapter.setOnOtherClickListener(new WriteBoardAdapter.OnOtherClickListener() {
                    @Override
                    public void onOtherClick(View v, int position) {
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });
                recyclerView.setAdapter(adapter);

            }
            else{   //이미지 2개이상 선택
                int lastindex = list.size() - 1;
                if(lastindex != -1){
                    list.remove(lastindex);
                }
                ClipData clipData = data.getClipData();
                for(int i = 0;i<clipData.getItemCount();i++){
                    list.add(new WriteBoardItem(clipData.getItemAt(i).getUri(), Code.ViewType.FIRST));
                }
                list.add(new WriteBoardItem(null, Code.ViewType.SECOND));
                adapter = new WriteBoardAdapter(list, this);
                adapter.setOnPlusClickListener(new WriteBoardAdapter.OnPlusClickListener() {
                    @Override
                    public void onPlusClick(View v, int position) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0);
                    }
                });
                adapter.setOnOtherClickListener(new WriteBoardAdapter.OnOtherClickListener() {
                    @Override
                    public void onOtherClick(View v, int position) {
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

        }
    }

    private void postimage(){

        int select1 = rg1.getCheckedRadioButtonId();
        int select2 = rg2.getCheckedRadioButtonId();
        int select3 = rg3.getCheckedRadioButtonId();

        rb1 = findViewById(select1);
        rb2 = findViewById(select2);
        rb3 = findViewById(select3);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Post").push();
        String postid2 = databaseReference.getKey();

        time = System.currentTimeMillis();

        HashMap result = new HashMap<>();

        result.put("title", writetitle.getText().toString());
        result.put("contents", writecontents.getText().toString());
        result.put("writetime", makeTimeStamp(time));
        result.put("jjimcnt", "0");
        result.put("postid", postid2);
        //result.put("userid", userid);
        //result.put("nickname", nickname);
        result.put("imageurilist", list2);
        result.put("imagenamelist", list3);
        result.put("transyesno", rb1.getText().toString());
        result.put("tradeyesno", rb2.getText().toString());
        result.put("suggest", rb3.getText().toString());
        result.put("address", address.getText().toString());
        result.put("price", price.getText().toString());


        databaseReference.setValue(result);
        finish();
    }

    private String makeTimeStamp(long in){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(in);
    }
}