package io.github.abhirojp.myfeed_android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.abhirojp.myfeed_android.API.MyBackendAPI;
import io.github.abhirojp.myfeed_android.R;
import io.github.abhirojp.myfeed_android.adapter.FeedListAdapter;
import io.github.abhirojp.myfeed_android.data.DataModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.github.abhirojp.myfeed_android.data.Constants.BASE_URL;
import static io.github.abhirojp.myfeed_android.data.Constants.fragtag;

/**
 * Created by abhiroj on 6/9/17.
 */

public class FeedListFragment extends Fragment {

     public static final String TAG=FeedListFragment.class.getSimpleName();
    private static final String KEY_DATA = "LIST_FROM_BACKEND";
    private FeedListAdapter listAdapter;
    private ArrayList<DataModel> dataList;
    private Bundle instanceState;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    public static FeedListFragment newInstance() {
        FeedListFragment listFragment = new FeedListFragment();
        fragtag.put(TAG, listFragment);
        return listFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getList() != null || getList().size() > 0)
            outState.putSerializable(KEY_DATA, dataList);
    }

    private ArrayList<DataModel> getList() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        return dataList;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.instanceState = savedInstanceState;
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (instanceState != null && instanceState.getSerializable(KEY_DATA) != null) {
            dataList = (ArrayList<DataModel>) instanceState.getSerializable(KEY_DATA);
            if (dataList != null || dataList.size() > 0) {
                listAdapter.addAPIData(dataList);
            }
        } else {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            MyBackendAPI myBackendAPI = retrofit.create(MyBackendAPI.class);
            Call<ArrayList<DataModel>> call = myBackendAPI.loadData();
            call.enqueue(new Callback<ArrayList<DataModel>>() {
                @Override
                public void onResponse(Call<ArrayList<DataModel>> call, Response<ArrayList<DataModel>> response) {
                    dataList = response.body();
                    listAdapter.addAPIData(dataList);
                }

                @Override
                public void onFailure(Call<ArrayList<DataModel>> call, Throwable t) {
                    // show empty  view  or remaining list
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_frag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favorites:
                Toast.makeText(getContext(), "To implement", Toast.LENGTH_LONG).show();
                return true;
            case R.id.remove:
                Toast.makeText(getContext(), "To implement", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedlist,container,false);
        recyclerView = view.findViewById(R.id.recyclable_list);
        listAdapter=new FeedListAdapter(view.getContext());
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

/*    private ArrayList<DataModel> getFakeData() {
        Log.d(TAG,"creating list");
    // TODO: parse Fake Data as a JSON
        ArrayList<DataModel> parsedList=new ArrayList<>();
        for (int i=1;i<=20;i++){
            DataModel model=new DataModel();
            model.setTitle("title "+i);
            model.setName("name "+i);
            model.setDescription("description "+i);
            model.setText("text "+i);
            parsedList.add(model);
        }
        return parsedList;
    }*/
}
