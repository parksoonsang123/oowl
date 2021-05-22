package com.ooowl.oowl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder>{
    private ArrayList<String> arrayList;
    private Context context;


    public FollowAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list,parent,false);
        FollowAdapter.ViewHolder holder = new FollowAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(arrayList.get(position));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersItem usersItem = snapshot.getValue(UsersItem.class);
                holder.nickname.setText(usersItem.getNickname());
                holder.follow_cnt.setText(usersItem.getFollower());

                if(usersItem.getProfileuri() != null){
                    //java.lang.IllegalArgumentException: You cannot start a load for a destroyed activi
                    Glide.with(context).load(usersItem.getProfileuri()).into(holder.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        TextView follow_cnt;
        CircleImageView profile;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userid = mAuth.getCurrentUser().getUid();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nickname = itemView.findViewById(R.id.f_nick);
            this.follow_cnt = itemView.findViewById(R.id.num_yourfer);
            this.profile = itemView.findViewById(R.id.f_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    Intent intent1;
                    if(!arrayList.get(pos).equals(userid)) {
                        intent1 = new Intent(view.getContext(), YourGallery.class);
                        intent1.putExtra("your_id",arrayList.get(pos));
                        view.getContext().startActivity(intent1);
                    }
                    else{   //마이 갤러리 프래그먼트로 이동

                    }

                }
            });
        }
    }
}
