package edu.umd.cs.expandedalarm.relationship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umd.cs.expandedalarm.R;


public class RelationshipFragment extends Fragment {

    RelationshipButton birthday;
    RelationshipButton anniversary;
    RelationshipButton valentine;
    RelationshipButton christmas;
    RelationshipButton father_birthday;
    RelationshipButton mother_birthday;

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

        birthday = new RelationshipButton(view, R.id.birthday, "BIRTHDAY", false);
        anniversary = new RelationshipButton(view, R.id.anniversary, "ANNIVERSARY", false);
        father_birthday = new RelationshipButton(view, R.id.father_birthday, "FATHER", false);
        mother_birthday = new RelationshipButton(view, R.id.mother_birthday, "MOTHER", false);
        valentine = new RelationshipButton(view, R.id.valentine, "VALENTINE", true);
        christmas = new RelationshipButton(view, R.id.christmas, "CHRISTMAS", true);

        return view;
    }

}
