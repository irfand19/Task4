package irfandp.task4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 10/3/2016.
 */
public class DatabaseEdit extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EditDB3.db";

    public static final String TABLE_NAME_EDIT = "editdb3";
    public static final String COL_1_EDIT = "ID";
    public static final String COL_2_EDIT = "DESCRIPTION";
    public static final String COL_3_EDIT = "AMOUNT";
    public static final String COL_4_EDIT = "TYPE";
    public static final String TABLE_CREATE_EDIT = "CREATE TABLE " + TABLE_NAME_EDIT + " ( " +
            COL_1_EDIT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2_EDIT + " TEXT, " +
            COL_3_EDIT + " TEXT, " +
            COL_4_EDIT + " TEXT);";

    public DatabaseEdit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_EDIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EDIT);
        onCreate(db);
    }

    public boolean save_edit(String description, String amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_2_EDIT, description);
        content_values.put(COL_3_EDIT, amount);
        content_values.put(COL_4_EDIT, type);
        long result = db.insert(TABLE_NAME_EDIT, null, content_values);
        return result != 1;
    }

    public Cursor list_edit() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor pend = db.rawQuery("SELECT * FROM " + TABLE_NAME_EDIT, null);
        return pend;
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

    public Integer delete_edit(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_EDIT, "ID = ?", new String[] {id});
    }


}
