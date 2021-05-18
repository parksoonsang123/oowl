package com.ooowl.oowl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BoardDetailViewPagerAdapter extends PagerAdapter {

    private Context mContext = null;
    private ArrayList<String> list = new ArrayList<>();
    private String postid;

    public BoardDetailViewPagerAdapter(Context mContext, ArrayList<String> list, String postid) {
        this.mContext = mContext;
        this.list = list;
        this.postid = postid;
    }
    public BoardDetailViewPagerAdapter() {}

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = null ;

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_image, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.vp_img) ;
            Glide.with(mContext).load(list.get(position)).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), OnlyImage.class);
                    intent.putExtra("position", position+"");
                    intent.putExtra("postid", postid);
                    v.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
