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
 * Created by goldey17 on 4/3/2017.
 */

public class Students extends Fragment implements View.OnClickListener {

    TableLayout table;
    TableRow header;
    Button sortID;
    Button sortName;
    Button sortYear;
    Button search;
    Button add;
    EditText searchBar;
    EditText newID;
    EditText newName;
    EditText newYear;
    Spinner searchField;
    Spinner searchLimiter;

    //Data for the dropdown menus
    int fieldType;
    int fieldLimit;
    final int ID = 0;
    final int NAME = 1;
    final int YEAR = 2;
    final int EQUAL_TO = 0;
    final int LESS_THAN = 1;
    final int GREATER_THAN = 2;

    //Get main so I can get the readable and writable database
    MainActivity main;

    // Define a projection that gets all columns from the database
    String[] allColumnsProjection = {
            Database.Students.COLUMN_NAME_Student_Name,
            Database.Students.COLUMN_NAME_Student_Year,
            Database.Students.COLUMN_NAME_Student_ID
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
        return inflater.inflate(R.layout.activity_student, container, false);
    }


    @Override
     /*
     * Method that is triggered soon after onCreateView() all view setup is done here
     *
     * Parameter:
     * savedInstanceState - calls the super class
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //setup the widgets on the screen
        table = (TableLayout) getActivity().findViewById(R.id.table);
        header = (TableRow) getActivity().findViewById(R.id.header);
        sortID = (Button) getActivity().findViewById(R.id.sortID);
        sortID.setOnClickListener(this);
        sortName = (Button) getActivity().findViewById(R.id.sortName);
        sortName.setOnClickListener(this);
        sortYear = (Button) getActivity().findViewById(R.id.sortYear);
        sortYear.setOnClickListener(this);
        search = (Button) getActivity().findViewById(R.id.search);
        search.setOnClickListener(this);
        add = (Button)  getActivity().findViewById(R.id.add);
        add.setOnClickListener(this);
        searchBar = (EditText) getActivity().findViewById(R.id.searchBar);
        newID = (EditText) getActivity().findViewById(R.id.newID);
        newName = (EditText) getActivity().findViewById(R.id.newName);
        newYear = (EditText) getActivity().findViewById(R.id.newYear);
        searchField = (Spinner) getActivity().findViewById(R.id.searchField);
        searchField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position) {
                    case ID:
                        fieldType = ID;
                        searchLimiter.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case NAME:
                        fieldType = NAME;
                        searchLimiter.setVisibility(View.GONE);
                        break;
                    case YEAR:
                        fieldType = YEAR;
                        searchLimiter.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchLimiter = (Spinner) getActivity().findViewById(R.id.searchLimit);
        searchLimiter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        list.add("ID");
        list.add("Name");
        list.add("Year");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchField.setAdapter(dataAdapter);

        //Set up main
        main = (MainActivity) getActivity();

        //Call the initial query
        Cursor cursor = main.dbRead.query(
                Database.Students.TABLE_NAME,          // The table to query
                allColumnsProjection,                   // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );
        //redrawTable(cursor);
    }


    @Override
    public void onClick(View view) {
        if(view == sortID){
            if(sortID.getText().equals("Sort by ID (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Students.COLUMN_NAME_Student_ID + " ASC";

                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Students.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );

                redrawTable(cursor);
                sortID.setText(R.string.sort_by_id_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Students.COLUMN_NAME_Student_ID + " DESC";

                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Students.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortID.setText(R.string.sort_by_id_asc);
            }
        }else if(view == sortName){
            if(sortName.getText().equals("Sort by Name (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Students.COLUMN_NAME_Student_Name + " ASC";

                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Students.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortName.setText(R.string.sort_by_name_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Students.COLUMN_NAME_Student_Name + " DESC";

                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Students.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortName.setText(R.string.sort_by_name_asc);
            }
        }else if(view == sortYear){
            if(sortYear.getText().equals("Sort by Year (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Students.COLUMN_NAME_Student_Year + " ASC";

                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Students.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortYear.setText(R.string.sort_by_position_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Students.COLUMN_NAME_Student_Year + " DESC";

                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Students.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor);
                sortYear.setText(R.string.sort_by_position_asc);
            }
        }else if (view == search){
            //Set up the criteria
            String selection = null;
            String[] selectionArgs = {""};
            switch (fieldType){
                case ID:
                    switch (fieldLimit){
                        case EQUAL_TO:
                            selection = Database.Students.COLUMN_NAME_Student_ID + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Students.COLUMN_NAME_Student_ID + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Students.COLUMN_NAME_Student_ID + " > ?";
                            break;
                    }
                    selectionArgs[0] = searchBar.getText().toString();
                    break;
                case NAME:
                    selection = Database.Students.COLUMN_NAME_Student_Name + " = ?";
                    selectionArgs[0] = searchBar.getText().toString();
                    break;
                case YEAR:
                    selection = Database.Students.COLUMN_NAME_Student_Year + " = ?";
                    selectionArgs[0] = searchBar.getText().toString();
                    break;
            }
            //Call the query
            Cursor cursor = main.dbRead.query(
                    Database.Students.TABLE_NAME,              // The table to query
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
            values.put(Database.Students.COLUMN_NAME_Student_Name, newName.getText().toString());
            values.put(Database.Students.COLUMN_NAME_Student_Year, newYear.getText().toString());
            values.put(Database.Students.COLUMN_NAME_Student_ID, newID.getText().toString());

            main.dbWrite.insert(Database.Students.TABLE_NAME, null, values);
            //Update data on screen
            Cursor cursor = main.dbRead.query(
                    Database.Students.TABLE_NAME,          // The table to query
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
        table.addView(header);

        //Add the data to the table on the screen
        while (cursor.moveToNext()){
            // declare a new row
            TableRow newRow = new TableRow(getActivity());

            // add views to the row
            TextView id = new TextView(getActivity());
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Students.COLUMN_NAME_Student_ID));
            String text = Integer.toString(idValue);
            id.setText(text);
            newRow.addView(id);

            TextView name = new TextView(getActivity());
            String nameValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Students.COLUMN_NAME_Student_Name));
            name.setText(nameValue);
            newRow.addView(name);

            TextView position = new TextView(getActivity());
            String posValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Students.COLUMN_NAME_Student_Year));
            position.setText(posValue);
            newRow.addView(position);

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
        searchLimiter.setAdapter(dataAdapter);
    }
}
