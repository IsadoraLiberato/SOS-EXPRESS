package sosexpress.com.br.sosexpres.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import sosexpress.com.br.sosexpres.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng posicao;
    private String nomeUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMapAsync(this);
        //pegar dados de ActMapOficina
        Bundle data = getArguments();
        if (data != null) {
            String lat = data.getString("lat");
            String log = data.getString("log");
            nomeUser = data.getString("nome");
            //Toast.makeText(getContext(),"lat = "+lat,Toast.LENGTH_LONG).show();
            //Toast.makeText(getContext(),"log = "+log,Toast.LENGTH_LONG).show();
            Double latD = Double.parseDouble(lat);
            Double logD = Double.parseDouble(log);
            posicao =new LatLng(latD,logD);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMinZoomPreference(16);
        // Add a marker in Sydney and move the camera
       //Toast.makeText(getContext(),"Posicao é = "+posicao,Toast.LENGTH_LONG).show();
       mMap.addMarker(new MarkerOptions().position(posicao).title(nomeUser).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_userlocation)));

        //zoom no map
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(posicao).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
    }
}
