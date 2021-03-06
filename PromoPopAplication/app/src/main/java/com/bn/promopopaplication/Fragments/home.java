package com.bn.promopopaplication.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bn.promopopaplication.Activity.MainActivity;
import com.bn.promopopaplication.Entity.Product;
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
     * {@link OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link home#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class home extends android.support.v4.app.Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        private final ProductGrid pg = new ProductGrid();
        private final ProductList pl = new ProductList();

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        private OnFragmentInteractionListener mListener;

        public home() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        public static home newInstance(String param1, String param2) {
            home fragment = new home();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);

            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View view = inflater.inflate(R.layout.fragment_home, container, false);

            FragmentTransaction fragment = getFragmentManager().beginTransaction();
            fragment.replace(R.id.fragment_container, pg).commit();

            ImageButton filter = view.findViewById(R.id.filter);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // Quando o evento de click no filtro é chamado, eu pego activitivy resposavel pelo fragment, faço o cast para a
                    // classe java responsável e chamo o método showCategories()
                    ((MainActivity)getActivity()).showCategories(view);
                }
            });


            final ImageButton forGrid = view.findViewById(R.id.btn_grid);
            final ImageButton forList = view.findViewById(R.id.btn_list);

            forGrid.setVisibility(View.GONE);

            forList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragment = getFragmentManager().beginTransaction();
                    fragment.replace(R.id.fragment_container, pl).commit();
                    forList.setVisibility(View.GONE);
                    forGrid.setVisibility(View.VISIBLE);
                }
            });

            forGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragment = getFragmentManager().beginTransaction();
                    fragment.replace(R.id.fragment_container, pg).commit();
                    forList.setVisibility(View.VISIBLE);
                    forGrid.setVisibility(View.GONE);
                }
            });

            return view;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){

            if(view.getId()==R.id.filter){
                getActivity().getMenuInflater().inflate(R.menu.categories_menu,menu);
            }

            super.onCreateContextMenu(menu, view, menuInfo);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item){
            switch (item.getItemId()){
                case R.id.bebes:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a bebês e crianças", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.bebidas:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a bebidas", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.hobbies:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a brinquedos e hobbies", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.eletronicos:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a eletrônicos, aúdio e vídeo", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.eletrodomesticos:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a eletrodomésticos", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.games:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a games", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mais:
                    Toast.makeText(getContext(), item.getTitle()+" Produtos da relacionados a mais", Toast.LENGTH_SHORT).show();
                    break;

            }
            return super.onContextItemSelected(item);
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
