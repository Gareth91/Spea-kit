package com.example.gareth.speakitvisualcommunication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gareth on 07/08/2017.
 */

public class UserAdapter extends BaseAdapter {

    /**
     * The context
     */
    private final Context mContext;

    /**
     * A list of all the pecs images objects
     */
    private final List<User> images;

    /**
     * This constructor is used to instantiate a ImageAdapter.
     */
    public UserAdapter(Context context, List<User> images) {
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
        final User image = images.get(position);
        final ImageView imageView;
        final TextView wordView;

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.linear_layout_user, null);
                imageView = (ImageView) convertView.findViewById(R.id.userImage);
                wordView = (TextView) convertView.findViewById(R.id.user);

            final UserAdapter.ViewHolder viewHolder = new ViewHolder(imageView, wordView);
            convertView.setTag(viewHolder);
        }

        final UserAdapter.ViewHolder viewHolder = (UserAdapter.ViewHolder)convertView.getTag();
        viewHolder.textView.setText(image.getUserName());
        byte[] pecsImage = image.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(pecsImage, 0, pecsImage.length);
        viewHolder.imageView.setImageBitmap(bitmap);

        return convertView;
    }

    /**
     *
     */
    private class ViewHolder {

        private final ImageView imageView;
        private final TextView textView;

        public ViewHolder(ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
        }
    }

}
