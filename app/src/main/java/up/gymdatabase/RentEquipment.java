package up.gymdatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * The screen related to renting the equipment in the gym
 * Created by Jordan Goldey on 4/8/2017.
 */
public class RentEquipment extends Fragment implements View.OnClickListener {

    //All the widgets on the screen
    EditText type;
    EditText sID;
    NumberPicker quantity;
    NumberPicker days;
    Button checkQuan;
    Button checkCost;
    TextView cost;
    Button rent;
    TableLayout table;
    TableRow tableHeader;

    //Number Picker Values
    int daysValue;
    int quanValue;

    int equipID = -1;
    int studentID = -1;

    //Get main so I can get the readable and writable database
    MainActivity main;

    // Define a projection that gets all columns from the database
    String[] allColumnsProjection = {
            Database.Rents.COLUMN_NAME_Equipment_ID,
            Database.Rents.COLUMN_NAME_Student_ID,
            Database.Rents.COLUMN_NAME_Equipment_Quantity,
            Database.Rents.COLUMN_NAME_Days
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
        return inflater.inflate(R.layout.activity_rent_equipment, container, false);
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
        type = (EditText) getActivity().findViewById(R.id.equip_type);
        quantity = (NumberPicker) getActivity().findViewById(R.id.num_to_rent);
        quantity.setVisibility(View.GONE);
        quantity.setWrapSelectorWheel(true);
        quantity.setMinValue(0);
        quantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                quanValue = newVal;
            }
        });
        days = (NumberPicker) getActivity().findViewById(R.id.days_to_rent);
        days.setWrapSelectorWheel(true);
        days.setMinValue(0);
        days.setMaxValue(7);
        days.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                daysValue = newVal;
            }
        });
        checkQuan = (Button) getActivity().findViewById(R.id.check_quan);
        checkQuan.setOnClickListener(this);
        checkCost = (Button) getActivity().findViewById(R.id.check_cost);
        checkCost.setOnClickListener(this);
        rent = (Button) getActivity().findViewById(R.id.rent);
        rent.setOnClickListener(this);
        cost = (TextView) getActivity().findViewById(R.id.cost_header);
        table = (TableLayout) getActivity().findViewById(R.id.table);
        tableHeader = (TableRow) getActivity().findViewById(R.id.table_header);
        sID = (EditText) getActivity().findViewById(R.id.student_id);

        //Set up main
        main = (MainActivity) getActivity();

        //Call the initial query
        Cursor cursor = main.dbRead.query(
                Database.Rents.TABLE_NAME,              // The table to query
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
    public void onClick(View view) {
        // Checks the number of items that can be rented of the given type
        if(view == checkQuan){
            // Check quantity in the equipment table
            String[] projection = {
                    Database.Equipment.COLUMN_NAME_Equipment_Quantity,
                    Database.Equipment.COLUMN_NAME_Equipment_ID
            };
            String selection = Database.Equipment.COLUMN_NAME_Equipment_Type + " = ?";
            String[] selectionArgs = {type.getText().toString()};
            //Call the query
            Cursor cursor = main.dbRead.query(
                    Database.Equipment.TABLE_NAME,              // The table to query
                    projection,                                 // The columns to return
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );
            //Add the quantity to the number picker
            int quantityValue = -1;
            while (cursor.moveToNext()){
                quantityValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Quantity));
                quantity.setMaxValue(quantityValue);
                quantity.setVisibility(View.VISIBLE);
                equipID = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_ID));
            }
            // If the type isn't valid clear the number picker and hide it
            if(quantityValue == -1){
                quantity.setVisibility(View.GONE);
                quantity.setMaxValue(0);
            }
            cursor.close();
            // Find the number of already rented of that type and subtract from total
            String[] projection1 = {
                    Database.Rents.COLUMN_NAME_Equipment_Quantity
            };
            String selection1 = Database.Rents.COLUMN_NAME_Equipment_ID + " = ?";
            String[] selectionArgs1 = {Integer.toString(equipID)};
            //Call the query
            Cursor cursor1 = main.dbRead.query(
                    Database.Rents.TABLE_NAME,              // The table to query
                    projection1,                                 // The columns to return
                    selection1,                                  // The columns for the WHERE clause
                    selectionArgs1,                              // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );
            quantityValue = 0;
            while (cursor1.moveToNext()){
                quantityValue = cursor1.getInt(cursor1.getColumnIndexOrThrow(Database.Rents.COLUMN_NAME_Equipment_Quantity));
                quantity.setMaxValue(quantity.getMaxValue() - quantityValue);
            }
        }else if(view == checkCost){
            String[] projection = {
                    Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost
            };
            String selection = Database.Equipment.COLUMN_NAME_Equipment_Type + " = ?";
            String[] selectionArgs = {type.getText().toString()};
            //Call the query
            Cursor cursor = main.dbRead.query(
                    Database.Equipment.TABLE_NAME,              // The table to query
                    projection,                                 // The columns to return
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );
            //Add the data to the table on the screen
            while (cursor.moveToNext()){
                int rentalCost = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost));
                String costValue = "Cost: $" + Integer.toString(rentalCost * quanValue * daysValue) + ".00";
                cost.setText(costValue);
            }
            cursor.close();
        }else if (view == rent){
            //Set the values and add to the database
            ContentValues values = new ContentValues();
            values.put(Database.Rents.COLUMN_NAME_Equipment_ID, equipID);
            values.put(Database.Rents.COLUMN_NAME_Student_ID, sID.getText().toString());
            values.put(Database.Rents.COLUMN_NAME_Equipment_Quantity, quantity.getValue());
            values.put(Database.Rents.COLUMN_NAME_Days, days.getValue());
            main.dbWrite.insert(Database.Rents.TABLE_NAME, null, values);
            Cursor cursor = main.dbRead.query(
                    Database.Rents.TABLE_NAME,              // The table to query
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
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Rents.COLUMN_NAME_Equipment_ID));
            String text = Integer.toString(idValue);
            id.setText(text);
            newRow.addView(id);
            TextView type = new TextView(getActivity());
            String typeValue = cursor.getString(cursor.getColumnIndexOrThrow(Database.Rents.COLUMN_NAME_Student_ID));
            type.setText(typeValue);
            newRow.addView(type);
            TextView quantity = new TextView(getActivity());
            int quantityValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Rents.COLUMN_NAME_Equipment_Quantity));
            text = Integer.toString(quantityValue);
            quantity.setText(text);
            newRow.addView(quantity);
            TextView rental = new TextView(getActivity());
            int rentalValue = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Rents.COLUMN_NAME_Days));
            text = Integer.toString(rentalValue);
            rental.setText(text);
            newRow.addView(rental);
            // add the row to the table layout
            table.addView(newRow);
        }
        cursor.close();
    }
}
