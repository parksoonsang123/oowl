package com.ooowl.oowl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;

    public GalleryAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list,parent,false);
        ViewHolder holder = new ViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.mg_image.getLayoutParams();
        layoutParams.height = layoutParams.width;
        holder.mg_image.requestLayout();*/

        Glide.with(context)
                .load(arrayList.get(position))
                .into(holder.mg_image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mg_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mg_image = itemView.findViewById(R.id.mg_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int pos = getAdapterPosition();
                    final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Post");
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                PostItem postItem1 = snapshot1.getValue(PostItem.class);
                                if(("["+arrayList.get(pos)+"]").equals(postItem1.getImageurilist().toString())) {
                                    Intent intent = new Intent(v.getContext(), BoardDetailActivity.class);
                                    intent.putExtra("pid", postItem1.getPostid());
                                    intent.putExtra("uid", postItem1.getUserid());
                                    v.getContext().startActivity(intent);
                                    break;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

        }
    }
}
