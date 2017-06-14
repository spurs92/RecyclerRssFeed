package com.spurs.recyclerrssfeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alfo06-11 on 2017-06-14.
 */

public class MyAdapter extends RecyclerView.Adapter {

    ArrayList<Item> items;
    Context context;

    public MyAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        ViewHolder holder=new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyAdapter.ViewHolder h=(MyAdapter.ViewHolder)holder;

        h.tvTitle.setText(items.get(position).title);
        h.tvDesc.setText(items.get(position).desc);
        h.tvDate.setText(items.get(position).date);

        if(items.get(position).imgUrl==null){
            h.img.setVisibility(View.GONE);
        }else {
            h.img.setVisibility(View.VISIBLE);
            Glide.with(context).load(items.get(position).imgUrl).into(h.img);

            //imgURL의 이미지파일을 네트워크로 읽어와서 Bitmap객체로 생성
/*            new Thread(){
                @Override
                public void run() {
                    try {
                        URL url=new URL(items.get(position).imgUrl);

                        InputStream is=url.openStream();

                        Bitmap bm= BitmapFactory.decodeStream(is);

                        h.img.setImageBitmap(bm);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();*/

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvDesc;
        TextView tvDate;
        ImageView img;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvTitle=(TextView) itemView.findViewById(R.id.tv_title);
            tvDesc=(TextView) itemView.findViewById(R.id.tv_desc);
            tvDate=(TextView) itemView.findViewById(R.id.tv_date);
            img=(ImageView) itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //링크주소의 내용을 보여주는 액티비티 실행..
                    String link=items.get(getLayoutPosition()).link;
                    String title=items.get(getLayoutPosition()).title;

                    Intent intent=new Intent(context, ItemDetailActivity.class);
                    intent.putExtra("Title",title);
                    intent.putExtra("Link",link);

                    context.startActivity(intent);
                }
            });
        }
    }
}
