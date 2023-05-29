package com.example.myfleet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyFleet.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ACCOUNT = "account";
    private static final String COL_ID = "id";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_BIRTH_DATE = "birth_date";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_ACCOUNT + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_FULL_NAME + " TEXT,"
            + COL_BIRTH_DATE + " TEXT,"
            + COL_PHONE + " TEXT,"
            + COL_EMAIL + " TEXT,"
            + COL_PASSWORD + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }

    public long insertAccount(String fullName, String birthDate, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar se já existe um registro com o mesmo e-mail e telefone
        Cursor emailPhoneCursor = db.query(TABLE_ACCOUNT, new String[]{COL_EMAIL, COL_PHONE},
                COL_EMAIL + " = ? AND " + COL_PHONE + " = ?", new String[]{email, phone}, null, null, null);
        if (emailPhoneCursor.getCount() > 0) {
            emailPhoneCursor.close();
            db.close();
            return -2; // Retornar um valor indicando que o e-mail e telefone já estão em uso
        }
        emailPhoneCursor.close();

        // Verificar se já existe um registro com o mesmo e-mail
        Cursor emailCursor = db.query(TABLE_ACCOUNT, new String[]{COL_EMAIL}, COL_EMAIL + " = ?", new String[]{email}, null, null, null);
        if (emailCursor.getCount() > 0) {
            emailCursor.close();
            db.close();
            return -3; // Retornar um valor indicando que o e-mail já está em uso
        }
        emailCursor.close();

        // Verificar se já existe um registro com o mesmo telefone
        Cursor phoneCursor = db.query(TABLE_ACCOUNT, new String[]{COL_PHONE}, COL_PHONE + " = ?", new String[]{phone}, null, null, null);
        if (phoneCursor.getCount() > 0) {
            phoneCursor.close();
            db.close();
            return -4; // Retornar um valor indicando que o telefone já está em uso
        }
        phoneCursor.close();



        ContentValues values = new ContentValues();
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_BIRTH_DATE, birthDate);
        values.put(COL_PHONE, phone);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        long rowId = db.insert(TABLE_ACCOUNT, null, values);
        db.close();
        return rowId;
    }

}
