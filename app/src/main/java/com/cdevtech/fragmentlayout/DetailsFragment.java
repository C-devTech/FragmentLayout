package com.cdevtech.fragmentlayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	/**
	 * Static factory method that takes an int parameter,
	 * initializes the fragment's arguments, and returns the
	 * new fragment to the client.
	 */
	public static DetailsFragment newInstance(int index) {
		DetailsFragment df = new DetailsFragment();

        // Bundles are used to pass data using a key "index" and a value
		Bundle args = new Bundle();
		args.putInt("index", index);

        // Assign key value to fragment, use because Google says every fragment should
        // have an empty constructor(?)
		df.setArguments(args);

		return df;
	}

    // Called from "old code" in DetailsActivity
	public static DetailsFragment newInstance(Bundle bundle) {
		int index = bundle.getInt("index", 0);
		return newInstance(index);
	}

	// Returns the index assigned
	public int getShownIndex() {
        return getArguments().getInt("index", 0);
	}

	// LayoutInflater puts the Fragment on the screen
	// ViewGroup is the view you want to attach the Fragment to
	// Bundle stores key value pairs so that the data can be saved
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.details, container, false);
		TextView textView = (TextView) view.findViewById(R.id.text1);

		// Set the currently selected data to the TextView
		textView.setText(Shakespeare.DIALOGUE[getShownIndex()]);
		return view;

        /* New code creates the the fragment dynamically, old code uses xml
        // Create a ScrollView to put your data in
        ScrollView scroller  = new ScrollView(getActivity());

        // TextView goes in the ScrollView
        TextView text = new TextView(getActivity());

        // A TypedValue can hold multiple dimension values which can be assigned dynamically
        // applyDimensions receives the unit type to use which is COMPLEX_UNIT_DIP, which
        // is Device Independent Pixels
        // The padding amount being 4
        // The final part is information on the devices size and density
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());

        // Set the padding to the TextVIew
        text.setPadding(padding, padding, padding, padding);

        // Add the TextView to the ScrollView
        scroller.addView(text);

        // Set the currently selected data to the TextVIew
        text.setText(Shakespeare.DIALOGUE[getShownIndex()]);
        return scroller;
         */
	}
}