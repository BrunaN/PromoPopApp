package com.bn.promopopaplication.Fragments;

import android.Manifest;
import android.app.MediaRouteButton;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bn.promopopaplication.Activity.ProductActivity;
import com.bn.promopopaplication.Activity.StoreActivity;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.ItemClickListener;
import com.bn.promopopaplication.ProductListAdapter;
import com.bn.promopopaplication.R;
import com.bn.promopopaplication.Services.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link map.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Store> storeList;
    private Map<String, Integer> mMarkers;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LatLng ny;
    private boolean refresh;


    private OnFragmentInteractionListener mListener;

    public map() {
        // Required empty public constructor
        refresh = false;
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double lat, lng;
            lat = intent.getDoubleExtra("lat", ny.latitude);
            lng = intent.getDoubleExtra("lng", ny.longitude);
            ny = new LatLng(lat, lng);
            if(!refresh) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                refresh = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this); //this is important


        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app");
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(receiver, intentFilter);

        Intent i = new Intent(getContext(), LocationService.class);
        getActivity().startService(i);

        return v;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        ny = new LatLng(-4.9684385, -39.0161259);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        mGoogleMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stores/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                 storeList = new ArrayList<Store>();
                 mMarkers = new HashMap<String, Integer>();


                for (DataSnapshot storeSnapshot: snapshot.getChildren()) {
                    storeList.add(storeSnapshot.getValue(Store.class));
                }

                mGoogleMap.clear();
                mMarkers.clear();

                int i = 0;

                for(Store store: storeList){
                    MarkerOptions markerOptions = new MarkerOptions();
                    Log.d("Endereço", store.getEndereco()+", "+store.getCidade());
                    LatLng ll = getLocationFromAddress(getContext(), store.getEndereco()+", "+store.getCidade());
                    if(ll != null) {
                        Log.d("Endereço", ll.latitude + " " + ll.longitude);
                        markerOptions.position(ll)
                                .snippet("Clique para ver o perfil")
                                .title(store.getStoreName());
                        Marker mkr = mGoogleMap.addMarker(markerOptions);

                        mMarkers.put(mkr.getId(), i);
                    }

                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mGoogleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map.
     */
    // TODO: Rename and change types and number of parameters
    public static map newInstance(String param1, String param2) {
        map fragment = new map();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Integer pos = mMarkers.get(marker.getId());
        if(pos != null) {
            Store store = storeList.get(pos);
           
            Intent i = new Intent(getContext(), StoreActivity.class);
            i.putExtra("store", store);
            startActivity(i);
            getActivity().finish();
        }
    }
}
