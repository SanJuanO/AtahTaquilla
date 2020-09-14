package com.example.myapplication

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.util.Util
import com.example.myapplication.utilidades.Utilidades

import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_infracciones.*


import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


public class MainActivity : AppCompatActivity() {
   var TOKEN: String=""
    var id: String=""
    var correo= String()
    var toque:Boolean= false
    var permisos = ArrayList<String>()
    // Para la operaciones con dispositivos bluetooth
    var bluetoothAdapter: BluetoothAdapter? = null
    var dispositivoBluetooth: BluetoothDevice? = null
     var bluetoothSocket: BluetoothSocket? = null

    private val REQUEST_DISPOSITIVO = 425
    private val LIMITE_CARACTERES_POR_LINEA = 32
    val TAG_DEBUG = "tag_debug"
    private val IR_A_DIBUJAR = 632
    private val COD_PERMISOS = 872
    private val INTENT_CAMARA = 123
    private val INTENT_GALERIA = 321
    private val ANCHO_IMG_58_MM = 384
    private val MODE_PRINT_IMG = 0
    // identificador unico default
    private val aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    // Para el flujo de datos de entrada y salida del socket bluetooth
     var outputStream: OutputStream? = null
     var inputStream: InputStream? = null



    @Volatile
    private var pararLectura = false
    var URL:String="https://appis-apizaco.gesdesapplication.com/api/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("correo", "")
        val t = preferences.getString("iduser", "")!!
        id=t
        correo = temp.toString()
        URL +="EditTokenUsuarios"
        listablu.setOnClickListener(View.OnClickListener {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            lista()

        })

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationViewinicio)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val vent = Escanear.newInstance()
        openFragment(vent)

    }
    override fun onBackPressed() { // Añade más funciones si fuese necesario
        finish()
    }

    private fun cerrarConexion() {
        try {
            if (bluetoothSocket != null) {
                if (outputStream != null) outputStream!!.close()
                pararLectura = true
                if (inputStream != null) inputStream!!.close()
                bluetoothSocket!!.close()
                listablu.setText("Conexion terminada")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun lista(){

        cerrarConexion()

        val intentLista = Intent(this, ListaBluetoohtActivity::class.java)
        startActivityForResult(
            intentLista, REQUEST_DISPOSITIVO)




    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.infracciones -> {

                val inci = Infracciones.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.perfil -> {
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Perfil.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.escanear -> {
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Escanear.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.boletos -> {
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = BoletosVendidos.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.guias -> {
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Guias.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun permisosobtener() {


        val datos = JSONObject()
        try {
            datos.put("Correo", correo)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requstQueue = Volley.newRequestQueue(this)
        val progressDialog = ProgressDialog(this,
            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, "https://inventarios.gesdesapplication.com/api/UsuariosApi/getUsuarioByCorreo", datos,
            Response.Listener<JSONObject> { response ->
                try {
                    progressDialog?.dismiss()
                    val result = response.get("resultado") as Int
                    progressDialog?.dismiss()
                    if (result == 1) {
                        try {


                            val guias = response.getJSONObject("data")
                            id = guias.getString("id")


                            val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

                            val editor = preferencias.edit()
                            val costos = guias.getJSONArray("listaPermisos")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)
                            }


                        } catch (es: Exception) {
                            Log.d("sergio1", "" + es.toString())
                            finish()
                            progressDialog?.dismiss()
                        }

                    } else {
                        Toast.makeText(this, "sin conexion", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressDialog?.dismiss()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_DISPOSITIVO -> {
                    listablu.text = "Cargando..."
                    val direccionDispositivo =
                        data!!.extras!!.getString("DireccionDispositivo")
                    val nombreDispositivo =
                        data!!.extras!!.getString("NombreDispositivo")

                    // Obtenemos el dispositivo con la direccion seleccionada en la lista
                    dispositivoBluetooth = bluetoothAdapter!!.getRemoteDevice(direccionDispositivo)
                    Thread(Runnable {
                        try {
                            // Conectamos los dispositivos

                            // Creamos un socket

                            bluetoothSocket = dispositivoBluetooth!!.createInsecureRfcommSocketToServiceRecord(aplicacionUUID)
                            bluetoothSocket!!.connect() // conectamos el socket
                            outputStream = bluetoothSocket!!.getOutputStream()
                            inputStream = bluetoothSocket!!.getInputStream()
                            Utilidades.bluetoothAdapter=bluetoothAdapter
                            Utilidades.inputStream=inputStream
                            Utilidades.outputStream=outputStream
                            Utilidades.dispositivoBluetooth=dispositivoBluetooth
                            Utilidades.bluetoothSocket=bluetoothSocket

                            //empezarEscucharDatos();
                            this.runOnUiThread(java.lang.Runnable {

                                listablu!!.text = "$nombreDispositivo conectada"
                                Toast.makeText(
                                    this,
                                    "Dispositivo Conectado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        } catch (e: IOException) {
                            this.runOnUiThread(java.lang.Runnable {
                                listablu!!.text = ""
                                Toast.makeText(
                                    this,
                                    "No se pudo conectar el dispositivo"+e.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                            Log.e(
                                TAG_DEBUG,
                                "Error al conectar el dispositivo bluetooth"
                            )
                            e.printStackTrace()
                        }
                    }).start()
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cerrarConexion()
    }

}
