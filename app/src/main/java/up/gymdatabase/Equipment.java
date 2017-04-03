package up.gymdatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * Created by Jordan Goldey on 4/3/2017.
 */

public class Equipment extends Fragment implements View.OnClickListener {
    //All the stuff on the screen
    TextView title;
    TableLayout table;

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
        return inflater.inflate(R.layout.activity_equipment, container, false);
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
        title = (TextView) getActivity().findViewById(R.id.title);
        table = (TableLayout) getActivity().findViewById(R.id.table);

        //Get main so I can get the readable database
        MainActivity main = (MainActivity) getActivity();
        // Define a projection that gets all columns from the database
        String[] projection = {
                Database.Equipment.COLUMN_NAME_Equipment_ID,
                Database.Equipment.COLUMN_NAME_Equipment_Type,
                Database.Equipment.COLUMN_NAME_Equipment_Quantity,
                Database.Equipment.COLUMN_NAME_Equipment_Rental_Cost
        };
        //Call the query
        Cursor cursor = main.dbRead.query(
                Database.Equipment.TABLE_NAME,          // The table to query
                projection,                             // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

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
    @Override
    public void onClick(View view) {

    }
}
