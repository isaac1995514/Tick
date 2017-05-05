package edu.umd.cs.expandedalarm.relationship;

import android.os.Bundle;

import edu.umd.cs.expandedalarm.SingleFragmentActivity;

public class RelationshipActivity extends SingleFragmentActivity {

    @Override
    protected RelationshipFragment createFragment() {
        return RelationshipFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }
}
