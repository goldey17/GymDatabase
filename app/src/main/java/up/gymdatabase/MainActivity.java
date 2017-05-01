package up.gymdatabase;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The main header of the app. This contains the menu bar and the fragment for all the other pages
 * Created by Jordan Goldey on 4/3/2017.
 */
public class MainActivity extends AppCompatActivity {

    //To Access the Database
    DatabaseHelper mDbHelper = new DatabaseHelper(App.getContext());

    // Gets the data repository in write mode
    SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();

    // Gets the data repository in read mode
    SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Students student = new Students();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            student.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, student).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    /*
     * Handles the menu bar and replaces the fragment with the certain page
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_students:
                // Create fragment and give it an argument specifying the article it should show
                Students account = new Students();
                Bundle args1 = new Bundle();
                account.setArguments(args1);

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                transaction1.replace(R.id.fragment_container, account, "My Frag");

                // Commit the transaction
                transaction1.commit();
                break;
            case R.id.action_classes:
                // Create fragment and give it an argument specifying the article it should show
                Classes Classes = new Classes();
                Bundle args2 = new Bundle();
                Classes.setArguments(args2);

                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                transaction2.replace(R.id.fragment_container, Classes, "My Frag");

                // Commit the transaction
                transaction2.commit();
                break;
            case R.id.action_equipment:
                // Create fragment and give it an argument specifying the article it should show
                Equipment equip = new Equipment();
                Bundle args3 = new Bundle();
                equip.setArguments(args3);

                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                transaction3.replace(R.id.fragment_container, equip, "My Frag");

                // Commit the transaction
                transaction3.commit();
                break;
            case R.id.action_rent_equipment:
                // Create fragment and give it an argument specifying the article it should show
                RentEquipment rent = new RentEquipment();
                Bundle args4 = new Bundle();
                rent.setArguments(args4);

                FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                transaction4.replace(R.id.fragment_container, rent, "My Frag");

                // Commit the transaction
                transaction4.commit();
                break;
            case R.id.action_staff:
                // Create fragment and give it an argument specifying the article it should show
                Staff staff = new Staff();
                Bundle args6 = new Bundle();
                staff.setArguments(args6);

                FragmentTransaction transaction6 = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                transaction6.replace(R.id.fragment_container, staff, "My Frag");

                // Commit the transaction
                transaction6.commit();
                break;
            case R.id.action_teaches:
                // Create fragment and give it an argument specifying the article it should show
                Teaches teaches = new Teaches();
                Bundle args7 = new Bundle();
                teaches.setArguments(args7);

                FragmentTransaction transaction7 = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                transaction7.replace(R.id.fragment_container, teaches, "My Frag");

                // Commit the transaction
                transaction7.commit();
                break;
        }
        return true;
    }
}
