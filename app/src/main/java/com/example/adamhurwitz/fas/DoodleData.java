package com.example.adamhurwitz.fas;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Object that contains information about each of the Google doodles.
 */
public class DoodleData implements Parcelable {
    private final String mId;
    private final String mTitle;
    private final String mReleaseDate;
    private final String mDescription;
    private final String mPrice;
    private final String mImageUrl;

    /**
     * Constructor for the DoodleData object.
     * @param id The inventory id of the doodle.
     * @param title The title of the doodle.
     * @param releaseDate When the doodle was released.
     * @param description Summary about the doodle.
     * @param price How much the doodle costs.
     * @param imageUrl The image path to the actual doodle.
     */
    public DoodleData(String id, String title, String releaseDate, String description, String price,
                      String imageUrl) {
        this.mId = id;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mDescription = description;
        this.mPrice = price;
        this.mImageUrl = imageUrl;
    }

    public String getId() { return this.mId; }

    public String getTitle() { return this.mTitle; }

    public String getReleaseDate() { return this.mReleaseDate; }

    public String getDescription() { return this.mDescription; }

    public String getPrice() { return this.mPrice; }

    public String getImageUrl() { return this.mImageUrl; }

    /**
     * Need to override to implement parcelable. Should return a bitmask indicating the set of
     * special object types used by the DoodleData class.
     * @return Returns zero because it is not needed for this implementation.
     */
    public int describeContents() { return 0; }

    /**
     * Writes the content about the object that needs to be stored when creating a parcel.
     * @param dest A parcel object that will transfer data about the doodle.
     * @param flags Additional flags about how the object should be written.
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mDescription);
        dest.writeString(this.mPrice);
        dest.writeString(this.mImageUrl);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of the DoodleData class from a Parcel object.
     */
    public static final Creator<DoodleData> CREATOR = new Creator<DoodleData>() {

        /**
         * Create a new instance of the DoodleData class, instantiating it from the given Parcel
         * whose data had previously been written by the writeToParcel method.
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the DoodleData class.
         */
        public DoodleData createFromParcel(Parcel source) {
            return new DoodleData(source.readString(), source.readString(), source.readString(),
                    source.readString(), source.readString(), source.readString());
        }

        /**
         * Create a new array of the DoodleData class.
         * @param size Size of the array to create.
         * @return Returns an array of the DoodleData class, with every entry initialized to null.
         */
        public DoodleData[] newArray(int size) { return new DoodleData[size]; }
    };
}