package com.ooowl.oowl;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MyGalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mg_id;
    private TextView num_jjim;
    private TextView num_follower;
    private TextView num_following;
    private LinearLayout btn_fer;
    private LinearLayout btn_fing;
    private LinearLayout btn_jjim;

    CircleImageView profile;

    String userid;

    private FirebaseStorage storage;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;
    private StorageTask uploadTask;

    int flag = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_my_gallery, container, false);

        recyclerView = view.findViewById(R.id.mg_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(view.getContext(),3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        userid = mAuth.getUid();
        mg_id = view.findViewById(R.id.mg_id);
        num_jjim = view.findViewById(R.id.num_jjim);

        num_follower = view.findViewById(R.id.num_follower);
        num_following = view.findViewById(R.id.num_following);

        profile = view.findViewById(R.id.profile);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostItem postItem = snapshot.getValue(PostItem.class);
                    if(postItem.getUserid().equals(userid)){
                        for(int i=0;i<postItem.getImageurilist().size();i++){
                            arrayList.add(postItem.getImageurilist().get(i));
                        }
                        mg_id.setText(postItem.getNickname());
                    }
                }

                adapter = new GalleryAdapter(arrayList, view.getContext());

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //내가 팔로잉
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                num_following.setText(item.getFollowing());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //나를 팔로잉
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                num_follower.setText(item.getFollower());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //찜
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("JJim").child(userid);
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt = 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1 != null){
                        cnt++;
                    }
                }
                num_jjim.setText(cnt+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //프로필
        DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                if(item.getProfileuri() == null){
                    profile.setImageResource(R.drawable.mypageimage);
                }
                else{
                    Glide.with(getContext()).load(item.getProfileuri()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        btn_fer = view.findViewById(R.id.btn_fer);
        btn_fer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(), FollowerMenu.class);
                intent1.putExtra("userid",userid);
                startActivity(intent1);
            }
        });
        btn_fing = view.findViewById(R.id.btn_fing);
        btn_fing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(), FollowingMenu.class);
                intent1.putExtra("userid",userid);
                startActivity(intent1);
            }
        });

        btn_jjim=view.findViewById(R.id.btn_jjim);
        btn_jjim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(),JjimMenu.class);
                intent1.putExtra("userid",userid);
                startActivity(intent1);
            }
        });



        //프로필 클릭 이벤트
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomSheetDialog(v);
            }
        });

        return view;
    }

    private void setBottomSheetDialog(View view){

        View v;
        BottomSheetBehavior mBehavior;

        final BottomSheetDialog normal_dialog = new BottomSheetDialog(view.getContext(),R.style.BottomSheetDialogTheme);
        v  = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_profile,
                (LinearLayout)view.findViewById(R.id.bottomSheetContainer));

        LinearLayout layout1 = v.findViewById(R.id.bs1);
        LinearLayout layout2 = v.findViewById(R.id.bs2);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
                normal_dialog.dismiss();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //프로필 사진 삭제
                final DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                reference6.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersItem item = snapshot.getValue(UsersItem.class);
                        if(item.getProfileuri() != null){
                            String tmp = item.getProfileimagename();
                            StorageReference reference5 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("profile").child(tmp);
                            reference5.delete();

                            item.setProfileuri(null);
                            item.setProfileimagename(null);
                            reference6.setValue(item);
                            normal_dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        normal_dialog.setContentView(v);

        mBehavior = BottomSheetBehavior.from((View)v.getParent());
        mBehavior.setPeekHeight(4000);
        normal_dialog.show();

    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.MyDialogTheme);
        progressDialog.setTitle("이미지 업로드");
        progressDialog.show();

        flag = -1;
        if(imageuri != null){
            storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://oowl-d90e9.appspot.com");

            final String profileimagename = System.currentTimeMillis()+"";

            Uri file = imageuri;
            final StorageReference riversRef = storageRef.child("profile/" + profileimagename);
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    flag = 1;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
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
                    if(task.isSuccessful() && flag == 1){
                        Uri downloadUri = task.getResult();
                        profileupdate(downloadUri.toString(), profileimagename);
                        progressDialog.dismiss();
                    }
                }
            });

        }
        else{
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void profileupdate(final String downloadUri, final String profileimagename){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem item = snapshot.getValue(UsersItem.class);
                item.setProfileimagename(profileimagename);
                item.setProfileuri(downloadUri);
                reference.setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageuri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }
            else{
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersItem item = snapshot.getValue(UsersItem.class);
                        if(item.getProfileuri() == null){
                            uploadImage();
                        }
                        else{
                            StorageReference reference5 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://oowl-d90e9.appspot.com").child("profile").child(item.getProfileimagename());
                            reference5.delete();
                            uploadImage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }
}