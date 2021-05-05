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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    FirebaseAuth auth;
    String my;

    private Context context = null;
    private List<ChatListItem> mDataList;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();
        my = auth.getUid();

        if(mDataList.get(position).getSellid().equals(my)){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mDataList.get(position).getMyid());
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
}
