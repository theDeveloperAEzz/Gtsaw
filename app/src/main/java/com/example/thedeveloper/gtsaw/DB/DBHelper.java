package com.example.thedeveloper.gtsaw.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thedeveloper.gtsaw.module.UserInformation;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Gtsaw";
    private static final int DATABASE_VERSION = 6;
    //table name
    private static final String TABLE_NAME = "gtsawUsersData";
    //columns name

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
   private static final String KEY_AVATAR = "avatarPath";


    //constractor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_movieDATA_TABLE = " CREATE TABLE " + TABLE_NAME + " ( "  + KEY_NAME + " TEXT, "
                +KEY_EMAIL  + " TEXT ,"+KEY_AVATAR  + " TEXT " + " )";
        db.execSQL(CREATE_movieDATA_TABLE);
    }
    //upgrade DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //inserting data in table
    public void Insert(UserInformation userInformation) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userInformation.getName());
        values.put(KEY_EMAIL, userInformation.getEmail());
        values.put(KEY_AVATAR, userInformation.getAvatar());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    //select data
    public ArrayList<UserInformation> SelectName() {
        ArrayList<UserInformation> userInformations = new ArrayList<UserInformation>();
        String SELECT_QUERY = " SELECT "+KEY_NAME +" FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                UserInformation userInformation = new UserInformation();
                userInformation.setName(cursor.getString(0));

                userInformations.add(userInformation);
            } while (cursor.moveToNext());

        }
        return userInformations;
    }
    public ArrayList<UserInformation> SelectAvatar() {
        ArrayList<UserInformation> userInformations = new ArrayList<UserInformation>();
        String SELECT_QUERY = " SELECT "+KEY_AVATAR +" FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                UserInformation userInformation = new UserInformation();

                userInformations.add(userInformation);
            } while (cursor.moveToNext());

        }
        return userInformations;
    }

    //delete all data
    public void clearTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
        db.close();
    }
//    public UserInformation userInformation(int idReceiver) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_NAME, new String[] {
//                        KEY_NAME, KEY_AVATAR }, KEY_ID + "=?",
//                new String[] { String.valueOf(idReceiver) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        UserInformation userInformation = new UserInformation();
//        // return userInformation
//        return userInformation;
//    }

}