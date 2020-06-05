package com.stiwy.dstorapp.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.stiwy.dstorapp.ui.dashboard.directionhelpers.FetchURL;
import com.stiwy.dstorapp.ui.dashboard.directionhelpers.TaskLoadedCallback;

import com.stiwy.dstorapp.R;

public class DashboardFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback,
        View.OnClickListener {

    private DashboardViewModel dashboardViewModel;
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    Context cont;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //setContentView(R.layout.fragment_dashboard);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapNearBy, fragment);
        transaction.commit();
        fragment.getMapAsync(this);

        getDirection = root.findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(this);
        //27.658143,85.3199503
        //27.667491,85.3208583
        place1 = new MarkerOptions().position(new LatLng(-0.155720, -78.475272)).title("Current Location");//0.155720, -78.475272
        place2 = new MarkerOptions().position(new LatLng(-0.138577, -78.471304)).title("Location 2");
        cont = getActivity();
        Log.d("mylog", String.valueOf(cont));//com.stiwy.dstorapp.MainActivity@f6be2b3
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(place1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place1.getPosition(), 15));
        mMap.addMarker(place2);
        Log.d("mylog", "Added Markers");
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onClick(View v) {
        new FetchURL(cont).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
    }
}