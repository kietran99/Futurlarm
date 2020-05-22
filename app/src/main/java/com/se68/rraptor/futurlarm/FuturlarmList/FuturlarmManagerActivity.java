package com.se68.rraptor.futurlarm.FuturlarmList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.se68.rraptor.futurlarm.Adapter.FuturlarmAdapter;
import com.se68.rraptor.futurlarm.Class.FirebaseHelper;
import com.se68.rraptor.futurlarm.Class.Futurlarm;
import com.se68.rraptor.futurlarm.Class.FuturlarmFilterDialog;
import com.se68.rraptor.futurlarm.Class.FuturlarmList;
import com.se68.rraptor.futurlarm.Class.User;
import com.se68.rraptor.futurlarm.R;
import com.se68.rraptor.futurlarm.Templates.TemplateActivity;

import java.util.ArrayList;

public class FuturlarmManagerActivity extends TemplateActivity implements FuturlarmFilterDialog.FuturlarmDialogListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Button btnRemoveFilter;

    private FuturlarmAdapter adapter;
    private FuturlarmList futurlarmList;
    private FuturlarmList futurlarmListFull;
    private FirebaseAuth auth;

    private int selectedPos = -1;
    private int originalPos = -1;
    public static String FUTURLARM  = "futurlarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futurlarm_manager);
        bindView();
        init();
        getData();
        btnRemoveFilter_click();
    }

    public void bindView(){
        recyclerView = findViewById(R.id.RecyclerViewFuturlarms);
        toolbar = findViewById(R.id.ToolbarFuturlarms);
        btnRemoveFilter = findViewById(R.id.ButtonRemoveFilter);
    }

    public void init(){
        futurlarmList = new FuturlarmList();
        futurlarmListFull = new FuturlarmList();
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
    }

    public void getData(){
        new FirebaseHelper().getFuturlarms(auth.getCurrentUser().getUid(), new FirebaseHelper.DataStatus() {
            @Override
            public void dataLoaded(User userData) {

            }

            @Override
            public void dataLoaded(FuturlarmList futurlarms) {
                futurlarmList.getList().addAll(futurlarms.getList());
                futurlarmListFull.setList(new ArrayList<>(futurlarmList.getList()));
                adapter = new FuturlarmAdapter(FuturlarmManagerActivity.this, futurlarmList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(FuturlarmManagerActivity.this));
                adapter.setOnItemClickListener(listener);
            }

            @Override
            public void dataInserted() {

            }

            @Override
            public void dataUpdated() {

            }

            @Override
            public void dataDeleted() {

            }
        });
    }

    FuturlarmAdapter.OnItemClickListener listener = new FuturlarmAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            selectedPos = position;
            for (int i = 0; i < futurlarmListFull.getList().size(); i++){
                if (futurlarmListFull.getList().get(i).equals(futurlarmList.getList().get(position)))
                    originalPos = i;
            }
            Intent intent = new Intent(FuturlarmManagerActivity.this, FuturlarmDetailActivity.class);
            intent.putExtra(FUTURLARM, futurlarmList.getList().get(position));
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                futurlarmList.getList().set(selectedPos, (Futurlarm) getIntent().getSerializableExtra(FuturlarmDetailActivity.FUTURLARM_RESULT));
                futurlarmListFull.getList().set(originalPos, (Futurlarm) getIntent().getSerializableExtra(FuturlarmDetailActivity.FUTURLARM_RESULT));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search_futurlarms);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //remove search icon on keyboard
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter_futurlarms:
                openFilterDialog();
                return true;
            case R.id.account_manager:
                return true;
            case R.id.sign_out:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openFilterDialog(){
        FuturlarmFilterDialog dialog = new FuturlarmFilterDialog();
        dialog.show(getSupportFragmentManager(), "Filter Dialog");
    }

    @Override
    public void applyFilter(String date, String time, boolean important) {
        btnRemoveFilter.setEnabled(true);
        btnRemoveFilter.setBackgroundResource(R.drawable.forest_green_rounded_corner_button);
        futurlarmList.getList().clear();
        futurlarmList.getList().addAll(futurlarmListFull.getList());
        adapter.notifyDataSetChanged();

        if (!date.equals("")) {
            for (int i = 0; i < futurlarmList.getList().size(); i++) {
                if (!futurlarmList.getList().get(i).getDate().equals(date)) {
                    futurlarmList.getList().remove(i);
                    i--;
                }
            }
        }

        if (!time.equals("")) {
            for (int i = 0; i < futurlarmList.getList().size(); i++) {
                if (!futurlarmList.getList().get(i).getHour().equals(time)) {
                    futurlarmList.getList().remove(i);
                    i--;
                }
            }
        }

        if (important) {
            for (int i = 0; i < futurlarmList.getList().size(); i++) {
                if (!futurlarmList.getList().get(i).isImportant()){
                    futurlarmList.getList().remove(i);
                    i--;
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
    
    public void btnRemoveFilter_click(){
        btnRemoveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                futurlarmList.getList().clear();
                futurlarmList.getList().addAll(futurlarmListFull.getList());
                adapter.notifyDataSetChanged();
                btnRemoveFilter.setEnabled(false);
                btnRemoveFilter.setBackgroundResource(R.drawable.light_green_rounded_corner_button);
            }
        });
    }
    
}
