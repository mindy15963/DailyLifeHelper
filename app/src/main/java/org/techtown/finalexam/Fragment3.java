package org.techtown.finalexam;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class Fragment3 extends Fragment implements AutoPermissionsListener {
    SupportMapFragment mapFragment;
    GoogleMap map;

    MarkerOptions myLocationMarker;

    //현재 위치 확인 기능
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            //지도 준비 완료
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map = googleMap;
            }
        });
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button button = rootView.findViewById(R.id.button3A);
        //내 위치 요청하기
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                try {
                    Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
                        Log.d("Map", message);
                    }
                    GPSListener gpsListener = new GPSListener();
                    long minTime = 10000;
                    float minDistance = 0;
                    manager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            minTime, minDistance, gpsListener);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
            }
        });
        AutoPermissions.Companion.loadAllPermissions(getActivity(), 101);
        return rootView;
    }

    //내 위치 확인
    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.d("Map", message);
            showCurrentLocation(latitude, longitude);
        }
        public void onProviderDisabled(String provider) { }
        public void onProviderEnabled(String provider) { }
        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }
    //현재 위치 보여주기
    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
        showMyLocationMarker(curPoint);
    }
    //현재 위치 확인하기
    private void showMyLocationMarker(LatLng curPoint) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(curPoint);
            myLocationMarker.title("● 내 위치\n");
            myLocationMarker.snippet("● GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myLocationMarker);
        } else {
            myLocationMarker.position(curPoint);
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myLocationMarker);
        }
    }
    //요청 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(getActivity(), requestCode, permissions, (AutoPermissionsListener) this);
    }
    //요청 거부
    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(getActivity(), "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }
    //요청 승인
    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(getActivity(), "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }
}