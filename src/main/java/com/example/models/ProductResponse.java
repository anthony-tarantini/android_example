package com.example.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {
    @SerializedName("status")
    String mStatus;

    @SerializedName("messgae")
    String mMessage;

    @SerializedName("result")
    List<Product> mResult;

    private ProductResponse(Builder builder) {
        mStatus = builder.mStatus;
        mMessage = builder.mMessage;
        mResult = builder.mResult;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List<Product> getResult() {
        return mResult;
    }


    public static final class Builder {
        private String mStatus;
        private String mMessage;
        private List<Product> mResult;

        private Builder() {
        }

        public Builder status(String mStatus) {
            this.mStatus = mStatus;
            return this;
        }

        public Builder message(String mMessage) {
            this.mMessage = mMessage;
            return this;
        }

        public Builder result(List<Product> mResult) {
            this.mResult = mResult;
            return this;
        }

        public ProductResponse build() {
            return new ProductResponse(this);
        }
    }
}
