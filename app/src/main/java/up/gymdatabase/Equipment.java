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
 * The screen related to the equipment in the gym
 * Created by Jordan Goldey on 4/6/2017.
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
    Button add;
    EditText searchCrit;
    EditText newID;
    EditText newType;
    EditText newQuan;
    EditText newCost;
    EditText deleteId;
    Button delete;
    Spinner searchDropdown;
    Spinner extraSearchDropdown;

    //Data for the dropdown menus
    int dropdownSelection;
    int extraDropdownSelection;
    final int ID = 0;
    final int TYPE = 1;
    final int QUANTITY = 2;
    final int RENTAL_COST = 3;
    final int EQUAL_TO = 0;
    final int LESS_THAN = 1;
    final int GREATER_THAN = 2;

    //Get main so I can get the readable and writable database
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
        add = (Button)  getActivity().findViewById(R.id.add);
        add.setOnClickListener(this);
        searchCrit = (EditText) getActivity().findViewById(R.id.search_crit);
        newID = (EditText) getActivity().findViewById(R.id.new_id);
        newType = (EditText) getActivity().findViewById(R.id.new_type);
        newQuan = (EditText) getActivity().findViewById(R.id.new_quan);
        newCost = (EditText) getActivity().findViewById(R.id.new_cost);
        deleteId = (EditText) getActivity().findViewById(R.id.old_id);
        delete = (Button) getActivity().findViewById(R.id.delete);
        delete.setOnClickListener(this);
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

        //Call the initial query
        Cursor cursor = main.dbRead.query(
                Database.Equipment.TABLE_NAME,          // The table to query
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
                redrawTable(cursor);
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
                redrawTable(cursor);
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
                redrawTable(cursor);
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
                redrawTable(cursor);
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
                redrawTable(cursor);
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
                redrawTable(cursor);
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
                redrawTable(cursor);
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
                redrawTable(cursor);
                sortCost.setText(R.string.sort_by_cost_asc);
            }
        }else if (view == search){
            //Set up the criteria
            String selection = null;
            String[] selectionArgs = {""};
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
                    selectionArgs[0] = searchCrit.getText().toString();
                    break;
                case TYPE:
                    selection = Database.Equipment.COLUMN_NAME_Equipment_Type + " = ?";
                    selectionArgs[0] = searchCrit.getText().toString();
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
                    selectionArgs[0] = searchCrit.getText().toString();
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
                    selectionArgs[0] = searchCrit.getText().toString();
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
            redrawTable(cursor);
        } else if (view == add){
            //Set the values and add to the database
            ContentValues values = new ContentValues();
            values.put(Database.Equipment.COLUMN_NAME_Equipment_ID, newID.getText().toString());
            values.put(Database.Equipment.COLUMN_NAME_Equipment_Type, newType.getText().toString());
            values.put(Database.Equipment.COLUMN_NAME_Equipment_Quantity, newQuan.getText().toString());
            values.put(Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost, newCost.getText().toString());
            main.dbWrite.insert(Database.Equipment.TABLE_NAME, null, values);
            //Update data on screen
            Cursor cursor = main.dbRead.query(
                    Database.Equipment.TABLE_NAME,          // The table to query
                    allColumnsProjection,                   // The columns to return
                    null,                                   // The columns for the WHERE clause
                    null,                                   // The values for the WHERE clause
                    null,                                   // don't group the rows
                    null,                                   // don't filter by row groups
                    null                                    // The sort order
            );
            redrawTable(cursor);
        }else if(view == delete){
            // Define 'where' part of query.
            String selection = Database.Equipment.COLUMN_NAME_Equipment_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { deleteId.getText().toString() };
            // Issue SQL statement.
            main.dbWrite.delete(Database.Equipment.TABLE_NAME, selection, selectionArgs);
            //Call the initial query
            Cursor cursor = main.dbRead.query(
                    Database.Equipment.TABLE_NAME,          // The table to query
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
            TextView id = new TextView(getActivity());
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_ID));
            String text = Integer.toString(idValue);
            id.setText(text);
            newRow.addView(id);
            TextView type = new TextView(getActivity());
            String typeValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Type));
            type.setText(typeValue);
            newRow.addView(type);
            TextView quantity = new TextView(getActivity());
            int quantityValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Quantity));
            text = Integer.toString(quantityValue);
            quantity.setText(text);
            newRow.addView(quantity);
            TextView rental = new TextView(getActivity());
            int rentalValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost));
            text = Integer.toString(rentalValue);
            rental.setText(text);
            newRow.addView(rental);
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
        extraSearchDropdown.setAdapter(dataAdapter);
    }
}
