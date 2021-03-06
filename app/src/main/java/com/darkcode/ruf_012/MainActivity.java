package com.darkcode.ruf_012;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.darkcode.ruf_012.Diagrama.DienteService;
import com.darkcode.ruf_012.Diagrama.VistaGetDiagrama;
import com.darkcode.ruf_012.Diagrama.VistaRegDiagrama;
import com.darkcode.ruf_012.Paciente.PacienteService;
import com.darkcode.ruf_012.Paciente.VistaRegPaciente;
import com.darkcode.ruf_012.Tratamientos.AdapterTratsConsulta;
import com.darkcode.ruf_012.Tratamientos.Tratamiento;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Runnable,Communicator{

    TextView name;
    int temp =0;
    int actual =0;
    int cont = 0;
    Thread t,t1;
    int  escucha;
    Object vistaA;
    FloatingActionButton btnUniversal;
    RestAdapter restadpter = new RestAdapter.Builder().setEndpoint("http://linksdominicana.com").build();




    public String vistaActual="principal";
    //VARIABLE DE TRATS REALIZADOS
    private ArrayList<AdapterTratsConsulta.checkItem> ite = new ArrayList<AdapterTratsConsulta.checkItem>();
    private ArrayList<AdapterTratsConsulta.checkItem> itemRegPlan = new ArrayList<AdapterTratsConsulta.checkItem>();

    public ArrayList<AdapterTratsConsulta.checkItem> getItemRegPlan() {
        return itemRegPlan;
    }



    public void hideBtnUnivesal(String vistaAct){
        getSupportActionBar().setTitle( vistaActual);
        if(vistaAct!="Diagrama" && vistaAct!="Nuevo Paciente"){
            btnUniversal.hide();
        }else{
            btnUniversal.show();
        }
    }

    public void setIte(ArrayList<AdapterTratsConsulta.checkItem> ite) {
        this.ite = ite;
    }
    public void setItemRegPlan(ArrayList<AdapterTratsConsulta.checkItem> itemRegPlan) {
        this.itemRegPlan = itemRegPlan;
    }

    public String getVistaActual() {
        return vistaActual;
    }

    public void setVistaActual(String vistaActual) {
        this.vistaActual = vistaActual;
    }

    public int getUltimo_plan() {
        return ultimo_plan;
    }

    public void setUltimo_plan(int ultimo_plan) {
        this.ultimo_plan = ultimo_plan;
    }

    public int getId_pacienteA() {
        return id_pacienteA;
    }

    public void setId_pacienteA(int id_pacienteA) {
        this.id_pacienteA = id_pacienteA;
    }

//===========================
//    VARIABLES DE PACIENTE
    String NOMBRES,SEXO,OCUPACION,DIRECCION,TELEFONO,ESTADO_CIVIL,DIRECCION_OCU,TELEFONO_OCU,ALLEGADO;
    int EDAD;
    int id_pacienteA;
    int ultimo_plan;
//===========================


    private InputStream mmInStream;
    private Handler mHandler;

    String btNombre;
    boolean init= false;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Button ecu = (Button)findViewById(R.id.)


        mHandler = new Handler();
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //recivimos la mac address obtenida en la actividad anterior

        if(address.equals("sinBT")){
            Log.d("sin BT","Recibido:"+address);
        }else {
//        Toast.makeText(this,"ADD: "+address, Toast.LENGTH_LONG).show();
            new ConnectBT().execute(); //Call the class to connect
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnUniversal = (FloatingActionButton) findViewById(R.id.fab);
        btnUniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(init==false) {
//                    permitirEscuchar();
                    Escuchar();
                    init=true;
                }else{ Escuchar();} // PARA PRUEBAS SIN ARDUINO QUITAR LUEGO DE PROBAR
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.support.v4.app.Fragment vista = new VistaPrincipal();
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.f_main, vista);
        transaction.addToBackStack(null);
        transaction.commit();
    }


//      ===============================================
//      |   COMPRUEBA SI EL ESTADO DEL BOTON ES NUEVO  |
//      ===============================================
    private boolean ButtonPress(){
        boolean state = false;
//        if(escucha > temp) {
        if(escucha>0){
            temp = escucha;
            state = true;
        }else{
//            actual = escucha;
            state = false;
        }
//        }
        return state;
    }


