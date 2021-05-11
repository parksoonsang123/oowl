package com.ooowl.oowl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JjimAdapter extends RecyclerView.Adapter<JjimAdapter.ViewHolder>{

    private ArrayList<String> arrayList;
    private Context context;

    public JjimAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public JjimAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jjim_list,parent,false);
        JjimAdapter.ViewHolder holder = new JjimAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final JjimAdapter.ViewHolder holder, final int position) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(arrayList.get(position));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem postItem = snapshot.getValue(PostItem.class);

                holder.title.setText(postItem.getTitle());
                holder.nickname.setText(postItem.getNickname());

                Long t = Long.parseLong(postItem.getWritetime());
                holder.time.setText(formatTimeString(t));

                String ppp = "";
                String p = postItem.getPrice();
                String pp = p.replace("₩", "");
                if(pp.length() == 4){
                    ppp = pp.substring(0,1)+","+pp.substring(1,pp.length()) + "원";
                }
                else if(pp.length() == 5){
                    ppp = pp.substring(0,2)+","+pp.substring(2,pp.length()) + "원";
                }
                holder.price.setText(ppp);
                holder.jjimcnt.setText(postItem.getJjimcnt()+"");

                GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
                holder.image.setBackground(drawable);
                holder.image.setClipToOutline(true);
                Glide.with(context).load(postItem.getImageurilist().get(0)).into(holder.image);
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

        ImageView image;
        TextView title;
        TextView nickname;
        TextView price;
        TextView jjimcnt;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nickname = itemView.findViewById(R.id.owner_nick);
            this.title = itemView.findViewById(R.id.j_title);
            this.image = itemView.findViewById(R.id.j_image);
            this.price = itemView.findViewById(R.id.j_price);
            this.jjimcnt = itemView.findViewById(R.id.j_jjimcnt);
            this.time = itemView.findViewById(R.id.jjim_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int pos = getAdapterPosition();

                    final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Post").child(arrayList.get(pos));
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            PostItem postItem = snapshot.getValue(PostItem.class);
                            Intent intent = new Intent(v.getContext(), BoardDetailActivity.class);
                            intent.putExtra("pid",postItem.getPostid());
                            intent.putExtra("uid", postItem.getUserid());
                            v.getContext().startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

        }
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
