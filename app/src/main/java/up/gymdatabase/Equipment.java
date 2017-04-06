package up.gymdatabase;

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
 * The screen related to the equipment in the gym
 * Created by Jordan Goldey on 4/3/2017.
 */
public class Equipment extends Fragment implements View.OnClickListener {
    //All the stuff on the screen
    TextView title;
    TableLayout table;
    TableRow tableHeader;
    Button sortId;
    Button sortType;
    Button sortQuan;
    Button sortCost;
    Button search;
    EditText searchCrit;
    Spinner searchDropdown;
    Spinner extraSearchDropdown;

    int dropdownSelection;
    int extraDropdownSelection;
    final int ID = 0;
    final int TYPE = 1;
    final int QUANTITY = 2;
    final int RENTAL_COST = 3;
    final int EQUAL_TO = 0;
    final int LESS_THAN = 1;
    final int GREATER_THAN = 2;

    //Get main so I can get the readable database
    MainActivity main;

    // Define a projection that gets all columns from the database
    String[] allColumnsProjection = {
            Database.Equipment.COLUMN_NAME_Equipment_ID,
            Database.Equipment.COLUMN_NAME_Equipment_Type,
            Database.Equipment.COLUMN_NAME_Equipment_Quantity,
            Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost
    };

    @Override
    /*
     * Method that handles the creation of the screen when first called, links the layout
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_equipment, container, false);
    }

    @Override
     /*
     * Method that is triggered soon after onCreateView() all view setup is done here
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //setup the widgets on the screen
        title = (TextView) getActivity().findViewById(R.id.title);
        table = (TableLayout) getActivity().findViewById(R.id.table);
        tableHeader = (TableRow) getActivity().findViewById(R.id.table_header);
        sortId = (Button) getActivity().findViewById(R.id.sort_id);
        sortId.setOnClickListener(this);
        sortType = (Button) getActivity().findViewById(R.id.sort_type);
        sortType.setOnClickListener(this);
        sortQuan = (Button) getActivity().findViewById(R.id.sort_quan);
        sortQuan.setOnClickListener(this);
        sortCost = (Button) getActivity().findViewById(R.id.sort_cost);
        sortCost.setOnClickListener(this);
        search = (Button) getActivity().findViewById(R.id.search);
        search.setOnClickListener(this);
        searchCrit = (EditText) getActivity().findViewById(R.id.search_crit);
        searchDropdown = (Spinner) getActivity().findViewById(R.id.search_dropdown);
        searchDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position) {
                    case ID:
                        dropdownSelection = ID;
                        extraSearchDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case TYPE:
                        dropdownSelection = TYPE;
                        extraSearchDropdown.setVisibility(View.GONE);
                        break;
                    case QUANTITY:
                        dropdownSelection = QUANTITY;
                        extraSearchDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                    case RENTAL_COST:
                        dropdownSelection = RENTAL_COST;
                        extraSearchDropdown.setVisibility(View.VISIBLE);
                        setExtraSpinnerToInt();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        extraSearchDropdown = (Spinner) getActivity().findViewById(R.id.extra_search_dropdown);
        extraSearchDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position){
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
        list.add("Type");
        list.add("Quantity");
        list.add("Rental Cost");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchDropdown.setAdapter(dataAdapter);

        //Set up main
        main = (MainActivity) getActivity();

        //Call the query
        Cursor cursor = main.dbRead.query(
                Database.Equipment.TABLE_NAME,          // The table to query
                allColumnsProjection,                   // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );
        redrawTable(cursor, true, true, true, true);
    }

    @Override
    /*
     * Method to handle when the buttons are clicked on the screen
     */
    public void onClick(View view) {
        if(view == sortId){
            if(sortId.getText().equals("Sort by ID (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_ID + " ASC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortId.setText(R.string.sort_by_id_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_ID + " DESC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortId.setText(R.string.sort_by_id_asc);
            }
        }else if(view == sortType){
            if(sortType.getText().equals("Sort by Type (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_Type + " ASC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortType.setText(R.string.sort_by_type_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_Type + " DESC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortType.setText(R.string.sort_by_type_asc);
            }
        }else if(view == sortQuan){
            if(sortQuan.getText().equals("Sort by Quantity (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_Quantity + " ASC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortQuan.setText(R.string.sort_by_quan_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_Quantity + " DESC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortQuan.setText(R.string.sort_by_quan_asc);
            }
        }else if(view == sortCost){
            if(sortCost.getText().equals("Sort by Rental Cost (ASC)")){
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " ASC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortCost.setText(R.string.sort_by_cost_dsc);
            }else{
                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " DESC";
                //Call the query
                Cursor cursor = main.dbRead.query(
                        Database.Equipment.TABLE_NAME,          // The table to query
                        allColumnsProjection,                   // The columns to return
                        null,                                   // The columns for the WHERE clause
                        null,                                   // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                   // don't filter by row groups
                        sortOrder                               // The sort order
                );
                redrawTable(cursor, true, true, true, true);
                sortCost.setText(R.string.sort_by_cost_asc);
            }
        }else if (view == search){
            String selection = null;
            String[] selectionArgs = null;
            switch (dropdownSelection){
                case ID:
                    switch (extraDropdownSelection){
                        case EQUAL_TO:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_ID + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_ID + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_ID + " > ?";
                            break;
                    }
                    String[] args = { searchCrit.getText().toString() };
                    selectionArgs = args;
                    break;
                case TYPE:
                    selection = Database.Equipment.COLUMN_NAME_Equipment_Type + " = ?";
                    String[] args1 = { searchCrit.getText().toString() };
                    selectionArgs = args1;
                    break;
                case QUANTITY:
                    switch (extraDropdownSelection){
                        case EQUAL_TO:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_Quantity + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_Quantity + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_Quantity + " > ?";
                            break;
                    }
                    String[] args2 = { searchCrit.getText().toString() };
                    selectionArgs = args2;
                    break;
                case RENTAL_COST:
                    switch (extraDropdownSelection){
                        case EQUAL_TO:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " = ?";
                            break;
                        case LESS_THAN:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " < ?";
                            break;
                        case GREATER_THAN:
                            selection = Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost + " > ?";
                            break;
                    }
                    String[] args3 = { searchCrit.getText().toString() };
                    selectionArgs = args3;
                    break;
            }
            //Call the query
            Cursor cursor = main.dbRead.query(
                    Database.Equipment.TABLE_NAME,              // The table to query
                    allColumnsProjection,                       // The columns to return
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );
            redrawTable(cursor, true, true, true, true);
        }
    }

    /*
     * Method to redraw the table on the screen based on the input provided
     */
    public void redrawTable(Cursor cursor, Boolean needId, Boolean needType, Boolean needQuan, Boolean needCost){
        table.removeAllViews();
        table.addView(tableHeader);

        //Add the data to the table on the screen
        while (cursor.moveToNext()){
            // declare a new row
            TableRow newRow = new TableRow(getActivity());
            // add views to the row
            TextView id = new TextView(getActivity());
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_ID));
            id.setText("" + idValue);
            newRow.addView(id);
            TextView type = new TextView(getActivity());
            String typeValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Type));
            type.setText(typeValue);
            newRow.addView(type);
            TextView quantity = new TextView(getActivity());
            int quantityValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Quantity));
            quantity.setText("" + quantityValue);
            newRow.addView(quantity);
            TextView rental = new TextView(getActivity());
            int rentalValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost));
            rental.setText("" + rentalValue);
            newRow.addView(rental);
            // add the row to the table layout
            table.addView(newRow);
        }
        cursor.close();
    }

    public void setExtraSpinnerToInt(){
        //Set up the extra search drop down
        List<String> list = new ArrayList<>();
        list.add("Equal To");
        list.add("Less Than");
        list.add("Greater Than");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraSearchDropdown.setAdapter(dataAdapter);
    }
}
