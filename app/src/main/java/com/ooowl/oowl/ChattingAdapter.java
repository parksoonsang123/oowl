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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof FirstViewHolder){
            ((FirstViewHolder) holder).contents.setText(mDataList.get(position).getContents());
        }
        else{
            ((SecondViewHolder) holder).contents.setText(mDataList.get(position).getContents());
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
        public SecondViewHolder(@NonNull View itemView) {
            super(itemView);
            contents = itemView.findViewById(R.id.chatting_content2);

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
