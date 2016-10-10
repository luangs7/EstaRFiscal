package br.com.tads.estarfiscal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import br.com.tads.estarfiscal.adapter.EnderecoAdapter;
import br.com.tads.estarfiscal.model.Estar;
import br.com.tads.estarfiscal.retrofit.ApiManager;
import br.com.tads.estarfiscal.retrofit.CustomCallback;
import okhttp3.ResponseBody;

public class DescriptionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbar;
    private TextView textView;
    private TextView txtPlaca;
    private TextView textView2;
    private TextView txtHora;
    private TextView textView3;
    private TextView txtEntrada;
    private TextView txtEndereco;
    private LinearLayout linearbuttons;
    LinearLayout parent;
    private GoogleMap mMap;
    public static final int  REQUEST_PERMISSIONS_CODE = 128;
    private GoogleApiClient mGoogleApiClient;
    Location lastLocation;
    Estar estar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Descrição");
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            toolbar.setNavigationIcon(R.drawable.ic_action_name);
            setSupportActionBar(toolbar);
        }

        Intent it = getIntent();
        estar = (Estar) it.getSerializableExtra("estar");

        if(estar == null){
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView = (TextView) findViewById(R.id.textView);
        txtPlaca = (TextView) findViewById(R.id.txtPlaca);
        textView2 = (TextView) findViewById(R.id.textView2);
        txtEntrada = (TextView) findViewById(R.id.txtEntrada);
        txtHora = (TextView) findViewById(R.id.txtHora);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtEndereco = (TextView) findViewById(R.id.txtEndereco);
        linearbuttons = (LinearLayout) findViewById(R.id.linearbuttons);
        parent = (LinearLayout) findViewById(R.id.parent);
        Button btnMultar = (Button) findViewById(R.id.btnMultar);
        Button btnRetirar = (Button) findViewById(R.id.btnRetirar);


        txtPlaca.setText(estar.getPlaca());
        txtEndereco.setText(estar.getAddress());
        txtHora.setText(estar.getHoras());
        txtEntrada.setText(estar.getInicio());

        btnRetirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retirardata();
            }
        });

        btnMultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificardata();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        apiManager = new ApiManager(this);
    }

    private synchronized void callConnection(){
        Log.i("LOG", "LastLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );

            }else{
                lastLocation = LocationServices
                        .FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }else{
            lastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Add a marker in Sydney and move the camera
        if(lastLocation != null) {
            LatLng location = new LatLng(Double.parseDouble(estar.getLatitude()), Double.parseDouble(estar.getLongitude()));
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(location).title("Essa é a posição do carro"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17));


        }else{
            Toast.makeText(this, "Sua localização não foi encontrada. Verifique se seu celular está funcionando corretamente.", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        callConnection();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    ApiManager apiManager;
    public void retirardata(){

        final Gson gson = new Gson();


        apiManager.retirar(new CustomCallback<ResponseBody>(this, parent, new CustomCallback.OnResponse<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.string());
                    if(jsonObject.get("result").toString().equalsIgnoreCase("1")) {

                        new AlertDialog.Builder(DescriptionActivity.this)
                                .setTitle("Sucesso")
                                .setMessage("Veículo foi removido com sucesso!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                                        finish();
                                    }})
                                .show();

                        Log.e("response", "response");
                    }else {
                        new AlertDialog.Builder(DescriptionActivity.this)
                                .setTitle("ERRO!")
                                .setMessage("Erro ao realizar operação! Deseja realizar novamente?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        retirardata();
                                    }})
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Exception", t.getMessage());
            }

            @Override
            public void onRetry(Throwable t) {
                retirardata();
            }
        }), estar);

    }

    public void notificardata(){

        final Gson gson = new Gson();


        apiManager.notificar(new CustomCallback<ResponseBody>(this, parent, new CustomCallback.OnResponse<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.string());
                    if(jsonObject.get("result").toString().equalsIgnoreCase("1")) {

                        new AlertDialog.Builder(DescriptionActivity.this)
                                .setTitle("Sucesso")
                                .setMessage("O motorista foi notificado!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                                        finish();
                                    }})
                                .show();

                        Log.e("response", "response");
                    }else {
                        new AlertDialog.Builder(DescriptionActivity.this)
                                .setTitle("ERRO!")
                                .setMessage("Erro ao realizar operação! Deseja realizar novamente?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        retirardata();
                                    }})
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    new AlertDialog.Builder(DescriptionActivity.this)
                            .setTitle("Sucesso")
                            .setMessage("O motorista foi notificado!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }})
                            .show();

                    Log.e("response", "response");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Exception", t.getMessage());
            }

            @Override
            public void onRetry(Throwable t) {
                retirardata();
            }
        }), estar);

    }
}
