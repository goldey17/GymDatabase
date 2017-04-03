package up.gymdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to create and maintain the database and tables
 * Created by goldey17 on 4/3/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";

    //String to create the equipment table as defined in the schema
    private static final String SQL_CREATE_EQUIPMENT =
            "CREATE TABLE " + Database.Equipment.TABLE_NAME + " (" +
                    Database.Equipment.COLUMN_NAME_Equipment_ID + " INTEGER PRIMARY KEY," +
                    Database.Equipment.COLUMN_NAME_Equipment_Type + " TEXT," +
                    Database.Equipment.COLUMN_NAME_Equipment_Quantity + " INTEGER," +
                    Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " INTEGER)";

    //String to drop the equipment table
    private static final String SQL_DELETE_EQUIPMENT =
            "DROP TABLE IF EXISTS " + Database.Equipment.TABLE_NAME;

    //Constructor for the helper, this creates the database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //On create method for the database which creates the tables
    public void onCreate(SQLiteDatabase db) {
        //Create the equipment database
        db.execSQL(SQL_CREATE_EQUIPMENT);
    }

    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EQUIPMENT);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
