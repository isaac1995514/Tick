package edu.umd.cs.expandedalarm.Relationship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umd.cs.expandedalarm.R;


public class RelationshipFragment extends Fragment {

    /**
     *
     * @return an instance of WeatherFragment
     */
    public static RelationshipFragment newInstance() {
        return new RelationshipFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_relationship, container, false);



        return view;
    }

}
