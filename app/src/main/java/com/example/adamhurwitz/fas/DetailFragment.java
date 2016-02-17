package com.example.adamhurwitz.fas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adamhurwitz.fas.data.Contract;
import com.squareup.picasso.Picasso;

/**
 * DetailFragment class implementation.
 * Created by adamhurwitz on 12/5/15.
 */
public class DetailFragment extends Fragment {
    public DetailFragment() {
    }

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_layout, container, false);
        //receive the intent

        final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.favorite_button);
        String favVal = "";

        //Activity has intent, must get intent from Activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {

            final String[] doodleDataElements = intent.getStringArrayExtra("Cursor Doodle Attributes");
            // doodleDataElements[0] = item_id
            // doodleDataElements[1] = title
            // doodleDataElements[2] = image
            // doodleDataElements[3] = description
            // doodleDataElements[4] = price
            // doodleDataElements[5] = release_date

            // Create DoodleData Within 'detail_fragment_layout.xml'
            ImageView doodle_image = (ImageView) view.findViewById(R.id.detail_doodle_image);

            final String imageUrl = doodleDataElements[2];

            // generate images with Picasso
            // switch this to getActivity() since must get Context from Activity
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .resize(1200, 500)
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(doodle_image);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView title = (TextView) view.findViewById(R.id.detail_title);
            title.setText(doodleDataElements[1]);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView price = (TextView) view.findViewById(R.id.detail_price);
            price.setText("$" + doodleDataElements[4]);

            //Create MovieData User Release Date Within 'fragment_detail.xml'
            TextView releaseDate = (TextView) view.findViewById(R.id.detail_releasedate);
            releaseDate.setText("Released: " + doodleDataElements[5]);

            //Create MovieData User Rating Within 'fragment_detail.xml'
            TextView about = (TextView) view.findViewById(R.id.detail_description);
            about.setText(doodleDataElements[3]);

            Cursor c = getContext().getContentResolver().query(Contract.ProductData.CONTENT_URI,
                    new String[]{Contract.ProductData.COLUMN_NAME_FAVORITE},
                    Contract.ProductData.COLUMN_NAME_TITLE + "= ?",
                    new String[]{doodleDataElements[1]},
                    Contract.ProductData._ID);

            if (c != null) {
                c.moveToFirst();
                favVal = c.getString(
                        c.getColumnIndexOrThrow(Contract.ProductData.COLUMN_NAME_FAVORITE));
            }

            if (favVal.equals("2")) {
                favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
            } else {
                favoriteButton.setImageResource(R.drawable.star_default_18dp);
            }

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Cursor cursor = getContext().getContentResolver().query(
                            Contract.ProductData.CONTENT_URI,
                            new String[]{Contract.ProductData.COLUMN_NAME_FAVORITE},
                            Contract.ProductData.COLUMN_NAME_TITLE + "= ?",
                            new String[]{doodleDataElements[1]},
                            Contract.ProductData._ID + " DESC");
                    ContentValues values = new ContentValues();

                    if (doodleDataElements[6].equals("1")) {
                        favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
                        cursor.moveToFirst();
                            values.put(Contract.ProductData.COLUMN_NAME_FAVORITE, 2);
                            doodleDataElements[6] = "2";
                    } else {
                        favoriteButton.setImageResource(R.drawable.star_default_18dp);
                        cursor.moveToFirst();
                            values.put(Contract.ProductData.COLUMN_NAME_FAVORITE, 1);
                            doodleDataElements[6] = "1";
                    }

                    int rowsUpdated;
                    rowsUpdated = getContext().getContentResolver().update(
                            Contract.ProductData.CONTENT_URI, values,
                            Contract.ProductData.COLUMN_NAME_TITLE + "= ?",
                            new String[]{doodleDataElements[1]});
                    //cursor.close();
                }
            });
        }
        return view;
    }

}
