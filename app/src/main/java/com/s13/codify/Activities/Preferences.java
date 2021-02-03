package com.s13.codify.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.s13.codify.Adapters.PreferencesAdapter;
import com.s13.codify.MainActivity;
import com.s13.codify.Models.ModelClasses;
import com.s13.codify.Models.PreferencesModel;
import com.s13.codify.R;
import com.s13.codify.Room.ModelClasses.ModelClass;
import com.s13.codify.Room.ModelClasses.ModelClassesRepo;
import com.s13.codify.Utils.AlertDialogHelper;
import com.s13.codify.Utils.RecyclerItemClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.s13.codify.Models.ModelClasses.MODEL_CLASSES;

public class Preferences extends AppCompatActivity implements AlertDialogHelper.AlertDialogListener {

    private static final int N_THREADS = 10;
    ActionMode mActionMode;
    Menu context_menu;

    FloatingActionButton fab;
    RecyclerView recyclerView;
    PreferencesAdapter multiSelectAdapter;
    boolean isMultiSelect = false;

    ArrayList<PreferencesModel> user_list = new ArrayList<>();
    ArrayList<PreferencesModel> multiselect_list = new ArrayList<>();

    AlertDialogHelper alertDialogHelper;

    ModelClassesRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        insert_data();
        alertDialogHelper = new AlertDialogHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        repo = new ModelClassesRepo(getApplication());

        multiSelectAdapter = new PreferencesAdapter(this, user_list, multiselect_list);
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
        data_load();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
//                Intent main = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(main);

                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(main);

                setContentView(R.layout.activity_main);
                if (multiselect_list.size() > 0) {
                    for(int i = 0;i< MODEL_CLASSES.length;i++){
                        ModelClass modelClass = new ModelClass();
                        modelClass.setClassName(MODEL_CLASSES[i]);
                        modelClass.setSelected(false);
                        repo.updatePreference(modelClass);
                    }
                    for(int i = 0; i<multiselect_list.size();i++) {
                        ModelClass modelClass = new ModelClass();
                        modelClass.setClassName(multiselect_list.get(i).getLabel());
                        modelClass.setSelected(true);
                        repo.updatePreference(modelClass);
                    }


                    Toast.makeText(getApplicationContext(), "Delete Click", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Select one or more option to continue..!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void data_load() {

        LiveData<List<ModelClass>> modelClasses = repo.getModelClasses();
        modelClasses.observe(this, new Observer<List<ModelClass>>() {
            @Override
            public void onChanged(List<ModelClass> modelClasses) {
                ArrayList<PreferencesModel> userList = new ArrayList<>();
                for (int i = 0; i < modelClasses.size(); i++) {
                    PreferencesModel mSample = new PreferencesModel(modelClasses.get(i).getClassName(), "Soman");
                    userList.add(mSample);
                }
                multiSelectAdapter.setUsersList(userList);
                recyclerView.setAdapter(multiSelectAdapter);
                user_list = userList;
            }
        });
    }

    public void insert_data() {
        ModelClassesRepo repo = new ModelClassesRepo(getApplication());
        String[] modelClasses = MODEL_CLASSES;
        for (int i = 0; i < modelClasses.length; i++) {
            ModelClass modelClass = new ModelClass();
            modelClass.setClassName(modelClasses[i]);
            modelClass.setSelected(false);
            repo.insert(modelClass);
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


    public void refreshAdapter() {
        multiSelectAdapter.selected_usersList = multiselect_list;
        multiSelectAdapter.usersList = user_list;
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