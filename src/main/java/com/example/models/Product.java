package com.example.models;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("name")
    protected String mName;
    @SerializedName("origin")
    protected String mOrigin;
    @SerializedName("image_url")
    protected String mImageUrl;

    public Product(){}

    private Product(Builder builder) {
        mName = builder.mName;
        mOrigin = builder.mOrigin;
        mImageUrl = builder.mImageUrl;
    }

    public String getName() {
        return mName;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String mName;
        private String mOrigin;
        private String mImageUrl;

        private Builder() {
        }

        public Builder name(String mName) {
            this.mName = mName;
            return this;
        }

        public Builder origin(String mOrigin) {
            this.mOrigin = mOrigin;
            return this;
        }

        public Builder imageUrl(String mImageUrl) {
            this.mImageUrl = mImageUrl;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
