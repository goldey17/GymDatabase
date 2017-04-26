package up.gymdatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    Button csearch;
    Button cadd;
    EditText csearchCrit;
    EditText newcID;
    EditText newcName;
    EditText newcTime;
    EditText newcDate;
    EditText newcLoc;
    Spinner searchcDropdown;
    Spinner extraSearchcDropdown;
    EditText deleteId;
    Button delete;


    //Data for the dropdown menus
    int dropdownSelection;
    int extraDropdownSelection;
    final int ID = 0;
    final int NAME = 1;
    final int TIME = 2;
    final int DATE = 3;
    final int LOC = 4;
    final int EQUAL_TO = 0;
    final int LESS_THAN = 1;
    final int GREATER_THAN = 2;


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
        title = (TextView) getActivity().findViewById(R.id.title);
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
        csearch = (Button) getActivity().findViewById(R.id.csearch);
        csearch.setOnClickListener(this);
        cadd = (Button) getActivity().findViewById(R.id.cadd);
        cadd.setOnClickListener(this);
        csearchCrit = (EditText) getActivity().findViewById(R.id.csearch_crit);
        newcID = (EditText) getActivity().findViewById(R.id.new_cid);
        newcName = (EditText) getActivity().findViewById(R.id.new_cname);
        newcTime = (EditText) getActivity().findViewById(R.id.new_ctime);
        newcDate = (EditText) getActivity().findViewById(R.id.new_cdate);
        newcLoc = (EditText) getActivity().findViewById(R.id.new_cloc);

        deleteId = (EditText) getActivity().findViewById(R.id.old_cid);
        delete = (Button) getActivity().findViewById(R.id.delete);
        delete.setOnClickListener(this);

        searchcDropdown = (Spinner) getActivity().findViewById(R.id.search_cdropdown);
        searchcDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case ID:
                        dropdownSelection = ID;
                        extraSearchcDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case NAME:
                        dropdownSelection = NAME;
                        extraSearchcDropdown.setVisibility(View.GONE);
                        break;
                    case TIME:
                        dropdownSelection = TIME;
                        extraSearchcDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case DATE:
                        dropdownSelection = DATE;
                        extraSearchcDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case LOC:
                        dropdownSelection = LOC;
                        extraSearchcDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        extraSearchcDropdown = (Spinner) getActivity().findViewById(R.id.extra_search_cdropdown);
        extraSearchcDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case EQUAL_TO:
                        extraDropdownSelection = EQUAL_TO;
                        break;
                    case LESS_THAN:
                        extraDropdownSelection = LESS_THAN;
                        break;
                    case GREATER_THAN:
                        extraDropdownSelection = GREATER_THAN;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Set up the search drop down
        List<String> list = new ArrayList<>();
        list.add("ID");
        list.add("Name");
        list.add("Time");
        list.add("Date");
        list.add("Location");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchcDropdown.setAdapter(dataAdapter);

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


        } else if (view == csearch) {
            //Set up the criteria
            String selection = null;
            String[] selectionArgs = {""};
            switch (dropdownSelection) {
                case ID:
                    switch (extraDropdownSelection) {
                        case EQUAL_TO:
                            selection = Database.Classes.COLUMN_NAME_Class_ID + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Classes.COLUMN_NAME_Class_ID + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Classes.COLUMN_NAME_Class_ID + " > ?";
                            break;
                    }
                    selectionArgs[0] = csearchCrit.getText().toString();
                    break;
                case NAME:
                    selection = Database.Classes.COLUMN_NAME_Class_Name + " = ?";
                    selectionArgs[0] = csearchCrit.getText().toString();
                    break;
                case TIME:
                    selection = Database.Classes.COLUMN_NAME_Class_Time + " = ?";
                    selectionArgs[0] = csearchCrit.getText().toString();
                    break;

                case DATE:
                    selection = Database.Classes.COLUMN_NAME_Class_Date + " = ?";
                    selectionArgs[0] = csearchCrit.getText().toString();
                    break;
                case LOC:
                    selection = Database.Classes.COLUMN_NAME_Class_Location + " = ?";
                    selectionArgs[0] = csearchCrit.getText().toString();
                    break;
            }
            //Call the query
            Cursor cursor = main1.dbRead.query(
                    Database.Classes.TABLE_NAME_CLASS,              // The table to query
                    allColumnsProjection1,                       // The columns to return
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );
            redrawTable(cursor, true, true, true, true, true);
        } else if (view == cadd) {
            //Set the values and add to the database
            ContentValues values = new ContentValues();
            values.put(Database.Classes.COLUMN_NAME_Class_ID, newcID.getText().toString());
            values.put(Database.Classes.COLUMN_NAME_Class_Name, newcName.getText().toString());
            values.put(Database.Classes.COLUMN_NAME_Class_Time, newcTime.getText().toString());
            values.put(Database.Classes.COLUMN_NAME_Class_Date, newcDate.getText().toString());
            values.put(Database.Classes.COLUMN_NAME_Class_Location, newcLoc.getText().toString());
            main1.dbWrite.insert(Database.Classes.TABLE_NAME_CLASS, null, values);
            //Update data on screen
            Cursor cursor = main1.dbRead.query(
                    Database.Classes.TABLE_NAME_CLASS,          // The table to query
                    allColumnsProjection1,                   // The columns to return
                    null,                                   // The columns for the WHERE clause
                    null,                                   // The values for the WHERE clause
                    null,                                   // don't group the rows
                    null,                                   // don't filter by row groups
                    null                                    // The sort order
            );
            redrawTable(cursor, true, true, true, true, true);

        }else if(view == delete){
        // Define 'where' part of query.
        String selection = Database.Classes.COLUMN_NAME_Class_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { deleteId.getText().toString() };
        // Issue SQL statement.
        main1.dbWrite.delete(Database.Classes.TABLE_NAME_CLASS, selection, selectionArgs);
        //Call the initial query
        Cursor cursor = main1.dbRead.query(
                Database.Classes.TABLE_NAME_CLASS,          // The table to query
                allColumnsProjection1,                   // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );
        redrawTable(cursor,true, true, true, true, true);
    }
    }

    public void redrawTable(Cursor cursor, Boolean needId, Boolean needTime, Boolean needName, Boolean needDate, Boolean needLoc) {
        table.removeAllViews();
        table.addView(tableHeader);

        //Add the data to the table on the screen
        while (cursor.moveToNext()) {
            // declare a new row
            TableRow newRow = new TableRow(getActivity());
            // add views to the row
            TextView id = new TextView(getActivity());
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_ID));
            id.setText("" + idValue);
            newRow.addView(id);

            TextView name = new TextView(getActivity());
            String timeValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Name));
            name.setText(timeValue);
            newRow.addView(name);

            TextView time = new TextView(getActivity());
            String nameValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Time));
            time.setText("" + nameValue);
            newRow.addView(time);

            TextView date = new TextView(getActivity());
            String dateValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Date));
            date.setText("" + dateValue);
            newRow.addView(date);

            TextView loc = new TextView(getActivity());
            String locValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Classes.COLUMN_NAME_Class_Location));
            loc.setText("" + locValue);
            newRow.addView(loc);
            // add the row to the table layout
            table.addView(newRow);
        }
        cursor.close();
    }

    public void setExtraSpinnerToInt() {
        List<String> list = new ArrayList<>();
        list.add("Equal To");
        list.add("Less Than");
        list.add("Greater Than");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraSearchcDropdown.setAdapter(dataAdapter);
    }

}

