package com.bn.promopopaplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bn.promopopaplication.Activity.ProductActivity;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.ItemClickListener;
import com.bn.promopopaplication.ProductListAdapter;
import com.bn.promopopaplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WishListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WishListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idUser";

    private String idUser;

    private List<Product> productList = new ArrayList<Product>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    public WishListFragment() {
        // Required empty public constructor
        this.idUser = "";
    }

    // TODO: Rename and change types and number of parameters
    public static WishListFragment newInstance(String idUser) {
        WishListFragment fragment = new WishListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, idUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idUser = getArguments().getString(ARG_PARAM1);

        } else {
            this.idUser = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (idUser != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("user/"+ idUser).child("wishedProducts");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        String id = snapshot.getValue().toString();

                        Log.w("FIREBASE DATABASE", ""+id);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref2 = database.getReference("product/" + id);

                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot productSnapshot) {
                                Log.w("FIREBASE DATABASE", ""+productSnapshot);
                                Product product = productSnapshot.getValue(Product.class);
                                productList.add(product);
                                Log.w("FIREBASE DATABASE", ""+productList);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }

                    mAdapter = new ProductListAdapter(productList, getContext(), R.layout.list_item);

                    ((ProductListAdapter) mAdapter).setOnItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d("TESTE", "Elemento " + position + " clicado.");
                            Intent intent = new Intent(getActivity(), ProductActivity.class);
                            intent.putExtra("produto",productList.get(position));
                            startActivity(intent);
                        }
                    });

                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("FIREBASE DATABASE", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });

            Log.w("FIREBASE DATABASE", ""+productList);
        }

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
