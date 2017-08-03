package com.example.gareth.speakitvisualcommunication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gareth on 31/07/2017.
 */

public class SentenceBuilderAdapter extends RecyclerView.Adapter<SentenceBuilderAdapter.MyViewHolder> {

    private List<PecsImages> imagesList;

    /**
     *
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        /**
         *
         * @param view
         */
        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.wordText3);
            imageView = (ImageView)view.findViewById(R.id.imageview3);


        }
    }

    /**
     *
     * @param imagesList
     */
    public SentenceBuilderAdapter(List<PecsImages> imagesList) {
        this.imagesList = imagesList;
    }


    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SentenceBuilderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_linear_layout_sentence_builder, parent, false);

        return new MyViewHolder(itemView);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(SentenceBuilderAdapter.MyViewHolder holder, int position) {
        final PecsImages images = imagesList.get(position);
        holder.textView.setText(images.getWord());
        if(images.getNumber() == 1) {
            holder.imageView.setImageResource(images.getImage());
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(images.getImages(), 0, images.getImages().length);
            holder.imageView.setImageBitmap(bmp);

        }


    }


    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return imagesList.size();
    }


}
