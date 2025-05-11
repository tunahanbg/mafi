package com.mafi.app.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Veritabanı versiyonu
    private static final int DATABASE_VERSION = 1;

    // Veritabanı adı
    private static final String DATABASE_NAME = "mafi_content.db";

    // Tablo adları
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CONTENTS = "contents";
    public static final String TABLE_CONTENT_TYPES = "content_types";

    // Ortak alan adları
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Users tablosu alan adları
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PROFILE_PICTURE_URL = "profile_picture_url";
    public static final String COLUMN_IS_ACTIVE = "is_active";

    // Contents tablosu alan adları
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CONTENT_TYPE_ID = "content_type_id";
    public static final String COLUMN_CONTENT_URL = "content_url";
    public static final String COLUMN_USER_ID = "user_id";

    // ContentTypes tablosu alan adları
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ICON_RESOURCE = "icon_resource";

    // Tablo oluşturma SQL ifadeleri
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_PROFILE_PICTURE_URL + " TEXT,"
            + COLUMN_CREATED_AT + " INTEGER,"
            + COLUMN_IS_ACTIVE + " INTEGER" + ")";

    private static final String CREATE_TABLE_CONTENTS = "CREATE TABLE " + TABLE_CONTENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_CONTENT_TYPE_ID + " INTEGER,"
            + COLUMN_CONTENT_URL + " TEXT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_CREATED_AT + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_CONTENT_TYPE_ID + ") REFERENCES " + TABLE_CONTENT_TYPES + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" + ")";

    private static final String CREATE_TABLE_CONTENT_TYPES = "CREATE TABLE " + TABLE_CONTENT_TYPES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_ICON_RESOURCE + " TEXT" + ")";

    // Singleton instance
    private static DatabaseHelper instance;

    // Singleton pattern
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabloları oluştur
        db.execSQL(CREATE_TABLE_CONTENT_TYPES);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CONTENTS);

        // Varsayılan içerik tiplerini ekle
        insertDefaultContentTypes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Basit bir güncelleme stratejisi - tabloları sil ve yeniden oluştur
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT_TYPES);
        onCreate(db);
    }

    private void insertDefaultContentTypes(SQLiteDatabase db) {
        // ContentType sabitleri için varsayılan değerleri ekle
        db.execSQL("INSERT INTO " + TABLE_CONTENT_TYPES + "(" + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_ICON_RESOURCE + ") VALUES "
                + "(1, 'Metin', 'ic_text'), "
                + "(2, 'Görüntü', 'ic_image'), "
                + "(3, 'Ses', 'ic_audio'), "
                + "(4, 'Video', 'ic_video')");
    }
}