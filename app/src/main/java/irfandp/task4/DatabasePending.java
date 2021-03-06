package irfandp.task4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 10/2/2016.
 */
public class DatabasePending extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PendingDB.db";

    public static final String TABLE_NAME = "pendingdb";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DESCRIPTION";
    public static final String COL_3 = "AMOUNT";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2 + " TEXT, " +
            COL_3 + " TEXT);";

    public static final String TABLE_NAME_EDIT = "editdb";
    public static final String COL_1_EDIT = "ID";
    public static final String COL_2_EDIT = "DESCRIPTION";
    public static final String COL_3_EDIT = "AMOUNT";
    public static final String TABLE_CREATE_EDIT = "CREATE TABLE " + TABLE_NAME_EDIT + " ( " +
            COL_1_EDIT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2_EDIT + " TEXT, " +
            COL_3_EDIT + " TEXT);";

    public DatabasePending(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_EDIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EDIT);
        onCreate(db);
    }

    public boolean save_pending(String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_2, description);
        content_values.put(COL_3, amount);
        long result = db.insert(TABLE_NAME, null, content_values);
        return result != 1;
    }

    public boolean save_edit(String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_2_EDIT, description);
        content_values.put(COL_3_EDIT, amount);
        long result = db.insert(TABLE_NAME_EDIT, null, content_values);
        return result != 1;
    }

    public Cursor list_pending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor pend = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return pend;
    }

    public Cursor list_edit() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor pend = db.rawQuery("SELECT * FROM " + TABLE_NAME_EDIT, null);
        return pend;
    }

    public boolean update_pending(String id, String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_1, id);
        content_values.put(COL_2, description);
        content_values.put(COL_3, amount);
        db.update(TABLE_NAME, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public boolean update_edit(String id, String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_1_EDIT, id);
        content_values.put(COL_2_EDIT, description);
        content_values.put(COL_3_EDIT, amount);
        db.update(TABLE_NAME_EDIT, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public Integer delete_pending(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public Integer delete_edit(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_EDIT, "ID = ?", new String[] {id});
    }

}
