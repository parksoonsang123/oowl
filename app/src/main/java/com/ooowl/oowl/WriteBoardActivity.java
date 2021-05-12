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
    private DatabaseReference databaseReference4;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage;

    EditText writetitle;
    EditText writecontents;
    Button completebtn;
    ImageView write_back;

    RadioGroup rg1;
    RadioGroup rg2;
    RadioGroup rg3;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;

    RadioButton rb11;
    RadioButton rb12;
    RadioButton rb21;
    RadioButton rb22;
    RadioButton rb31;
    RadioButton rb32;


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

    private ArrayList<String> remainlist = new ArrayList<>();

    private ArrayList<Integer> removelist = new ArrayList<>();

    //업로드된 사진 갯수
    private int cnt = 0;

    long time;
    long imagename;

    private static final int PICK_FROM_CAMEFA=0;
    private static final int PICK_FROM_ALBUM=1;
    private static final int CROP_FROM_CAMEFA=2;
    private static final int CROP_FROM_ALBUM=3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);

        rb11 = findViewById(R.id.yes3);
        rb12 = findViewById(R.id.no3);

        rb21 = findViewById(R.id.yes1);
        rb22 = findViewById(R.id.no1);

        rb31 = findViewById(R.id.yes2);
        rb32 = findViewById(R.id.no2);

        Intent intent = getIntent();
        final String postid = intent.getStringExtra("postid");

        sb = findViewById(R.id.seekBar);
        price = findViewById(R.id.seekbar_text);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    price.setText("₩0");
                } else {
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

                if (select1 == -1) {
                    Toast.makeText(WriteBoardActivity.this, "가격 제안 가능 여부를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (select2 == -1) {
                    Toast.makeText(WriteBoardActivity.this, "배송 가능 여부를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (select3 == -1) {
                    Toast.makeText(WriteBoardActivity.this, "직거래 가능 여부를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                cnt = 0;
                list2.clear();
                list3.clear();

                if (postid == null) { //글쓰기
                    if (list.size() >= 2) {
                        storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com");

                        final ProgressDialog progressDialog = new ProgressDialog(WriteBoardActivity.this, R.style.MyDialogTheme);
                        progressDialog.setTitle("이미지 업로드");
                        progressDialog.show();

                        for (int i = 0; i < list.size() - 1; i++) {
                            imagename = System.currentTimeMillis();
                            list3.add(imagename + "");
                            Uri file = list.get(i).getImageuri();
                            final StorageReference riversRef = storageRef.child("images/" + imagename);
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
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    return riversRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {


                                        Uri downloadUri = task.getResult();
                                        list2.add(downloadUri.toString());
                                        cnt++;

                                        if (cnt == list.size() - 1) { //이미지파일 업로드 완료 후 동작
                                            progressDialog.dismiss();
                                            postimage2(postid);
                                        }
                                    } else {
                                        //실패
                                    }
                                }
                            });
                        }


                    } else if(list.size() < 2) {
                        Toast.makeText(WriteBoardActivity.this, "사진을 5개 이하로 올려주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(WriteBoardActivity.this, "사진은 5개 이하로 올려주세요!", Toast.LENGTH_SHORT).show();
                    }
                } else {   //수정하기

                    remainlist.clear();
                    removelist.clear();
                    databaseReference4 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
                    databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final PostItem item = snapshot.getValue(PostItem.class);
                            for (int k = 0; k < item.getImagenamelist().size(); k++) {
                                remainlist.add(item.getImagenamelist().get(k));
                            }
                            if (list.size() >= 2) {   //이미지 파일 있을 때
                                storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com");

                                final ProgressDialog progressDialog = new ProgressDialog(WriteBoardActivity.this, R.style.MyDialogTheme);
                                progressDialog.setTitle("이미지 업로드");
                                progressDialog.show();

                                for (int i = 0; i < list.size() - 1; i++) {
                                    imagename = System.currentTimeMillis();

                                    if (list.get(i).getImageuri().toString().contains("http")) {  //기존 이미지
                                        int index = 0;
                                        for (int j = 0; j < item.getImageurilist().size(); j++) {
                                            if (list.get(i).getImageuri().toString().equals(item.getImageurilist().get(j))) {
                                                index = j;
                                                removelist.add(index);
                                                break;
                                            }
                                        }

                                        list3.add(item.getImagenamelist().get(index));
                                        list2.add(item.getImageurilist().get(index));
                                        cnt++;

                                        if (cnt == list.size() - 1) { //이미지파일 업로드 완료 후 동작
                                            progressDialog.dismiss();
                                            postimage(postid, item.getJjimcnt(), item.getNickname());
                                        }
                                    } else {   //새로운 이미지
                                        list3.add(imagename + "");
                                        Uri file = list.get(i).getImageuri();
                                        //StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                                        final StorageReference riversRef = storageRef.child("images/" + imagename);
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
                                                if (!task.isSuccessful()) {
                                                    throw task.getException();
                                                }

                                                return riversRef.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {

                                                    Uri downloadUri = task.getResult();
                                                    list2.add(downloadUri.toString());
                                                    cnt++;

                                                    if (cnt == list.size() - 1) { //이미지파일 업로드 완료 후 동작
                                                        progressDialog.dismiss();
                                                        postimage(postid, item.getJjimcnt(), item.getNickname());
                                                    }
                                                } else {
                                                    //실패
                                                }
                                            }
                                        });
                                    }
                                }

                            } else if(list.size() < 2) {
                                Toast.makeText(WriteBoardActivity.this, "사진을 5개 이하로 올려주세요!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(WriteBoardActivity.this, "사진은 5개 이하로 올려주세요!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });


        if (postid == null) {
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
        } else {   //수정
            databaseReference2 = FirebaseDatabase.getInstance().getReference("Post").child(postid);
            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PostItem item = snapshot.getValue(PostItem.class);

                    writetitle.setText(item.getTitle());
                    writecontents.setText(item.getContents());
                    address.setText(item.getAddress());

                    if(item.getSuggest().equals("YES")){
                        rb11.setChecked(true);
                    }
                    else{
                        rb12.setChecked(true);
                    }

                    if(item.getTransyesno().equals("YES")){
                        rb21.setChecked(true);
                    }
                    else{
                        rb22.setChecked(true);
                    }

                    if(item.getTradeyesno().equals("YES")){
                        rb31.setChecked(true);
                    }
                    else{
                        rb32.setChecked(true);
                    }

                    String pg = item.getPrice();
                    String pg2 = pg.substring(0, pg.length()-3);
                    int pg3 = Integer.parseInt(pg2);
                    sb.setProgress(pg3);
                    price.setText(pg);

                    for (int i = 0; i < item.getImageurilist().size(); i++) {
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
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data.getClipData() == null && data.getData() != null) {   //이미지 1개 선택
                int lastindex = list.size() - 1;
                if (lastindex != -1) {
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

            } else {   //이미지 2개이상 선택
                int lastindex = list.size() - 1;
                if (lastindex != -1) {
                    list.remove(lastindex);
                }
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
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

    private void postimage2(String postid) {
        final String userid = mAuth.getCurrentUser().getUid();

        int select1 = rg1.getCheckedRadioButtonId();
        int select2 = rg2.getCheckedRadioButtonId();
        int select3 = rg3.getCheckedRadioButtonId();

        rb1 = findViewById(select1);
        rb2 = findViewById(select2);
        rb3 = findViewById(select3);

        database = FirebaseDatabase.getInstance();

        databaseReference2 = database.getReference("Users").child(userid);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);

                databaseReference = database.getReference("Post").push();
                String postid2 = databaseReference.getKey();

                time = System.currentTimeMillis();

                HashMap result = new HashMap<>();

                result.put("title", writetitle.getText().toString());
                result.put("contents", writecontents.getText().toString());
                result.put("writetime", System.currentTimeMillis()+"");
                result.put("jjimcnt", "0");
                result.put("postid", postid2);
                result.put("userid", userid);
                result.put("nickname", item.getNickname());
                result.put("imageurilist", list2);
                result.put("imagenamelist", list3);
                result.put("transyesno", rb1.getText().toString());
                result.put("tradeyesno", rb2.getText().toString());
                result.put("suggest", rb3.getText().toString());
                result.put("address", address.getText().toString());
                String p2 = price.getText().toString().replace("₩","");
                result.put("price", p2);


                databaseReference.setValue(result);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void postimage(String postid, String jjimcnt, String nickname) {

        final String userid = mAuth.getCurrentUser().getUid();

        int select1 = rg1.getCheckedRadioButtonId();
        int select2 = rg2.getCheckedRadioButtonId();
        int select3 = rg3.getCheckedRadioButtonId();

        rb1 = findViewById(select1);
        rb2 = findViewById(select2);
        rb3 = findViewById(select3);

        database = FirebaseDatabase.getInstance();

        if (postid == null) {
            databaseReference2 = database.getReference("Users").child(userid);
            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UsersItem item = snapshot.getValue(UsersItem.class);

                    databaseReference = database.getReference("Post").push();
                    String postid2 = databaseReference.getKey();

                    time = System.currentTimeMillis();

                    HashMap result = new HashMap<>();

                    result.put("title", writetitle.getText().toString());
                    result.put("contents", writecontents.getText().toString());
                    result.put("writetime", System.currentTimeMillis()+"");
                    result.put("jjimcnt", "0");
                    result.put("postid", postid2);
                    result.put("userid", userid);
                    result.put("nickname", item.getNickname());
                    result.put("imageurilist", list2);
                    result.put("imagenamelist", list3);
                    result.put("transyesno", rb1.getText().toString());
                    result.put("tradeyesno", rb2.getText().toString());
                    result.put("suggest", rb3.getText().toString());
                    result.put("address", address.getText().toString());
                    String p2 = price.getText().toString().replace("₩","");
                    result.put("price", p2);


                    databaseReference.setValue(result);
                    finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            databaseReference = database.getReference("Post").child(postid);

            time = System.currentTimeMillis();

            HashMap result = new HashMap<>();

            result.put("title", writetitle.getText().toString());
            result.put("contents", writecontents.getText().toString());
            result.put("writetime", System.currentTimeMillis()+"");
            result.put("jjimcnt", jjimcnt);
            result.put("postid", postid);
            result.put("userid", userid);
            result.put("nickname", nickname);
            result.put("imageurilist", list2);
            result.put("imagenamelist", list3);
            result.put("transyesno", rb1.getText().toString());
            result.put("tradeyesno", rb2.getText().toString());
            result.put("suggest", rb3.getText().toString());
            result.put("address", address.getText().toString());
            String p2 = price.getText().toString().replace("₩","");
            result.put("price", p2);


            databaseReference.setValue(result);
            finish();

        }

        for (int i = 0; i < remainlist.size(); i++) {
            int check = -1;
            for (int j = 0; j < removelist.size(); j++) {
                if (i == removelist.get(j)) {
                    check = 1;
                    break;
                }
            }
            if (check == -1) {
                imagedelete2(remainlist.get(i));
            }
        }


    }

    private void imagedelete2(String imagename) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("images/" + imagename);
        storageRef.delete();
    }

    /*private String makeTimeStamp(long in) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(in);
    }*/



}