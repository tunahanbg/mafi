package com.mafi.app.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mafi.app.data.local.DatabaseHelper;
import com.mafi.app.data.model.Content;
import com.mafi.app.data.model.ContentType;

import java.util.ArrayList;
import java.util.List;

public class ContentRepository {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public ContentRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Veritabanını açma ve kapama metodları
    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    // İçerik ekleme
    public long insertContent(Content content) {
        open();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, content.getTitle());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, content.getDescription());
        values.put(DatabaseHelper.COLUMN_CONTENT_TYPE_ID, content.getContentTypeId());
        values.put(DatabaseHelper.COLUMN_CONTENT_URL, content.getContentUrl());
        values.put(DatabaseHelper.COLUMN_USER_ID, content.getUserId());
        values.put(DatabaseHelper.COLUMN_CREATED_AT, System.currentTimeMillis());

        long id = database.insert(DatabaseHelper.TABLE_CONTENTS, null, values);
        close();

        return id;
    }

    // İçerik güncelleme
    public int updateContent(Content content) {
        open();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, content.getTitle());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, content.getDescription());
        values.put(DatabaseHelper.COLUMN_CONTENT_TYPE_ID, content.getContentTypeId());
        values.put(DatabaseHelper.COLUMN_CONTENT_URL, content.getContentUrl());

        int rowsAffected = database.update(DatabaseHelper.TABLE_CONTENTS, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(content.getId())});

        close();
        return rowsAffected;
    }

    // İçerik silme
    public int deleteContent(int contentId) {
        open();
        int rowsAffected = database.delete(DatabaseHelper.TABLE_CONTENTS,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(contentId)});
        close();
        return rowsAffected;
    }

    // ID'ye göre içerik getirme
    public Content getContentById(int contentId) {
        open();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CONTENTS,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contentId)},
                null, null, null);

        Content content = null;

        if (cursor != null && cursor.moveToFirst()) {
            content = cursorToContent(cursor);
            cursor.close();
        }

        close();
        return content;
    }

    // Tüm içerikleri getirme
    public List<Content> getAllContents() {
        open();

        List<Content> contentList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CONTENTS,
                null, null, null, null, null,
                DatabaseHelper.COLUMN_CREATED_AT + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                contentList.add(cursorToContent(cursor));
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return contentList;
    }

    // Belirli bir kullanıcının içeriklerini getirme
    public List<Content> getContentsByUserId(int userId) {
        open();

        List<Content> contentList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CONTENTS,
                null,
                DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null,
                DatabaseHelper.COLUMN_CREATED_AT + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                contentList.add(cursorToContent(cursor));
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return contentList;
    }

    // İçerik tipine göre içerikleri getirme
    public List<Content> getContentsByType(int contentTypeId) {
        open();

        List<Content> contentList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CONTENTS,
                null,
                DatabaseHelper.COLUMN_CONTENT_TYPE_ID + " = ?",
                new String[]{String.valueOf(contentTypeId)},
                null, null,
                DatabaseHelper.COLUMN_CREATED_AT + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                contentList.add(cursorToContent(cursor));
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return contentList;
    }

    // Cursor'dan Content nesnesine dönüştürme
    private Content cursorToContent(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
        int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
        int contentTypeIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT_TYPE_ID);
        int contentUrlIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT_URL);
        int userIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID);
        int createdAtIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATED_AT);

        Content content = new Content();

        if (idIndex != -1) content.setId(cursor.getInt(idIndex));
        if (titleIndex != -1) content.setTitle(cursor.getString(titleIndex));
        if (descriptionIndex != -1) content.setDescription(cursor.getString(descriptionIndex));
        if (contentTypeIdIndex != -1) content.setContentTypeId(cursor.getInt(contentTypeIdIndex));
        if (contentUrlIndex != -1) content.setContentUrl(cursor.getString(contentUrlIndex));
        if (userIdIndex != -1) content.setUserId(cursor.getInt(userIdIndex));
        if (createdAtIndex != -1) content.setCreatedAt(cursor.getLong(createdAtIndex));

        return content;
    }
}