package com.netkoin.app.custom_views.autocompleteview;

import com.netkoin.app.R;
import com.netkoin.app.custom_views.autocompleteview.annotations.ViewId;

/**
 * Created by siddharth on 1/16/2017.
 */
public class Place {
    private static final String PLACE_IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=%s&sensor=false&key=AIzaSyDhFGUWlyd0KsjPQ59ATr-yL0bQKujHmeg";
    private String name;
    private String photoReference;
    private String place_id;

    @ViewId(id = R.id.name)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    @ViewId(id = R.id.image)
    public String getImageUrl() {
        String url = String.format(PLACE_IMAGE_URL, photoReference);
        return url;
    }


    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
