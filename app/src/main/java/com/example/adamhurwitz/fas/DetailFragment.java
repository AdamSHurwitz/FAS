package com.example.adamhurwitz.fas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private final String LOG_TAG = DetailFragment.class.getSimpleName();

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

            final String[] detailArray = intent.getStringArrayExtra("recylerAdapterExtra");
            //final String[] doodleDataElements = intent.getStringArrayExtra("recylerAdapterExtra");
            // detailArray[0] = imageUrl
            // detailArray[1] = title
            // detailArray[2] = price
            // detailArray[3] = releaseDate
            // detailArray[4] = description
            // detailArray[5] = favorite


            // Create DoodleData Within 'detail_fragment_layout.xml'
            ImageView doodle_image = (ImageView) view.findViewById(R.id.detail_doodle_image);

            final String imageUrl = detailArray[0];
            //final String imageUrl = doodleDataElements[2];

            // generate images with Picasso
            // switch this to getActivity() since must get Context from Activity
            Log.v(LOG_TAG, "onCreateView() - imageUrl " + imageUrl);
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .resize(1200, 500)
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(doodle_image);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView title = (TextView) view.findViewById(R.id.detail_title);
            title.setText(detailArray[1]);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView price = (TextView) view.findViewById(R.id.detail_price);
            price.setText("$" + detailArray[2]);

            //Create MovieData User Release Date Within 'fragment_detail.xml'
            TextView releaseDate = (TextView) view.findViewById(R.id.detail_releasedate);
            releaseDate.setText("Released: " + detailArray[3]);

            //Create MovieData User Rating Within 'fragment_detail.xml'
            TextView about = (TextView) view.findViewById(R.id.detail_description);
            about.setText(detailArray[4]);

            Cursor c = getContext().getContentResolver().query(Contract.ProductData.CONTENT_URI,
                    new String[]{Contract.ProductData.COLUMN_NAME_FAVORITE},
                    Contract.ProductData.COLUMN_NAME_TITLE + "= ?",
                    new String[]{detailArray[1]},
                    Contract.ProductData._ID);
            if (c != null) {
                c.moveToFirst();
                favVal = c.getString(
                        c.getColumnIndexOrThrow(Contract.ProductData.COLUMN_NAME_FAVORITE));
            }

            if (favVal.equals("2")) {
                favoriteButton.setImageResource(R.drawable.favorite_selected);
            } else {
                favoriteButton.setImageResource(R.drawable.favorite_default);
            }


            favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Cursor cursor = getContext().getContentResolver().query(
                            Contract.ProductData.CONTENT_URI,
                            new String[]{Contract.ProductData.COLUMN_NAME_FAVORITE},
                            Contract.ProductData.COLUMN_NAME_TITLE + "= ?",
                            new String[]{detailArray[1]},
                            Contract.ProductData._ID + " DESC");
                    ContentValues values = new ContentValues();

                    if (detailArray[5].equals("1")) {
                        favoriteButton.setImageResource(R.drawable.favorite_selected);
                        cursor.moveToFirst();
                        values.put(Contract.ProductData.COLUMN_NAME_FAVORITE, 2);
                        detailArray[5] = "2";
                    } else {
                        favoriteButton.setImageResource(R.drawable.favorite_default);
                        cursor.moveToFirst();
                        values.put(Contract.ProductData.COLUMN_NAME_FAVORITE, 1);
                        detailArray[5] = "1";
                    }

                    int rowsUpdated;
                    rowsUpdated = getContext().getContentResolver().update(
                            Contract.ProductData.CONTENT_URI, values,
                            Contract.ProductData.COLUMN_NAME_TITLE + "= ?",
                            new String[]{detailArray[1]});
                    //cursor.close();
                }
            });
        }
        return view;
    }

}
