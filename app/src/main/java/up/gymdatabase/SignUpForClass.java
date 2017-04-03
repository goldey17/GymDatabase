package up.gymdatabase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by goldey17 on 4/3/2017.
 */

public class SignUpForClass extends Fragment implements View.OnClickListener {

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
        return inflater.inflate(R.layout.activity_sign_up_for_class, container, false);
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


    }
    @Override
    public void onClick(View view) {

    }
}

