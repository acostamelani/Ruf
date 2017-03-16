package com.darkcode.ruf_012.Diagrama;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.darkcode.ruf_012.Diagrama.Diente;
import com.darkcode.ruf_012.Diagrama.DienteService;
import com.darkcode.ruf_012.MainActivity;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by NativoLink on 2/2/17.
 */

public class VistaRegDiagrama extends Fragment {

    boolean estado_o = false;
    //--------- PRIMER CUADRANTE ------
    Diente diente18,diente17,diente16,diente15,diente14,diente13,diente12,diente11;
    Diente diente55,diente54,diente53,diente52,diente51;

    //--------- PRIMER CUADRANTE ------
    Diente diente48,diente47,diente46,diente45,diente44,diente43,diente42,diente41;
    Diente diente85,diente84,diente83,diente82,diente81;



    ArrayList<Diente> lista = new ArrayList<Diente>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//     View rootView= inflater.inflate(R.layout.reg_paciente, container,false);


        return new MyView(getContext());

    }
    public class MyView extends View {
        public MyView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            //=======================================
            //      * * * PRIMER CUADRANTE * * *
            //=======================================
            float div = (float) (1.2);
            float ejeX = 70/div;
            float X_D=210;
            diente18 = new Diente(getContext(),18,50,ejeX,false);
            diente17 = new Diente(getContext(),17,250/div,ejeX,false);
            diente16 = new Diente(getContext(),16,450/div,ejeX,false);
            diente15 = new Diente(getContext(),15,650/div,ejeX,false);
            diente14 = new Diente(getContext(),14,850/div,ejeX,false);
            diente13 = new Diente(getContext(),13,1050/div,ejeX,false);
            diente12 = new Diente(getContext(),12,1250/div,ejeX,false);
            diente11 = new Diente(getContext(),11,1450/div,ejeX,false);

            diente55 = new Diente(getContext(),55,300/div,ejeX+X_D,true);
//            diente54 = new Diente(getContext(),54,500/div,ejeX+X_D,true);
//            diente53 = new Diente(getContext(),53,700/div,ejeX+X_D,true);
//            diente52 = new Diente(getContext(),52,900/div,ejeX+X_D,true);
//            diente51 = new Diente(getContext(),51,1100/div,ejeX+X_D,true);

            lista.add(diente18);
            lista.add(diente17);
            lista.add(diente16);
            lista.add(diente15);
            lista.add(diente14);
            lista.add(diente13);
            lista.add(diente12);
            lista.add(diente11);


            lista.add(diente55);
//            lista.add(diente54);
//            lista.add(diente53);
//            lista.add(diente52);
//            lista.add(diente51);


            //=======================================
            //      * * * SEGUNDO CUADRANTE * * *
            //=======================================
            float ejeX2 = 610/div;
            diente48 = new Diente(getContext(),48,50/div,ejeX2,false);
            diente47 = new Diente(getContext(),47,250/div,ejeX2,false);
            diente46 = new Diente(getContext(),46,450/div,ejeX2,false);
            diente45 = new Diente(getContext(),45,650/div,ejeX2,false);
            diente44 = new Diente(getContext(),44,850/div,ejeX2,false);
            diente43 = new Diente(getContext(),43,1050/div,ejeX2,false);
            diente42 = new Diente(getContext(),42,1250/div,ejeX2,false);
            diente41 = new Diente(getContext(),41,1450/div,ejeX2,false);

            diente85 = new Diente(getContext(),85,300/div,ejeX2+X_D,true);
            diente84 = new Diente(getContext(),84,500/div,ejeX2+X_D,true);
            diente83 = new Diente(getContext(),83,700/div,ejeX2+X_D,true);
            diente82 = new Diente(getContext(),82,900/div,ejeX2+X_D,true);
            diente81 = new Diente(getContext(),81,1100/div,ejeX2+X_D,true);

            lista.add(diente48);
            lista.add(diente47);
            lista.add(diente46);
            lista.add(diente45);
            lista.add(diente44);
            lista.add(diente43);
            lista.add(diente42);
            lista.add(diente41);


            lista.add(diente85);
            lista.add(diente84);
            lista.add(diente83);
            lista.add(diente82);
            lista.add(diente81);



            new Thread(new Runnable() {
                public void run() {
                    while(true){
                        try {
                            Thread.sleep(500);
                            postInvalidate();
                            if(estado_o==true){
//                              diente.cambiarColor("#f50057","U");
                                estado_o = false;
                                Log.v("CANVAS","TRUE",null);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();


        }

        @Override
        protected void onDraw(final Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);

            //=======================================
            //      * * * PRIMER CUADRANTE * * *
            //=======================================
            diente18.onDraws(canvas);
            diente17.onDraws(canvas);
            diente16.onDraws(canvas);
            diente15.onDraws(canvas);
            diente14.onDraws(canvas);
            diente13.onDraws(canvas);
            diente12.onDraws(canvas);
            diente11.onDraws(canvas);


            diente55.onDraws(canvas);
//            diente54.onDraws(canvas);
//            diente53.onDraws(canvas);
//            diente52.onDraws(canvas);
//            diente51.onDraws(canvas);
            //=======================================
            //      * * * SEGUNDO CUADRANTE * * *
            //=======================================
            diente48.onDraws(canvas);
            diente47.onDraws(canvas);
            diente46.onDraws(canvas);
            diente45.onDraws(canvas);
            diente44.onDraws(canvas);
            diente43.onDraws(canvas);
            diente42.onDraws(canvas);
            diente41.onDraws(canvas);


            diente85.onDraws(canvas);
            diente84.onDraws(canvas);
            diente83.onDraws(canvas);
            diente82.onDraws(canvas);
            diente81.onDraws(canvas);


        }
    }
    public  void editDiente(int posicionDiente,String pared,String estado){

        for(Diente p : lista) {

            if(p.getPosDiente() == posicionDiente){
                if(pared.equals("U")){
                    p.cambiarColor(estado,"U");
                }
                if(pared.equals("D")){
                    p.cambiarColor(estado,"D");
                }
                if(pared.equals("L")){
                    p.cambiarColor(estado,"L");
                }
                if(pared.equals("R")){
                    p.cambiarColor(estado,"R");
                }
                if(pared.equals("C")){
                    p.cambiarColor(estado,"C");
                }

            }else{
                Log.v("DienteActual",p.getPosDiente()+">>FALSE>"+estado,null);
            }
        }
    }
    public void guardarDiagrama(int id_paciente,int ultimo_plan){


        for(Diente d : lista) {
            ArrayList<String> pared = new ArrayList<String>();
            int posicion = d.getPosDiente();


            pared.add("U");
            pared.add("D");
            pared.add("C");
            pared.add("R");
            pared.add("L");

            for(String pa : pared) {
                String estado ="vacio";
                if(pa=="U"){   estado = d.getEstado_U();}
                if(pa=="D"){   estado = d.getEstado_D();}
                if(pa=="C"){   estado = d.getEstado_C();}
                if(pa=="R"){   estado = d.getEstado_R();}
                if(pa=="L"){   estado = d.getEstado_L();}

                RestAdapter restadpter = new RestAdapter.Builder().setEndpoint("http://linksdominicana.com").build();
                DienteService servicio = restadpter.create(DienteService.class);
                servicio.guardarDiente(id_paciente,posicion,pa,estado,new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.d("RETORNO",">>"+s);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), "ERROR :" + error + "...", Toast.LENGTH_LONG).show();
                        Log.d("RETORNO",">"+error.getMessage());
                    }
                });
            }


        }
    }

}