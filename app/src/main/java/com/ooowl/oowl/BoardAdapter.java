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

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private Context context = null;
    private List<PostItem> mDataList;

    public BoardAdapter(Context context, List<PostItem> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.board_item1, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item1 = mDataList.get(position);
        holder.title.setText(item1.getTitle());
        holder.nickname.setText(item1.getNickname());


        Long t = Long.parseLong(item1.getWritetime());
        holder.time.setText(formatTimeString(t));

        String ppp = "";
        String p = item1.getPrice();
        String pp = p.replace("₩", "");
        if(pp.length() == 4){
            ppp = pp.substring(0,1)+","+pp.substring(1,pp.length()) + "원";
        }
        else if(pp.length() == 5){
            ppp = pp.substring(0,2)+","+pp.substring(2,pp.length()) + "원";
        }
        holder.price.setText(ppp);
        holder.jjimcnt.setText(item1.getJjimcnt()+"");

        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
        holder.image.setBackground(drawable);
        holder.image.setClipToOutline(true);
        Glide.with(context).load(mDataList.get(position).getImageurilist().get(0)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView nickname;
        TextView time;
        TextView price;
        TextView jjimcnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.board_image);
            title = itemView.findViewById(R.id.board_title);
            nickname = itemView.findViewById(R.id.board_nick);
            time = itemView.findViewById(R.id.board_time);
            price = itemView.findViewById(R.id.board_price);
            jjimcnt = itemView.findViewById(R.id.board_jjimcnt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), BoardDetailActivity.class);
                    intent.putExtra("pid", mDataList.get(pos).getPostid());
                    intent.putExtra("uid", mDataList.get(pos).getUserid());
                    intent.putExtra("pos", pos);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }


    public static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg = "";
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
}
