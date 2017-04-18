package up.gymdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class to create and maintain the database and tables
 * Created by goldey17 on 4/3/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Database.db";

    //String to create the equipment table as defined in the schema
    private static final String SQL_CREATE_EQUIPMENT =
            "CREATE TABLE " + Database.Equipment.TABLE_NAME + " (" +
                    Database.Equipment.COLUMN_NAME_Equipment_ID + " INTEGER PRIMARY KEY," +
                    Database.Equipment.COLUMN_NAME_Equipment_Type + " TEXT UNIQUE," +
                    Database.Equipment.COLUMN_NAME_Equipment_Quantity + " INTEGER," +
                    Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " INTEGER)";

    //String to create the rents table as defined in the schema
    private static final String SQL_CREATE_RENTS =
            "CREATE TABLE " + Database.Rents.TABLE_NAME + " (" +
                    Database.Rents.COLUMN_NAME_Equipment_ID + " INTEGER," +
                    Database.Rents.COLUMN_NAME_Student_ID + " INTEGER," +
                    Database.Rents.COLUMN_NAME_Equipment_Quantity + " INTEGER," +
                    Database.Rents.COLUMN_NAME_Days + " INTEGER," +
//                    " FOREIGN KEY (" + Database.Rents.COLUMN_NAME_Student_ID + ") REFERENCES " +
//                    Database.Students.TABLE_NAME + "(" +
//                    Database.Students.COLUMN_NAME_Student_ID + ")" +
                    " FOREIGN KEY (" + Database.Rents.COLUMN_NAME_Equipment_ID + ") REFERENCES " +
                    Database.Equipment.TABLE_NAME + "(" +
                    Database.Equipment.COLUMN_NAME_Equipment_ID + "))";


    private static final String SQL_CREATE_CLASSES =
            "CREATE TABLE " + Database.Classes.TABLE_NAME_CLASS + " (" +
                    Database.Classes.COLUMN_NAME_Class_ID + " INTEGER PRIMARY KEY," +
                    Database.Classes.COLUMN_NAME_Class_Name + " TEXT," +
                    Database.Classes.COLUMN_NAME_Class_Time + " TIME," +
                    Database.Classes.COLUMN_NAME_Class_Date + " DATE," +
                    Database.Classes.COLUMN_NAME_Class_Location + " TEXT)";


    //String to create the equipment table as defined in the schema
    private static final String SQL_CREATE_STAFF =
            "CREATE TABLE " + Database.Staff.TABLE_NAME + " (" +
                    Database.Staff.COLUMN_NAME_Staff_ID + " INTEGER PRIMARY KEY," +
                    Database.Staff.COLUMN_NAME_Staff_Name + " TEXT," +
                    Database.Staff.COLUMN_NAME_Staff_Position + " TEXT)";

    //String to drop the equipment table
    private static final String SQL_DELETE_EQUIPMENT =
            "DROP TABLE IF EXISTS " + Database.Equipment.TABLE_NAME;

    private static final String SQL_DELETE_CLASSES =
            "DROP TABLE IF EXISTS " + Database.Classes.TABLE_NAME_CLASS;

    //String to drop the staff table
    private static final String SQL_DELETE_STAFF =
            "DROP TABLE IF EXISTS " + Database.Staff.TABLE_NAME;

    //String to drop the rents table
    private static final String SQL_DELETE_RENTS =
            "DROP TABLE IF EXISTS " + Database.Rents.TABLE_NAME;

    //Constructor for the helper, this creates the database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //On create method for the database which creates the tables
    public void onCreate(SQLiteDatabase db) {
        //Create the equipment database and add the known data
        db.execSQL(SQL_CREATE_EQUIPMENT);
        db.execSQL(SQL_CREATE_CLASSES);
        db.execSQL(SQL_CREATE_STAFF);
        db.execSQL(SQL_CREATE_RENTS);
        addEquipmentToDatabase(db);
        addClassesToDatabase(db);
        addStaffToDatabase(db);
    }

    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EQUIPMENT);
        db.execSQL(SQL_DELETE_CLASSES);
        db.execSQL(SQL_DELETE_RENTS);
        db.execSQL(SQL_DELETE_STAFF);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Method to add all the equipment to the database from the EquipmentDatabaseInfo.txt file
    public void addEquipmentToDatabase(SQLiteDatabase db){
        //Get data from the file
        String data = readFromFile("EquipmentDatabaseInfo.txt");

        //Split data based on the commas
        String comma = "[,]";
        String[] items = data.split(comma);
        //Enter data into the table
        for(int i = 0; i < items.length; i = i + 4){
            // Create a new map of values for the entry into the table
            ContentValues values = new ContentValues();
            values.put(Database.Equipment.COLUMN_NAME_Equipment_ID, items[i]);
            values.put(Database.Equipment.COLUMN_NAME_Equipment_Type, items[i + 1]);
            values.put(Database.Equipment.COLUMN_NAME_Equipment_Quantity, items[i + 2]);
            values.put(Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost, items[i + 3]);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(Database.Equipment.TABLE_NAME, null, values);
        }
    }

    public void addClassesToDatabase(SQLiteDatabase db){
        //Get data from the file
        String data1 = readFromFile("ClassesDatabaseInfo.txt");

        //Split data based on the commas
        String comma = "[,]";
        String[] items = data1.split(comma);
        //Enter data into the table
        for(int i = 0; i < items.length; i = i + 5){
            // Create a new map of values for the entry into the table
            ContentValues values = new ContentValues();
            values.put(Database.Classes.COLUMN_NAME_Class_ID, items[i]);
            values.put(Database.Classes.COLUMN_NAME_Class_Name, items[i + 1]);
            values.put(Database.Classes.COLUMN_NAME_Class_Time, items[i + 2]);
            values.put(Database.Classes.COLUMN_NAME_Class_Date, items[i + 3]);
            values.put(Database.Classes.COLUMN_NAME_Class_Location, items[i + 4]);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(Database.Classes.TABLE_NAME_CLASS, null, values);
        }
    }

    //Method to add all the equipment to the database from the EquipmentDatabaseInfo.txt file
    public void addStaffToDatabase(SQLiteDatabase db){
        //Get data from the file
        String data = readFromFile("StaffDatabaseInfo.txt");

        //Split data based on the commas
        String comma = "[,]";
        String[] items = data.split(comma);

        //Enter data into the table
        for(int i = 0; i < items.length; i = i + 3){
            // Create a new map of values for the entry into the table
            ContentValues values = new ContentValues();
            values.put(Database.Staff.COLUMN_NAME_Staff_ID, items[i]);
            values.put(Database.Staff.COLUMN_NAME_Staff_Name, items[i + 1]);
            values.put(Database.Staff.COLUMN_NAME_Staff_Position, items[i + 2]);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(Database.Staff.TABLE_NAME, null, values);
        }
    }

    //Method to read data from a given file
    private String readFromFile(String fileName) {
        Context context = App.getContext();
        AssetManager assetManager = App.getContext().getAssets();
        String ret = "";

        try {
            InputStream inputStream = assetManager.open(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
