package br.com.tads.estarfiscal;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.tads.estarfiscal.adapter.EnderecoAdapter;
import br.com.tads.estarfiscal.model.Estar;
import br.com.tads.estarfiscal.retrofit.ApiManager;
import br.com.tads.estarfiscal.retrofit.CustomCallback;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    ApiManager apiManager;
    EnderecoAdapter adapter;
    RelativeLayout relativemain;
    ListView listView;
    List<Estar> listdata = new ArrayList<>();
    SwipeRefreshLayout swipe;
    private Handler handler;
    private GoogleApiClient mGoogleApiClient;
    Location lastLocation;
    String error = "";
    public static final int  REQUEST_PERMISSIONS_CODE = 128;
    String filter = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("EstaR Fiscal");
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            setSupportActionBar(toolbar);
        }


        listView = (ListView) findViewById(R.id.listView);
        relativemain = (RelativeLayout) findViewById(R.id.relativemain);
        apiManager = new ApiManager(this);


        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
            }
        });

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
        }else{
                callConnection();

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Estar estar = (Estar) parent.getItemAtPosition(position);
                Intent it = new Intent(getBaseContext(),DescriptionActivity.class);
                it.putExtra("estar",estar);
                startActivity(it);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void getdata(){

        final Gson gson = new Gson();
        listdata.clear();

        apiManager.getEstar(new CustomCallback<ResponseBody>(this, relativemain, new CustomCallback.OnResponse<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.string());
                    if(jsonObject.get("result").toString().equalsIgnoreCase("1")) {

                            final JSONArray jsonArray = jsonObject.getJSONArray("content");
                            Type type = new TypeToken<ArrayList<Estar>>() {
                            }.getType();
                            listdata = gson.fromJson(jsonArray.toString(), type);


                        adapter = new EnderecoAdapter(getBaseContext(),listdata);
                        listView.setAdapter(adapter);
                        if(listdata.size() == 0){
                            Toast.makeText(MainActivity.this, "Não há dados correspondentes ao seu filtro.", Toast.LENGTH_SHORT).show();
                            dialogFilter();
                        }
                            Log.e("response", "response");
                    }else {

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
                getdata();
            }
        }), filter ,lastLocation);


        swipe.setRefreshing(false);

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
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );

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

        if(lastLocation != null) {
            LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            getdata();
        }else{
            Toast.makeText(getBaseContext(), "Sua localização não foi encontrada. Verifique se seu celular está funcionando corretamente.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            dialogFilter();
            return true;
        }

        return false;
    }


    public void dialogFilter(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);

        LinearLayout relativeLayout1 = (LinearLayout) dialog.findViewById(R.id.relativeLayout1);
        final CheckBox checkBoxHorario = (CheckBox) dialog.findViewById(R.id.checkBox);
        final CheckBox checkVencidos = (CheckBox) dialog.findViewById(R.id.checkVencidos);
        final CheckBox checkAtivos = (CheckBox) dialog.findViewById(R.id.checkAtivos);
        final CheckBox checkPerto = (CheckBox) dialog.findViewById(R.id.checkPerto);
        Button btncloseDialog = (Button) dialog.findViewById(R.id.btncloseDialog);
        TextView txtTituloAlert = (TextView) dialog.findViewById(R.id.txtTituloAlert);

        dialog.show();

        checkBoxHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAtivos.setChecked(false);
                checkVencidos.setChecked(false);
                checkPerto.setChecked(false);
            }
        });

        checkPerto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAtivos.setChecked(false);
                checkVencidos.setChecked(false);
                checkBoxHorario.setChecked(false);
            }
        });

        checkAtivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxHorario.setChecked(false);
                checkVencidos.setChecked(false);
                checkPerto.setChecked(false);
            }
        });

        checkVencidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxHorario.setChecked(false);
                checkAtivos.setChecked(false);
                checkPerto.setChecked(false);
            }
        });

        btncloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPerto.isChecked()){
                    filter = "0";
                    getdata();
                    dialog.dismiss();
                }
                if(checkAtivos.isChecked()){
                    filter = "1";
                    getdata();
                    dialog.dismiss();
                }
                if(checkVencidos.isChecked()){
                    filter = "2";
                    getdata();
                    dialog.dismiss();
                }
                if(checkBoxHorario.isChecked()){
                    filter = "3";
                    getdata();
                    dialog.dismiss();
                }
                else if (!checkBoxHorario.isChecked() && !checkPerto.isChecked() && !checkAtivos.isChecked() && !checkVencidos.isChecked()){
                    Toast.makeText(MainActivity.this, "É necessario escolher 1 opção!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
