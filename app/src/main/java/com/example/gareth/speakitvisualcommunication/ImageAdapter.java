package com.example.gareth.speakitvisualcommunication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

/**
 * Created by Gareth on 27/07/2017.
 */

public class ImageAdapter extends BaseAdapter {

    /**
     * The context
     */
    private final Context mContext;

    /**
     * A list of all the pecs images objects
     */
    private final List<PecsImages> images;

    /**
     * This constructor is used to instantiate a ImageAdapter.
     */
    public ImageAdapter(Context context, List<PecsImages> images) {
        this.mContext = context;
        this.images = images;
    }

    /**
     * The number of cells to be rendered in the grid view
     * @return an int representing the number of cells to make in grid view
     */
    @Override
    public int getCount() {
        return images.size();
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final PecsImages image = images.get(position);

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.activity_linear_layout_image, null);

            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview);

            final ViewHolder viewHolder = new ViewHolder(imageView);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.imageView.setImageResource(image.getImage());

        return convertView;
    }

    /**
     * The view holder that holds references to each subview
     */
    private class ViewHolder {

        private final ImageView imageView;

        public ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
