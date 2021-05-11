package com.ooowl.oowl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context = null;
    private List<ChattingItem> mDataList;

    public ChattingAdapter(Context context, List<ChattingItem> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.FIRST){
            view = inflater.inflate(R.layout.chatting_item1, parent, false);
            //return 각각의 뷰홀더
            return new FirstViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.chatting_item2, parent, false);
            //return 각각의 뷰홀더
            return  new SecondViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof FirstViewHolder){
            ((FirstViewHolder) holder).contents.setText(mDataList.get(position).getContents());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mDataList.get(position).getSenderid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UsersItem item = snapshot.getValue(UsersItem.class);
                    if(item.getProfileuri() == null){
                        ((FirstViewHolder) holder).image.setImageResource(R.drawable.mypageimage);
                    }
                    else{
                        Glide.with(context).load(item.getProfileuri()).into(((FirstViewHolder) holder).image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{
            ((SecondViewHolder) holder).contents.setText(mDataList.get(position).getContents());

            if(position == mDataList.size()-1){
                if(mDataList.get(position).isIsseen()){
                    ((SecondViewHolder) holder).txt_seen.setText("Seen");
                }
                else{
                    ((SecondViewHolder) holder).txt_seen.setText("Delivered");
                }
            }
            else{
                ((SecondViewHolder) holder).txt_seen.setVisibility(View.GONE);
            }

        }




    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getViewType();
    }

    public class FirstViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView nickname;
        TextView contents;
        public FirstViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.chatting_profile);
            contents = itemView.findViewById(R.id.chatting_content);

        }
    }


    public class SecondViewHolder extends RecyclerView.ViewHolder{

        TextView contents;
        TextView txt_seen;

        public SecondViewHolder(@NonNull View itemView) {
            super(itemView);
            contents = itemView.findViewById(R.id.chatting_content2);
            txt_seen = itemView.findViewById(R.id.tv_seen);
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
