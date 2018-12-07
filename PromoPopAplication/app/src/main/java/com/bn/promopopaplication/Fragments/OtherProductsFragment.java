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
 * {@link OtherProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherProductsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idLoja";

    // TODO: Rename and change types of parameters
    private String idLoja;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    public OtherProductsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OtherProductsFragment newInstance(String idLoja) {
        OtherProductsFragment fragment = new OtherProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, idLoja);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.idLoja = getArguments().getString(ARG_PARAM1);
        } else {
            this.idLoja = "";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_other_products, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("product/");

        ref.orderByChild("idLoja").equalTo(idLoja).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Product> productList = new ArrayList<Product>();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    productList.add(productSnapshot.getValue(Product.class));
                    Log.d("teste", "" + productList.size());
                }

                mAdapter = new ProductListAdapter(productList, getContext(), R.layout.list_item);

                ((ProductListAdapter) mAdapter).setOnItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Log.d("TESTE", "Elemento " + position + " clicado.");
                        Intent intent = new Intent(getActivity(), ProductActivity.class);
                        intent.putExtra("produto", productList.get(position));
                        startActivity(intent);
                    }
                });

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

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