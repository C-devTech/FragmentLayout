package com.cdevtech.fragmentlayout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment implements OnBackPressedListener {
	int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // An ArrayAdapter connects the array to our ListView.
        // getActivity() returns a Context so we have the resources needed
        // We pass a default list item text view to put the data in and the
        // array. Create and Connect in one line
        setListAdapter(
                new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_activated_1,
                        Shakespeare.TITLES
                )
        );

        // If the screen was rotated when onSaveInstanceState() below will reload
        // the more recently selected item and store it in the global mCurCheckPosition
 		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

        // Clear all the back stacks
        getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (isDualPane()) {
            // CHOICE_MODE_SINGLE allows one item in hte ListView to be selected at a time
            // CHOICE_MODE_MULTIPLE allows multiple
            // CHOICE_MODE_NONE is the default and no item is highlighted
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Send the item selected to showDetails so the right info is shown
            showDetails(mCurCheckPosition);
        }
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure the main activity has access to this Fragment for overriding
        // when the back button is pressed
        ((MainActivity)activity).setAttachedFragment(this);
    }

    // Called every time the screen orientation changes or Android kills an activity to
    // conserve resources
    // We save the last item selected in the list here and attach it to the key curChoice
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition);
    }

    // When a list item is clicked, we want to change the info displayed
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		showDetails(pos);
	}

    // Pressing the back button restores the previously displayed Fragment and
    // resets the selected ListView index
    // Since key presses can come pretty quickly, make sure the function is finished
    // before being called again by using an object lock
    public void onBackPressed() {
        synchronized (this) {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                // If there is a stored Fragment, pop it off the stack immediately
                getFragmentManager().popBackStackImmediate();

                // Retrieve the newly displayed Fragment
                DetailsFragment details = (DetailsFragment)
                        getFragmentManager().findFragmentById(R.id.details);

                // Set the most recently selected value to the restored index
                mCurCheckPosition = details.getShownIndex();

                // Make sure the ListView has the correct item selected
                getListView().setItemChecked(mCurCheckPosition, true);
            }
        }
    }

    // Shows the data
	public void showDetails(int index) {
        // The most recently selected value in the ListView is sent
        mCurCheckPosition = index;

        // Check if we are in horizontal/landscape mode and if yes show the
        // ListView and data
        if (isDualPane()) {
            // Highlight the currently selected item
            getListView().setItemChecked(index, true);

            // Create an object that represents the current FrameLayout that we
            // will put the data in
			DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);

            // When a DetailsFragment is created by calling newInstance, the index for which
            // data it is supposed to show is passed in. If that index hasn't been assigned we
            // must assign it in the if block
			if (details == null || details.getShownIndex() != index) {
                // Make the details fragment and give it the currently selected index
				details = DetailsFragment.newInstance(index);

                // Start Fragment transactions
				FragmentTransaction ft = getFragmentManager().beginTransaction();

                // Replace any other Fragment with our new Details Fragment with the correct data
                ft.replace(R.id.details, details);

                // TRANSMIT_FRAGMENT_FADE calls for the Fragment to fade away
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                // Back button pressed when in dual pane. Reloads old Fragment to details
                // but the index is not updated. Without this, back button exits the program
				ft.addToBackStack("details");

                // Perform transaction
				ft.commit();
			}
		} else {
			// Launch the new Activity to show our DetailsFragment, which will take up the
            // entire screen in portrait mode
			Intent intent = new Intent();

			// Define the class Activity to call
			intent.setClass(getActivity(), DetailsActivity.class);

			// Pass along the currently selected index assigned to the keyword index
			intent.putExtra("index", index);

			// Call for the Activity to open
			startActivity(intent);
		}
	}

    public boolean isDualPane() {
        /*
        View detailsFrame = getActivity().findViewById(R.id.details);
        return detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        */

        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}
