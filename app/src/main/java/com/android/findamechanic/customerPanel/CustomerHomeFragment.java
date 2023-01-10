package com.android.findamechanic.customerPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.findamechanic.R;
import com.android.findamechanic.UpdateAdModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView mRecyclerView;
    private List<UpdateAdModel> updateAdModelList;
    private CustomerHomeAdapter adapter;
    String province, suburb;
    DatabaseReference dataa, mDatabaseReference;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_home, null);

        getActivity().setTitle("Home");
        setHasOptionsMenu(true);

        mRecyclerView = v.findViewById(R.id.customer_recycler_menu);
        mRecyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        mRecyclerView.startAnimation(animation);


        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        updateAdModelList = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.spareSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.white);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                dataa = FirebaseDatabase.getInstance().getReference("Customer").child(userId);
                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Customer customer = snapshot.getValue(Customer.class);
                        province = customer.getProvince();
                        suburb = customer.getSuburb();
                        customerMenu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        return v;
    }

    @Override
    public void onRefresh() {

        customerMenu();
    }

    private void customerMenu() {
        swipeRefreshLayout.setRefreshing(true);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("SpareAdDetails")
                .child(province).child(suburb);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateAdModelList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateAdModel updateAdModel = snapshot1.getValue(UpdateAdModel.class);
                        updateAdModelList.add(updateAdModel);
                    }
                }

                adapter = new CustomerHomeAdapter(getContext(), updateAdModelList);
                mRecyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    private void search(String searchText) {

        ArrayList<UpdateAdModel> myList = new ArrayList<>();
        for (UpdateAdModel object : updateAdModelList) {
            if(object.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                myList.add(object);
            }
        }

        adapter = new CustomerHomeAdapter(getContext(), myList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.SearchAd);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");
    }


}
