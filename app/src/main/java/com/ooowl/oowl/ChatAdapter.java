package com.ooowl.oowl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    FirebaseAuth auth;
    String my;

    private Context context = null;
    private List<ChatListItem> mDataList;
    private ArrayList<ChattingItem2> list = new ArrayList<>();

    public ChatAdapter(Context context, List<ChatListItem> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.chatlist_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        auth = FirebaseAuth.getInstance();
        my = auth.getUid();

        if(mDataList.get(position).getSellid().equals(my)){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mDataList.get(position).getMyid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UsersItem item = snapshot.getValue(UsersItem.class);
                    holder.nickname.setText(item.getNickname());

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chat").child(mDataList.get(position).getPostid()).child(mDataList.get(position).getChatid());
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                ChattingItem2 item2 = snapshot1.getValue(ChattingItem2.class);
                                list.add(item2);
                            }

                            Collections.sort(list, new Ascending());

                            holder.contents.setText(list.get(0).getContents());

                            Long t = Long.parseLong(list.get(0).getTime());
                            holder.time.setText(formatTimeString(t));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mDataList.get(position).getSellid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UsersItem item = snapshot.getValue(UsersItem.class);
                    holder.nickname.setText(item.getNickname());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView nickname;
    TextView time;
    TextView contents;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile);
            nickname = itemView.findViewById(R.id.sell_nick);
            time = itemView.findViewById(R.id.chattime);
            contents = itemView.findViewById(R.id.contents);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ChattingActivity.class);
                    intent.putExtra("postid", mDataList.get(pos).getPostid());
                    intent.putExtra("sellid", mDataList.get(pos).getSellid());
                    intent.putExtra("myid", mDataList.get(pos).getMyid());
                    intent.putExtra("chatid", mDataList.get(pos).getChatid());
                    intent.putExtra("where", "2");
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    class Ascending implements Comparator<ChattingItem2> {

        @Override
        public int compare(ChattingItem2 o1, ChattingItem2 o2) {
            return o2.getTime().compareTo(o1.getTime());
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