//    =================================
//    |   CAPTAR PULSO DEL PEDAL        |
//    =================================
    private void permitirEscuchar()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TF".toString().getBytes());
                escucha = btSocket.getInputStream().available();
                btSocket.getInputStream().read();
//                int  escucha = btSocket.getInputStream().available(); //ESTE FUNCIONA PARO SUMA DE 3 EN 3

                start();


            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

//  =================================
//  |   INSTANCIAMOS EL GOOGLE NOW  |
//  =================================
    private  void Escuchar(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//Especificamos el idioma, en esta ocasión probé con el de Estados Unidos
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS ,"99999999999999999999");
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS ,"99999999999999999999");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
        //Iniciamos la actividad dentro de un Try en caso sucediera un error.
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Tu dispositivo no soporta el reconocimiento de voz", Toast.LENGTH_LONG).show();
        }

    }
    private void msg(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

//    ============================
//         THREAD PRINCIPAL BT
//    ============================
    @Override
    public void run() {
        t1 = Thread.currentThread();
        byte[] buffer = new byte[1024];
        int bytes;
        InputStream tmpIn = null;

        try {
            tmpIn = btSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmInStream = tmpIn;
//        Message msg = mHandler.obtainMessage(444);

        while(t1 == t){

            try{
                t1.sleep(800);

                escucha = btSocket.getInputStream().available();
//                String readMessage = new String(buffer, 0, actual);
                if(ButtonPress()){
                    bytes = mmInStream.read(buffer);
                    mHandler.obtainMessage(2, bytes, -1, buffer)
                            .sendToTarget();
                    Log.d("PULSADO", cont + "<<== -> " + bytes);
                    Escuchar();

                }else{
                    Log.d("Sin Pulsar", cont + "<<=="+ actual);
                }
                cont++;
            }
            catch(InterruptedException e){
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        t = new Thread(this);
        t.start();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

//    ================================================
//        * * * FUNCIONES FRAGMENTADAS * * *
//    ================================================
    @Override
    public void regPacienteComm(String nombre,String sexo,int edad,String direccion,String telefono,String ocupacion, String direccion_ocu, String telefono_ocu,String allegado) {
//        FragmentManager fragmentManager = getFragmentManager();
        VistaRegPaciente fragmentb=(VistaRegPaciente) getSupportFragmentManager().findFragmentById(R.id.f_main);
        fragmentb.setRegPaciente(nombre,sexo,edad,direccion,telefono,ocupacion,direccion_ocu,telefono_ocu,allegado);
    }

    @Override
    public void editDiente(int posicionDiente, String pared, String estado) {
        VistaRegDiagrama fragmentb=(VistaRegDiagrama) getSupportFragmentManager().findFragmentById(R.id.f_diagrama);
        fragmentb.editDiente(posicionDiente,pared,estado);
    }

    @Override
    public void guardarDiagrama(int id_paciente,int ultimo_plan) {
        VistaRegDiagrama fragmentb=(VistaRegDiagrama) getSupportFragmentManager().findFragmentById(R.id.f_diagrama);
        fragmentb.guardarDiagrama(id_paciente,ultimo_plan);
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;


        @Override
        protected void onPreExecute()
        {
//            progress = ProgressDialog.show(getApplicationContext(), "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    btNombre = myBluetooth.getName();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//conectamos al dispositivo y chequeamos si esta disponible
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Conexión BT Fallida");
                finish();
            }
            else
            {
                msg("Conectado");
                isBtConnected = true;
            }
//            progress.dismiss();
        }
    }
    public void onActivityResult(int requestcode, int resultcode, Intent datos)
    {

// Si el reconocimiento de voz es correcto almacenamos dentro de un array los datos obtenidos.
//Mostramos en pantalla el texto obtenido de la posición 0.
        if (resultcode== Activity.RESULT_OK && datos!=null)
        {
            ArrayList<String> text=datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this,text.get(0),Toast.LENGTH_LONG).show();
            interpretar(text.get(0).toString(),vistaA);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment vista = null;
        boolean trans = false;
        if (id == R.id.nav_camera) {
            vistaActual = "Diagrama";
            vista = new VistaRegDiagrama();
            trans= true;
        } else if (id == R.id.nav_gallery) {
            vista = new VistaRegPlanTratamiento();
            trans= true;
        } else if (id == R.id.nav_slideshow) {
            vistaActual = "pacientes";
            vista = new VistaPacientes();
            trans= true;
        } else if (id == R.id.nav_manage) {
            vistaActual = "plan";
            vista = new VistaRegPlanTratamiento();
            trans= true;
        } else if (id == R.id.nav_share) {
            vista = new VistaGetDiagrama();
            trans= true;
        } else if (id == R.id.nav_send) {
            vista = new VistaRegConsulta();
            trans= true;
        }

//        =========================================
//        |    MANEJADOR DE FRAGMENT PRINCIPAL    |
//        =========================================
        if(trans){
            Bundle bundle = new Bundle();
            //PARAMS RECIBIDOS POR LOGIN
            String id_doctor = getIntent().getStringExtra("id_doctor");
            String nombre = getIntent().getStringExtra("nombre");

            //PARAMS PARA ENVIAR A FRAGMENTS
            bundle.putString("id_doctor", id_doctor);
            bundle.putString("nombre", nombre);
            vista.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.f_main, vista);
            vistaA = vista;
            String titulo_Bar = item.getTitle().toString();
            vistaActual= titulo_Bar;
            hideBtnUnivesal(vistaActual);
            transaction.addToBackStack(null);
            transaction.commit();
            item.setCheckable(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    =====================================================================================
//                         * * *  INTERPRETE DE COMANDOS * * *
//    =====================================================================================
    public void interpretar(String comandos,Object vista){

// ----------------------------------------[ VISTA REG. DIAGRAMA ]----------------------------------------
        if(vistaActual=="Diagrama"){
            if(comandos.equals("guardar") || comandos.equals("Guardar") || comandos.equals("guarda")) {

                DienteService servicio = restadpter.create(DienteService.class);
                for(int i=0; i< ite.size(); i++) {
                    try {
                    servicio.regConsulta(
                        id_pacienteA,
                        ite.get(i).getId_p_tratamiento(),
                        ite.get(i).getEstado(),
                        "FALTA ESTO EN LA APP",
                        ite.get(i).getCantidad(),
                        new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Toast.makeText(getApplicationContext(), "..." +s+"...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getApplicationContext(),"ERROR :"+error+"...",Toast.LENGTH_LONG).show();
                            }
                        }
                    ); Thread.sleep(100);
                    }catch(InterruptedException e){}
                    Toast.makeText(getApplicationContext(), "Cantidad R => "+ ite.get(i).getCantidad(), Toast.LENGTH_LONG).show();
                }
                guardarDiagrama(id_pacienteA,ultimo_plan); // => id_paciente , id_plan
            }else{

                String[] split = comandos.split(" ");
                String pared;

                    int pos_diente = Integer.parseInt(split[0]);
                String estado_pared = split[2];

                //PALABRAS PARECIDAS A DERECHA  O RELACIONADAS
                if(split[1].equals("arriba")){pared = "U";}
                else if(split[1].equals("superior")){pared = "U";}
                else if(split[1].equals("Superior")){pared = "U";}

                //PALABRAS PARECIDAS A ABAJO O RELACIONADAS
                else if(split[1].equals("abajo")){pared = "D";}
                else if(split[1].equals("Abajo")){pared = "D";}
                else if(split[1].equals("inferior")){pared = "D";}
                else if(split[1].equals("Inferior")){pared = "D";}

                //PALABRAS PARECIDAS A IZQUIERDA O RELACIONADAS
                else if(split[1].equals("izquierda")){pared = "L";}
                else if(split[1].equals("izquierdas")){pared = "L";}

                //PALABRAS PARECIDAS A DERECHA O RELACIONADAS
                else if(split[1].equals("derecha")){pared = "R";}
                else if(split[1].equals("derechos")){pared = "R";}

                //PALABRAS PARECIDAS A CENTRO
                else  if(split[1].equals("centro")){pared = "C";}
                else  if(split[1].equals("Centro")){pared = "C";}
                else  if(split[1].equals("centros")){pared = "C";}
                else  if(split[1].equals("Centros")){pared = "C";}
                else {pared = "C";}
                editDiente(pos_diente, pared, estado_pared);
            }
        }
        if(vistaActual=="Nuevo Plan"){
            DienteService servicio = restadpter.create(DienteService.class);
            for(int i=0; i< ite.size(); i++) {
                try {
                    servicio.regConsulta(
                            id_pacienteA,
                            ite.get(i).getId_p_tratamiento(),
                            ite.get(i).getEstado(),
                            "FALTA ESTO EN LA APP",
                            ite.get(i).getCantidad(),
                            new Callback<String>() {
                                @Override
                                public void success(String s, Response response) {
                                    Toast.makeText(getApplicationContext(), "..." +s+"...", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getApplicationContext(),"ERROR :"+error+"...",Toast.LENGTH_LONG).show();
                                }
                            }
                    ); Thread.sleep(100);
                }catch(InterruptedException e){}
                Toast.makeText(getApplicationContext(), "Cantidad R => "+ ite.get(i).getCantidad(), Toast.LENGTH_LONG).show();
            }
        }
// ----------------------------------------[ VISTA REG. PACIENTE ]----------------------------------------
//        1.REGISTRAR PACIENTE (PRIMERA PRUEBA)
        if((comandos.equals("registrar paciente")) || (comandos.equals( "agregar nuevo paciente"))
                || (comandos.equals("nuevo paciente")) || (comandos.equals("registar un nuevo paciente"))
                || (comandos.equals("nuevo registro de paciente")))
        {
            Fragment vista2 = new VistaRegPaciente();
            Toast.makeText(this,"..."+vistaActual+"...",Toast.LENGTH_LONG).show();
            cambioVista(vista2, "Nuevo Paciente");

        }

        if(vistaActual=="Nuevo Paciente"){
            vistaA = vista;
            String[] split = comandos.split(" ");
            final StringBuilder nombre = new StringBuilder();
            StringBuilder direccion = new StringBuilder();
            StringBuilder telefono = new StringBuilder();
            StringBuilder sexo = new StringBuilder();
            StringBuilder edad = new StringBuilder();
            StringBuilder estado_civil = new StringBuilder();

            for (int i = 0; i < split.length; i++) {

                if (split[0].equals("nombre"))      {if(i>0){NOMBRES = nombre.append(" "+split[i]).toString();} }
                if (split[0].equals("dirección"))   {if(i>0){DIRECCION = direccion.append(" "+split[i]).toString();}}
                if (split[0].equals("teléfono"))    {if(i>0){TELEFONO = telefono.append(" "+split[i]).toString();}}
                if (split[0].equals("estado civil")){if(i>0){ESTADO_CIVIL = estado_civil.append(" "+split[i]).toString();}}
                if (split[0].equals("sexo"))        {if(i>0){SEXO = sexo.append(" "+split[i]).toString();}}
                regPacienteComm(NOMBRES, SEXO, 22, DIRECCION, TELEFONO, OCUPACION,DIRECCION_OCU,TELEFONO_OCU,ALLEGADO);
            }
            if(comandos.equals("guardar registro") || comandos.equals("guardar") || comandos.equals("confirmar registro")){
                PacienteService servicio = restadpter.create(PacienteService.class);
                servicio.regPaciente( NOMBRES, DIRECCION,TELEFONO, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Toast.makeText(getApplicationContext(), "..." + s + "...", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(),"ERROR :"+error+"...",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }





    }


    public void cambioVista(final Fragment vistaObj, final String vActual){
        new Handler().post(new Runnable() {
            public void run() {
                vistaActual = vActual;
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.f_main, vistaObj);
                transaction.addToBackStack(null);
                hideBtnUnivesal(vActual);
                transaction.commit();
            }
        });
    }

    public void cambioVistaU(final Fragment vistaObj, final String vActual,Bundle parametros){
                vistaActual = vActual;
                vistaObj.setArguments(parametros);
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.f_main, vistaObj);
                transaction.addToBackStack(null);
                hideBtnUnivesal(vActual);
                transaction.commit();
     }



}

