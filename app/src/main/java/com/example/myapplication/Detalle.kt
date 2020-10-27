package com.example.myapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_detalle.destino
import kotlinx.android.synthetic.main.activity_detalle.ecoautobus
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Detalle : AppCompatActivity() {
    var imagenes = ArrayList<String>()
    var imagenessin = ArrayList<String>()
    var estado = ArrayList<String>()
    var idestado = ArrayList<String>()
    var ciudadano = ""
    var telefono = ""
    var imagen = String()
    var titulod = String()
    var id = String()

    var URL=String()
    var URL2 =String()
    var codigo = String()



    var nombre = String()
    var descripcion = String()
    var idClase = String()
    var clase = String()
    var modelo = String()
    var costo = String()
    var cantidad = String()
    var cantidadAviso = String()
    var idUnidadMedida = String()
    var unidadMedida = String()
    var nota = String()
    var idProveedor = String()
    var proveedor = String()

    var idUbicacion = String()
    var ubicacion = String()
    var imagenUbicacion = String()
    var idArea = String()
    var area = String()
    var idSubarea = String()
    var subarea = String()
    var imagenArea = String()
    var idCentroCostos = String()
    var centroCostos = String()
    var noFactura = String()
    var diasMantenimiento = String()
    var diasAviso = String()
    var fechaAlta = String()
    var fechaUltimoMantenimiento = String()
    var fechaMantenimiento = String()
    var fechaAviso = String()
    var idResponsableUbicacion = String()
    var correoResponsableUbicacion = String()
    var tokenResponsableUbicacion = String()
    var idResponsableArea = String()
    var correoResponsableArea = String()
    var tokenResponsableArea = String()
    var idEstadoArticulo = String()

    var estadot = String()
    var color = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val i = intent.getStringExtra("folio")
        codigo=i

abordo()
        cerrar.setOnClickListener(View.OnClickListener {
finish()
        })

    }
    fun withEditText(view: View) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setMessage("¿Deseas modificar el estatus del articulo?")
            .setPositiveButton("Aceptar") { dialog, id ->

            }
            .setNegativeButton("Caancelar") { dialog, id ->
                // User cancelled the dialog
            }
        // Create the AlertDialog object and return it
        // Create the AlertDialog object and return it
        builder.create()
        builder.show()
    }


    fun abordo(){
        val progressDialog = ProgressDialog(this,
            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val datos = JSONObject()
        try {
            datos.put("folio",codigo )

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requstQueue = Volley.newRequestQueue(this@Detalle)

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, "https://appis.atah.online/api/Tickets", datos,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {

                    try {
                        progressDialog?.dismiss()
                        val result = response.get("result") as Int

                        if(result>0) {

                            var mensaje = response.getInt("result")

                            infob.setText(response.getString("mensaje"))

                            if (mensaje> 0) {


                                // start your next activity
                                var json_data = response.getJSONObject("boleto")

                                var org = json_data.getString("origen")
                                var dest = json_data.getString("destinoboleto")
                                var hor = json_data.getString("hora")
                                var tarif = json_data.getString("tarifa")
                                var asient = json_data.getString("asiento")
                                var eco = json_data.getString("eco")
                                var cambiodeautobus = json_data.getBoolean("cambiodeautobus")
                                var escaneado = json_data.getString("escaneado")

                                if(escaneado.equals("True")){
                                    adinciona.setImageResource(R.drawable.checkboletoazul )

                                }
                                else{
                                }
                                var totales = response.getJSONObject("totales")
                                var _escanead = totales.getString("escaneados")
                                var _faltante = totales.getString("faltantes")
                                var _tota = totales.getString("total")
                                escaneados.text = _escanead
                                faltantes.text = _faltante
                                ecoautobus.text = _tota


                                origen.text = "Origen: " + org
                                destino.text = "Destino: " + dest
                                salida.text = "Salida: " + hor
                                tarifa.text = "Tarifa: " + tarif
                                asiento.text = "Asiento: " + asient
                                autobus.text = "Autobus: " + eco
                                estatus.text = "Boleto escaneado"

                            }
                        }
                    } catch (e: JSONException) {
                        progressDialog?.dismiss()
                        e.printStackTrace()
                    }

                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Toast.makeText(this@Detalle,"sin conexion:"+error, Toast.LENGTH_LONG).show()
                    progressDialog?.dismiss()
                }
            }
        ) {

            //here I want to post data to sever
        }

        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        requstQueue.add(jsonObjectRequest)


    }



}
