package sosexpress.com.br.sosexpres.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sosexpress.com.br.sosexpres.GeneratoRetrofit.RetrofitServiceGenerator;
import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.RestClient.RetrofitService;
import sosexpress.com.br.sosexpres.entidades.Oficina;
import sosexpress.com.br.sosexpres.entidades.Registro;
import sosexpress.com.br.sosexpres.entidades.RetornoIdOficina;
import sosexpress.com.br.sosexpres.entidades.Usuario;
import sosexpress.com.br.sosexpres.utils.Constantes;

public class MapaUsuario extends SupportMapFragment implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private ProgressDialog progress;
    private GoogleApiClient googleApiClient;
    private List<Oficina> listaOficinas;
    private Marker currentLocationMaker;
    private Marker locationOficinasMaker;
    private LatLng currentLocationLatLong;
    private List<Oficina> listOficinasMap;
    private Usuario usuario;
    private Registro registro;
    private RetornoIdOficina retornoIdOficina;
    private Oficina ofic;


    private AlertDialog alerta;
    private AlertDialog alertRegs;


    EditText edt_placa;
    EditText edt_modelo;
    EditText edt_problema;

    private int idUser;
    private  int idOficina;
    private String latUser;
    private String longUser;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Constantes.currentToken = FirebaseInstanceId.getInstance().getToken();
        registro = new Registro();
        usuario = new Usuario();
        ofic = new Oficina();


        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            idUser = extras.getInt("id");
        }


        Toast.makeText(getContext(), "Id do usuario: "+idUser, Toast.LENGTH_SHORT).show();
        listaOficinas = new ArrayList<>();
        listOficinasMap = retornaOficinas();


        this.callConnection();
        this.startGettingLocations();

        getMapAsync(this);

        //Chamando o progress
        progress = new ProgressDialog(getContext());
        progress.setTitle("Aguarde carregando mapa... ");
        progress.setCancelable(false);
        progress.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

    }

    private synchronized void callConnection() {
        Log.i("LOG", "Entrou no callConnection");
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

       /*if (googleApiClient.isConnected()) {
            Log.i("LOG", "googleClient connected");

        }else{
            Log.i("LOG", "googleClient not1 connected");
            googleApiClient.connect();
        }*/
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().getFragmentManager().popBackStack();

    }

    public List<Oficina> retornaOficinas() {

        RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);

        Call<List<Oficina>> response = service.getOficinas();

        response.enqueue(new Callback<List<Oficina>>() {
            @Override
            public void onResponse(Call<List<Oficina>> call, Response<List<Oficina>> response) {

                if (response.isSuccessful()) {
                    listaOficinas = response.body();

                    Toast.makeText(getContext(), "Chamada das oficinas OK", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Oficina>> call, Throwable t) {

            }
        });

        return listaOficinas;

    }

    @Override
    public void onLocationChanged(Location location) {

        //pegar todas as oficinas
        listOficinasMap = retornaOficinas();

        //Verifica se a lista estar vazia(So para testar)
        if (!listOficinasMap.isEmpty()) {

            Toast.makeText(getContext(), "Oficinas encontradas", Toast.LENGTH_LONG).show();

            for ( Oficina oficina : listOficinasMap) {

                //So teste
                // Toast.makeText(getContext(), "Oficina = "+oficina.getRua(), Toast.LENGTH_SHORT).show();

                LatLng posicao = getLocationFromAddress(getContext(),
                        oficina.getRua() + ", " + oficina.getNumero() + ", " + oficina.getBairro() + ", " + "Natal - RN," + oficina.getCep() + ", " + "Brasil");

                mMap.addMarker(new MarkerOptions().position(posicao)
                        .title(oficina.getNome())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ofic)));

                mMap.setOnMarkerClickListener(this);
            }

        } else {
                //Toast.makeText(getContext(), "Não foi possivel buscar as oficinas", Toast.LENGTH_LONG).show();
            Snackbar.make(getView(), "Não foi possivel buscar as oficinas",Snackbar.LENGTH_LONG).show();
                progress.dismiss();

            }

        if (currentLocationMaker != null) {
            currentLocationMaker.remove();
        }
        //Add marker
        currentLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        Log.i("LOG", "Lat " + location.getLatitude());
        Log.i("LOG", "Lang " + location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        if (currentLocationLatLong != null && mMap != null && location != null && !location.equals("")) {
            markerOptions.position(currentLocationLatLong);
            markerOptions.title("Localização atual");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_userlocation));

            currentLocationMaker = mMap.addMarker(markerOptions);
            progress.dismiss();

            registro.setLatUser(String.valueOf(location.getLatitude()));
            registro.setLongUser(String.valueOf(location.getLongitude()));

            //Move to new location
            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(14).target(currentLocationLatLong).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Toast.makeText(getContext(), "Localização atualizada", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(getContext(), "Não foi possivel obter sua posição", Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }


      /*  LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());
        mDatabase.child("location").child(String.valueOf(new Date().getTime())).setValue(locationData);
*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getContext(), "GPS ligado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getContext(), "Conectado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    //Code para permissão

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void startGettingLocations() {

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (isGPS && isNetwork) {
            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }
        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "Permissão negada", Toast.LENGTH_SHORT).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
        } else {
            Toast.makeText(getContext(), "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
        }
    }

    //pega o click no marcador do mapa
    @Override
    public boolean onMarkerClick(Marker marker) {
        //Chamando o progress
        progress = new ProgressDialog(getContext());
        progress.setTitle("Carregando dados oficina... ");
        progress.setCancelable(false);
        progress.show();

        final String nomeOficina = marker.getTitle();

        //retorna dados de uma oficina passando o nome da oficina que está no marcador
        getOficina(nomeOficina);

        return true;

    }

    private RetornoIdOficina getOficina(final String nome){


        RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);

        Call<RetornoIdOficina> call = service.getOficina(nome);

        call.enqueue(new Callback<RetornoIdOficina>() {
            private Call<RetornoIdOficina> call;
            private Response<RetornoIdOficina> response;

            @Override
            public void onResponse(Call<RetornoIdOficina> call, Response<RetornoIdOficina> response) {
                this.call = call;
                this.response = response;
                if (response.isSuccessful()) {

                    retornoIdOficina = response.body();

                    Log.i("AppCliente", "Body = "+retornoIdOficina.getMsg());
                    //verifica aqui se o corpo da resposta não é nulo
                    if ("true".equals(retornoIdOficina.getMsg())) {

                        //carrega uma view para o alert dialog
                        View v = getLayoutInflater().inflate(R.layout.layout_edt_dialog, null);
                        edt_placa = v.findViewById(R.id.edt_placa);
                        edt_modelo = v.findViewById(R.id.edt_modelo);
                        edt_problema = v.findViewById(R.id.edt_problema);
                        //add evento de click para o botao cancelar da view e fecha a janela de dialog
                        v.findViewById(R.id.btn_alert_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alerta.dismiss();
                            }
                        });
                        //add evento de click para o botao enviar da view e salva os dados do chamado
                        v.findViewById(R.id.btn_alert_enviar).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //Chamando o progress
                                progress = new ProgressDialog(getContext());
                                progress.setTitle("Salvando registro...");
                                progress.setCancelable(false);
                                progress.show();

                                String placa = edt_placa.getText().toString();
                                String modelo = edt_modelo.getText().toString();
                                String problema = edt_problema.getText().toString();

                                registro.setPlaca(placa);
                                registro.setModelo(modelo);
                                registro.setDescProblema(problema);
                                registro.setFk_id_cliente(idUser);
                                registro.setFk_id_oficina(retornoIdOficina.getId());

                                geraRegistro(registro);
                                //fecha o ProgressDialog
                                alerta.dismiss();

                            }
                        });

                        //cria um alertDiaolog com uma view de formularios para preenche od dados do chamado
                        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Preencha os campos abaixo\n"+
                                "Oficina: "+"("+nome+")");
                        builder.setView(v);

                        builder.setCancelable(false);
                        alerta = builder.create();
                        alerta.show();


                        progress.dismiss();



                    } else {
                        progress.dismiss();
                        Toast.makeText(getContext(), "Resposta nula do servidor", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getContext(), "Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                    progress.dismiss();
                }

            }
            @Override
            public void onFailure(Call<RetornoIdOficina> call, Throwable t) {
                Log.e("AppCep", "Não foi possível recuperar o Cep", t);
                progress.dismiss();
            }
        });

        return  retornoIdOficina;

    }

    //grando um registro para a oficina
    private void geraRegistro(Registro registro){

            RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);

            Call<Registro> call = service.cadastrarRegistro(registro.getPlaca(), registro.getModelo(), registro.getDescProblema(),
                    registro.getFk_id_cliente(), registro.getFk_id_oficina(),registro.getLatUser(), registro.getLogUser());

            call.enqueue(new Callback<Registro>() {
                private Call<Registro> call;
                private Response<Registro> response;

                @Override
                public void onResponse(Call<Registro> call, Response<Registro> response) {
                    this.call = call;
                    this.response = response;
                    if (response.isSuccessful()) {

                        final Registro regist = response.body();
                        Toast.makeText(getContext(), "Registro problema: "+regist.getDescProblema(), Toast.LENGTH_SHORT).show();
                        Log.i("AppCliente", "Body = "+regist.getPlaca());
                        //verifica aqui se o corpo da resposta não é nulo
                        if (regist != null) {

                            progress.dismiss();

                            AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Mensagem");
                            builder.setMessage("O registro foi salvo com sucesso");
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertRegs.dismiss();
                                }
                            });
                            builder.setCancelable(false);
                            alertRegs = builder.create();
                            alertRegs.show();



                        } else {
                            progress.dismiss();
                            Toast.makeText(getContext(), "Resposta nula do servidor", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(getContext(), "Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                        // segura os erros de requisição
                        ResponseBody errorBody = response.errorBody();
                        progress.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Registro> call, Throwable t) {
                    Log.e("AppCep", "Não foi possível recuperar o registro", t);
                    progress.dismiss();
                }
            });

        }


}
