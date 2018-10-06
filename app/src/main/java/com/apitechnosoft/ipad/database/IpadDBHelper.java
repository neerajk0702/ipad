package com.apitechnosoft.ipad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apitechnosoft.ipad.model.Data;

import java.util.ArrayList;
import java.util.Date;

public class IpadDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Ipad_DB";

    public IpadDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ShareEmailData_TABLE = "CREATE TABLE ShareEmailData(id INTEGER PRIMARY KEY autoincrement,email TEXT)";
        db.execSQL(CREATE_ShareEmailData_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ShareEmailData");
        onCreate(db);
    }

    public boolean upsertShareEmailData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getEmailId() != null && !ob.getEmailId().equals("")) {
            data = getShareEmailData(ob.getEmailId());
            if (data == null) {
                done = insertShareEmailData(ob);
            } else {
                done = updateShareEmailData(ob);
            }
        }
        return done;
    }

    public Data getShareEmailData(String email) {
        String query = "Select * FROM ShareEmailData WHERE email = '" + email + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data ob = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateShareEmailData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    private void populateShareEmailData(Cursor cursor, Data ob) {
        ob.setId(cursor.getInt(0));
        ob.setEmailId(cursor.getString(1));
    }
    public boolean insertShareEmailData(Data ob) {
        ContentValues values = new ContentValues();
        populateShareEmailValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("ShareEmailData", null, values);
        db.close();
        return i > 0;
    }

    public void populateShareEmailValueData(ContentValues values, Data ob) {
        //values.put("id", ob.getId());
        values.put("email", ob.getEmailId());

    }

    public boolean updateShareEmailData(Data ob) {
        ContentValues values = new ContentValues();
        populateShareEmailValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("ShareEmailData", values, " email = '" + ob.getEmailId() + "'", null);

        db.close();
        return i > 0;
    }
    public ArrayList<Data> getAllShareEmailData() {
        String query = "Select *  FROM ShareEmailData ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Data> list = new ArrayList<Data>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateShareEmailData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }
}
