package irfandp.task4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 10/2/2016.
 */
public class DatabaseSent extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SentDB.db";
    public static final String TABLE_NAME = "sentdb";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DESCRIPTION";
    public static final String COL_3 = "AMOUNT";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2 + " TEXT, " +
            COL_3 + " TEXT);";

    public DatabaseSent(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean save_sent(String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_2, description);
        content_values.put(COL_3, amount);
        long result = db.insert(TABLE_NAME, null, content_values);
        return result != 1;
    }

    public Cursor list_sent() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor sent = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return sent;
    }

    public boolean update_sent(String id, String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_1, id);
        content_values.put(COL_2, description);
        content_values.put(COL_3, amount);
        db.update(TABLE_NAME, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public Integer delete_sent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

}