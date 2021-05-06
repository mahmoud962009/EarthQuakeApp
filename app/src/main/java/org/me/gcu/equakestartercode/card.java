package org.me.gcu.equakestartercode;

import android.os.Parcel;
import android.os.Parcelable;

public class card implements Parcelable {
    int id;
    String title, description, location, link, pubDate, category;
    double depth, magnitude, latitude, longitude;

    public card() {
    }

    protected card(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        location = in.readString();
        link = in.readString();
        pubDate = in.readString();
        category = in.readString();
        depth = in.readDouble();
        magnitude = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(category);
        dest.writeDouble(depth);
        dest.writeDouble(magnitude);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<card> CREATOR = new Creator<card>() {
        @Override
        public card createFromParcel(Parcel in) {
            return new card(in);
        }

        @Override
        public card[] newArray(int size) {
            return new card[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", category='" + category + '\'' +
                ", depth=" + depth +
                ", magnitude=" + magnitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
