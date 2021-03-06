package irfandp.task4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 9/26/2016.
 */
public class DatabaseIncome extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IncomeDB.db";
    public static final String TABLE_NAME = "incomedb";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DESCRIPTION";
    public static final String COL_3 = "AMOUNT";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2 + " TEXT, " +
            COL_3 + " TEXT );";

    public DatabaseIncome(Context context) {
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

    public boolean save_income(String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_2, description);
        content_values.put(COL_3, amount);
        long result = db.insert(TABLE_NAME, null, content_values);
        return result != 1;
    }

    public Cursor list_income() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor expenses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return expenses;
    }

    public boolean update_income(String id, String description, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_1, id);
        content_values.put(COL_2, description);
        content_values.put(COL_3, amount);
        db.update(TABLE_NAME, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public Integer delete_income(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

//    public boolean update_student(String id, String name, String surname, String marks) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues content_values = new ContentValues();
//        content_values.put(COL_1, id);
//        content_values.put(COL_2, name);
//        content_values.put(COL_3, surname);
//        db.update(TABLE_NAME, content_values, "ID = ? ", new String[]{id});
//        return true;
//    }
//    public Integer delete_student(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
//    }


}


