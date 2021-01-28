package com.s13.codify.Activities;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.s13.codify.Adapters.PreferencesAdapter;
import com.s13.codify.Models.PreferencesModel;
import com.s13.codify.R;
import com.s13.codify.Utils.AlertDialogHelper;
import com.s13.codify.Utils.RecyclerItemClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Preferences extends AppCompatActivity implements AlertDialogHelper.AlertDialogListener{

    ActionMode mActionMode;
    Menu context_menu;

    FloatingActionButton fab;
    RecyclerView recyclerView;
    PreferencesAdapter multiSelectAdapter;
    boolean isMultiSelect = false;

    ArrayList<PreferencesModel> user_list = new ArrayList<>();
    ArrayList<PreferencesModel> multiselect_list = new ArrayList<>();

    AlertDialogHelper alertDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        alertDialogHelper =new AlertDialogHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        data_load();

        multiSelectAdapter = new PreferencesAdapter(this,user_list,multiselect_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(multiSelectAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else
                    Toast.makeText(getApplicationContext(), "A single press would do :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<PreferencesModel>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                if(multiselect_list.size()>0) {
                    user_list.clear();
                    for(int i=0;i<multiselect_list.size();i++)
                        user_list.add(multiselect_list.get(i));
                    System.out.println(user_list+"XXX");
                    multiSelectAdapter.notifyDataSetChanged();

                    if (mActionMode != null) {
                        mActionMode.finish();
                    }
                    Toast.makeText(getApplicationContext(), "Delete Click", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Select one or more option to continue..!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//
//        switch (id) {
//            case android.R.id.home:
//            case R.id.action_exit:
//                onBackPressed();
//                return true;
//            case R.id.action_settings:
//                Toast.makeText(getApplicationContext(),"Settings Click",Toast.LENGTH_SHORT).show();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        return true;
    }

    public void data_load() {
        String name[] = {"Gokul", "Rajesh", "Ranjith", "Madhu", "Ameer", "Sonaal"};
        String posting[] = {"Manager", "HR", "Android Developer", "iOS Developer", "Team Leader", "Designer"};

        for (int i = 0; i < name.length; i++) {
            PreferencesModel mSample = new PreferencesModel(name[i], posting[i]);
            user_list.add(mSample);
        }
    }


    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(user_list.get(position)))
                multiselect_list.remove(user_list.get(position));
            else
                multiselect_list.add(user_list.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }


    public void refreshAdapter()
    {
        multiSelectAdapter.selected_usersList=multiselect_list;
        multiSelectAdapter.usersList=user_list;
        multiSelectAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            // Change this xml file to change appbar display when multiple items are selected.
            inflater.inflate(R.menu.menu_common_activity, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_delete:
//                    alertDialogHelper.showAlertDialog("","Delete Contact","DELETE","CANCEL",1,false);
//                    return true;
//                default:
//                    return false;
//            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<PreferencesModel>();
            refreshAdapter();
        }
    };

    // AlertDialog Callback Functions
    @Override
    public void onPositiveClick(int from) {

    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }
}