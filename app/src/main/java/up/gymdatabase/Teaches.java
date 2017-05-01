package up.gymdatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by laum18 on 4/25/2017.
 */
public class Teaches extends Fragment implements View.OnClickListener {
    //All the stuff on the screen
    TableLayout table;
    TableRow tableHeader;
    Button sortClassId;
    Button sortStaffID;
    Button search;
    Button add;
    Button delete;
    EditText searchBar;
    EditText newClassID;
    EditText newStaffID;
    EditText deleteId;
    Spinner searchField;
    Spinner searchLimit;

    //Data for the dropdown menus
    int fieldType;
    int fieldLimit;
    final int CLASSID = 0;
    final int STAFFID = 1;
    final int EQUAL_TO = 0;
    final int LESS_THAN = 1;
    final int GREATER_THAN = 2;

    //Get main so I can get the readable and writable database
    MainActivity main;

    // Define a projection that gets all columns from the database
    String[] allColumnsProjection = {
            Database.Teaches.COLUMN_NAME_Class_ID,
            Database.Teaches.COLUMN_NAME_Staff_ID
    };

    @Override
    /*
     * Method that handles the creation of the screen when first called, links the layout
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_teaches, container, false);
    }

    @Override
     /*
     * Method that is triggered soon after onCreateView() all view setup is done here
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //setup the widgets on the screen
        table = (TableLayout) getActivity().findViewById(R.id.teachesTable);
        tableHeader = (TableRow) getActivity().findViewById(R.id.teachesHeader);
        sortClassId = (Button) getActivity().findViewById(R.id.sortClassID);
        sortClassId.setOnClickListener(this);
        sortStaffID = (Button) getActivity().findViewById(R.id.sortStaffID);
        sortStaffID.setOnClickListener(this);
        search = (Button) getActivity().findViewById(R.id.search);
        search.setOnClickListener(this);
        add = (Button)  getActivity().findViewById(R.id.add);
        add.setOnClickListener(this);
        delete = (Button) getActivity().findViewById(R.id.deleteClassID);
        delete.setOnClickListener(this);
        searchBar = (EditText) getActivity().findViewById(R.id.searchBar);
        newClassID = (EditText) getActivity().findViewById(R.id.newClassID);
        newStaffID = (EditText) getActivity().findViewById(R.id.newStaffID);
        deleteId = (EditText) getActivity().findViewById(R.id.deleteCurID);
        searchField = (Spinner) getActivity().findViewById(R.id.searchField);
        searchField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position) {
                    case CLASSID:
                        fieldType = CLASSID;
                        searchLimit.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case STAFFID:
                        fieldType = STAFFID;
                        searchLimit.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchLimit = (Spinner) getActivity().findViewById(R.id.searchLimit);
        searchLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position){
                    case EQUAL_TO:
                        fieldLimit = EQUAL_TO;
                        break;
                    case LESS_THAN:
                        fieldLimit = LESS_THAN;
                        break;
                    case GREATER_THAN:
                        fieldLimit = GREATER_THAN;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Set up the search drop down
        List<String> list = new ArrayList<>();
        list.add("ClassID");
        list.add("StaffID");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchField.setAdapter(dataAdapter);

        //Set up main
        main = (MainActivity) getActivity();

        //Call the initial query
        Cursor cursor = main.dbRead.query(
                Database.Teaches.TABLE_NAME,          // The table to query
                allColumnsProjection,                   // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );
        redrawTable(cursor);
    }

    @Override
    /*
     * Method to handle when the buttons are clicked on the screen
     */
    public void onClick(View view) {
        if(view == sortClassId){
            if(sortClassId.getText().equals("Sort by Class ID (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Teaches.COLUMN_NAME_Class_ID + " ASC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Teaches.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortClassId.setText(R.string.sort_by_class_id_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Teaches.COLUMN_NAME_Class_ID + " DESC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Teaches.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortClassId.setText(R.string.sort_by_class_id_asc);
            }
        }else if(view == sortStaffID){
            if(sortStaffID.getText().equals("Sort by Staff ID (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Teaches.COLUMN_NAME_Staff_ID + " ASC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Teaches.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortStaffID.setText(R.string.sort_by_staff_id_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Teaches.COLUMN_NAME_Staff_ID + " DESC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Teaches.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortStaffID.setText(R.string.sort_by_staff_id_asc);
            }
        }else if (view == search){
            //Set up the criteria
            String selection = null;
            String[] selectionArgs = {""};
            switch (fieldType){
                case CLASSID:
                    switch (fieldLimit){
                        case EQUAL_TO:
                            selection = Database.Teaches.COLUMN_NAME_Class_ID + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Teaches.COLUMN_NAME_Class_ID + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Teaches.COLUMN_NAME_Class_ID + " > ?";
                            break;
                    }
                    selectionArgs[0] = searchBar.getText().toString();
                    break;
                case STAFFID:
                    switch (fieldLimit){
                        case EQUAL_TO:
                            selection = Database.Teaches.COLUMN_NAME_Staff_ID + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Teaches.COLUMN_NAME_Staff_ID + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Teaches.COLUMN_NAME_Staff_ID + " > ?";
                            break;
                    }
                    selectionArgs[0] = searchBar.getText().toString();
                    break;
            }
            //Call the query
            Cursor cursor = main.dbRead.query(
                    Database.Teaches.TABLE_NAME,              // The table to query
                    allColumnsProjection,                       // The columns to return
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );
            redrawTable(cursor);
        } else if (view == add){
            //Set the values and add to the database
            ContentValues values = new ContentValues();
            values.put(Database.Teaches.COLUMN_NAME_Class_ID, newClassID.getText().toString());
            values.put(Database.Teaches.COLUMN_NAME_Staff_ID, newStaffID.getText().toString());

            main.dbWrite.insert(Database.Teaches.TABLE_NAME, null, values);
            //Update data on screen
            Cursor cursor = main.dbRead.query(
                    Database.Teaches.TABLE_NAME,          // The table to query
                    allColumnsProjection,                   // The columns to return
                    null,                                   // The columns for the WHERE clause
                    null,                                   // The values for the WHERE clause
                    null,                                   // don't group the rows
                    null,                                   // don't filter by row groups
                    null                                    // The sort order
            );
            redrawTable(cursor);
        } else if(view == delete){
            // Define 'where' part of query.
            String selection = Database.Teaches.COLUMN_NAME_Class_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { deleteId.getText().toString() };
            // Issue SQL statement.
            main.dbWrite.delete(Database.Teaches.TABLE_NAME, selection, selectionArgs);
            //Call the initial query
            Cursor cursor = main.dbRead.query(
                    Database.Teaches.TABLE_NAME,          // The table to query
                    allColumnsProjection,                   // The columns to return
                    null,                                   // The columns for the WHERE clause
                    null,                                   // The values for the WHERE clause
                    null,                                   // don't group the rows
                    null,                                   // don't filter by row groups
                    null                                    // The sort order
            );
            redrawTable(cursor);
        }
    }

    /*
     * Method to redraw the table on the screen based on the input provided
     */
    public void redrawTable(Cursor cursor){
        table.removeAllViews();
        table.addView(tableHeader);

        //Add the data to the table on the screen
        while (cursor.moveToNext()){
            // declare a new row
            TableRow newRow = new TableRow(getActivity());

            // add views to the row
            TextView classId = new TextView(getActivity());
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Teaches.COLUMN_NAME_Class_ID));
            String text = Integer.toString(idValue);
            classId.setText(text);
            newRow.addView(classId);

            TextView staffId = new TextView(getActivity());
            int staffValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Teaches.COLUMN_NAME_Staff_ID));
            text = Integer.toString(staffValue);
            staffId.setText(text);
            newRow.addView(staffId);

            // add the row to the table layout
            table.addView(newRow);
        }
        cursor.close();
    }

    /*
     * Set up the extra search drop down
     */
    public void setExtraSpinnerToInt(){
        List<String> list = new ArrayList<>();
        list.add("Equal To");
        list.add("Less Than");
        list.add("Greater Than");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchLimit.setAdapter(dataAdapter);
    }
}
