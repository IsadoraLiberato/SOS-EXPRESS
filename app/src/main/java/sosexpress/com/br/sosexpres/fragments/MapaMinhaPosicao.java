package sosexpress.com.br.sosexpres.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapaMinhaPosicao extends SupportMapFragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private LocationManager locationManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

        pedirPermissoes();
    }

    private void pedirPermissoes() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else
            configurarServico();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMinZoomPreference(10);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            configurarServico();

        } catch (Exception e) {
            Log.e("Erro", "", e);
        }


    }

    public void setMarker(Location location)
    {
        Double latPoint = location.getLatitude();
        Double lngPoint = location.getLongitude();

        mMap.addMarker(new MarkerOptions().position(new LatLng(latPoint,lngPoint)).title("Latitude: "+latPoint+"\n"+"Longitude: "+lngPoint));
        LatLng posicao = new LatLng(latPoint,lngPoint);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicao));


    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    setMarker(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(getContext(), "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


}


