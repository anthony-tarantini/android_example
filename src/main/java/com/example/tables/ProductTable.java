package com.example.tables;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.models.Product;
import com.example.providers.DatabaseTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductTable extends DatabaseTable {
    public static final String TABLE_NAME = "product";

    public interface Columns extends DatabaseTable.Columns{
        String NAME = "name";
        String ORIGIN = "origin";
        String IMAGE_URL = "image_url";
    }

    @Override
    protected Map<String, String> getColumnTypes() {
        Map<String, String> columnTypes = super.getColumnTypes();
        columnTypes.put(Columns.NAME, "TEXT");
        columnTypes.put(Columns.ORIGIN, "TEXT");
        columnTypes.put(Columns.IMAGE_URL, "TEXT");
        return columnTypes;
    }

    public static ContentValues[] toContentValues(final List<Product> products) {
        final ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (final Product product : products) {
            contentValues.add(toContentValues(product));
        }
        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    public static ContentValues toContentValues(final Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.NAME, product.getName());
        contentValues.put(Columns.ORIGIN, product.getOrigin());
        contentValues.put(Columns.IMAGE_URL, product.getImageUrl());
        return contentValues;
    }

    public static Product fromCursor(final Cursor cursor) {
        return Product.newBuilder()
                .name(cursor.getString(cursor.getColumnIndex(Columns.NAME)))
                .origin(cursor.getString(cursor.getColumnIndex(Columns.ORIGIN)))
                .imageUrl(cursor.getString(cursor.getColumnIndex(Columns.IMAGE_URL)))
                .build();
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    protected String getConstraint() {
        return "UNIQUE (" + Columns.NAME + ") ON CONFLICT REPLACE";
    }
}
