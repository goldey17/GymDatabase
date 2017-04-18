package up.gymdatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by goldey17 on 4/3/2017.
 */

public class Classes extends Fragment implements View.OnClickListener {


    TextView title;
    TableLayout table;
    TableRow tableHeader;
    Button sortId;
    Button sortName;
    Button sortTime;
    Button sortDate;
    Button sortLocation;

    //Get main so I can get the readable database
    MainActivity main1;
    Cursor cursor;

    // Define a projection that gets all columns from the database
    String[] allColumnsProjection1 = {
            Database.Classes.COLUMN_NAME_Class_ID,
            Database.Classes.COLUMN_NAME_Class_Name,
            Database.Classes.COLUMN_NAME_Class_Time,
            Database.Classes.COLUMN_NAME_Class_Date,
            Database.Classes.COLUMN_NAME_Class_Location
    };

    @Override
    /*
     * Method that handles the creation of the screen when first called
     *
     * Parameter:
     * savedInstanceState - calls the super class
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_classes, container, false);
    }


    @Override
     /*
     * Method that is triggered soon after onCreateView() all view setup is done here
     *
     * Parameter:
     * savedInstanceState - calls the super class
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        title = (TextView) getActivity().findViewById(R.id.ctitle);
        table = (TableLayout) getActivity().findViewById(R.id.ctable);
        tableHeader = (TableRow) getActivity().findViewById(R.id.ctable_header);
        sortId = (Button) getActivity().findViewById(R.id.sort_cid);
        sortId.setOnClickListener(this);
        sortTime = (Button) getActivity().findViewById(R.id.sort_ctime);
        sortTime.setOnClickListener(this);
        sortDate = (Button) getActivity().findViewById(R.id.sort_cdate);
        sortDate.setOnClickListener(this);
        sortLocation = (Button) getActivity().findViewById(R.id.sort_cloc);
        sortLocation.setOnClickListener(this);
        sortName = (Button) getActivity().findViewById(R.id.sort_cname);
        sortName.setOnClickListener(this);

        //Set up main
        main1 = (MainActivity) getActivity();

        //Call the query
        Cursor cursor1 = null;



    cursor1 = main1.dbRead.query(
            Database.Classes.TABLE_NAME_CLASS,          // The table to query
            allColumnsProjection1,                   // The columns to return
            null,                                   // The columns for the WHERE clause
            null,                                   // The values for the WHERE clause
            null,                                   // don't group the rows
            null,                                   // don't filter by row groups
            null
    );                                              // The sort order);




        redrawTable(cursor1, true, true, true, true, true);


    }
    @Override
    public void onClick(View view) {

        if (view == sortId) {
            if (sortId.getText().equals("Sort by ID (ASC)")) {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_ID + " ASC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortId.setText(R.string.sort_by_id_dsc);
            } else {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_ID + " DESC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortId.setText(R.string.sort_by_id_asc);
            }
        } else if (view == sortTime) {
            if (sortTime.getText().equals("Sort by Time (ASC)")) {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Time + " ASC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortTime.setText(R.string.sort_by_time_dsc);
            } else {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Time + " DESC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortTime.setText(R.string.sort_by_time_asc);
            }
        } else if (view == sortDate) {
            if (sortDate.getText().equals("Sort by Date (ASC)")) {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Date + " ASC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortDate.setText(R.string.sort_by_cdate_dsc);
            } else {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Date + " DESC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortDate.setText(R.string.sort_by_cdate_asc);
            }
        } else if (view == sortLocation) {
            if (sortLocation.getText().equals("Sort by Location (ASC)")) {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Location + " ASC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortLocation.setText(R.string.sort_by_cloc_dsc);
            } else {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Location + " DESC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortLocation.setText(R.string.sort_by_cloc_asc);
            }

        } else if (view == sortName) {
            if (sortName.getText().equals("Sort by Name (ASC)")) {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Name + " ASC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortName.setText(R.string.sort_by_cname_dsc);
            } else {
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Classes.COLUMN_NAME_Class_Name + " DESC";
                //Call the query
                Cursor cursor = main1.dbRead.query(
                        Database.Classes.TABLE_NAME_CLASS,          // The table to query
                        allColumnsProjection1,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true, true);
                sortName.setText(R.string.sort_by_cname_asc);
            }
        }
    }

        public void redrawTable(Cursor cursor, Boolean needId, Boolean needTime, Boolean needName, Boolean needDate, Boolean needLoc){
            table.removeAllViews();
            table.addView(tableHeader);

            //Add the data to the table on the screen
            while (cursor.moveToNext()){
                // declare a new row
                TableRow newRow = new TableRow(getActivity());
                // add views to the row
                TextView id = new TextView(getActivity());
                int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_ID));
                id.setText("" + idValue);
                newRow.addView(id);

                TextView time = new TextView(getActivity());
                String timeValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Time));
                time.setText(timeValue);
                newRow.addView(time);

                TextView name = new TextView(getActivity());
                int nameValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Name));
                name.setText("" + nameValue);
                newRow.addView(name);

                TextView date = new TextView(getActivity());
                int dateValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Date));
                date.setText("" + dateValue);
                newRow.addView(date);

                TextView loc = new TextView(getActivity());
                int locValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Location));
                loc.setText("" + locValue);
                newRow.addView(loc);
                // add the row to the table layout
                table.addView(newRow);
            }
            cursor.close();
        }

    }

