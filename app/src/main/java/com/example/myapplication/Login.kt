package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class Login : AppCompatActivity() {
    var linea = ArrayList<String>()
    var REQUEST_LOCATION = 1
    var lineapk = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pedirPermisos()
        val preferences = getSharedPreferences("variables", Context.MODE_PRIVATE)
        val idt = preferences.getString("sesion", "")
        if (idt.equals("si")) {


                buttoningresar.isEnabled = false
                _cellText.setText(preferences.getString("usuario", ""))
            _passwordText.setText(preferences.getString("pass", ""))
                consultar()

        }


    }
    fun entrar(v: View?) {

        var nom=_cellText.text.toString()
        var pass=_passwordText.text.toString()

        if(nom==""){
            Toast.makeText(this, "Ingrese usuario", Toast.LENGTH_SHORT).show()

        }

      else if(pass==""){
            Toast.makeText(this, "Ingrese password", Toast.LENGTH_SHORT).show()

        }
        else {
        consultar()
        }

    }


    fun consultar() {

        val progressDialog = ProgressDialog(this,


            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val nomb = _cellText.text
        val pass = _passwordText.text

        val datos = JSONObject()
        try {
            datos.put("USUARIO", nomb)

            datos.put("PASSWORD", pass)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Method.POST, "https://appis.atah.online/api/Usuarios/Login", datos,
            Response.Listener { response ->
                try {
                    progressDialog?.dismiss()
                    val result = response["result"] as Int
                    if (result == 1) {
                        val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

                        val editor = preferencias.edit()

                        val guias = response.getJSONObject("usuario")
                     val pk = guias.getString("pk")
                     val usuario = guias.getString("usuario")
                     val contrase =  _passwordText.text.toString()
                     val nombre = guias.getString("nombre")
                     val apellidos = guias.getString("apellidos")
                     val rol = guias.getString("rol")
                     val sucursal = guias.getString("pK_DESTINO")
                     val sucursaltext    = guias.getString("destino")

                        editor.putString("pk", pk)
                        editor.putString("usuario", usuario)
                        editor.putString("nombre", nombre)
                        editor.putString("apellidos", apellidos)
                        editor.putString("rol", rol)
                        editor.putString("pass", contrase)
                        editor.putString("sucursal", sucursal)
                        editor.putString("sucursaltext", sucursaltext)
                        if (checkBox.isChecked) {
                            editor.putString("sesion", "si")
                        }

                        val costos = response.getJSONArray("lineas")
                        var a=0
                        for (i in 0 until costos.length()) {
                            val producto = costos.getJSONObject(i)

                            editor.putString("pklinea"+a.toString(), producto.getString("pK1"))
                            editor.putString("linea"+a.toString(), producto.getString("linea"))
                            a++

                        }

                        editor.putInt("cantidadlineas", a)
                        editor.commit()
                        val sendMailIntent = Intent(this, MainActivity::class.java)
                        startActivity(sendMailIntent)
                        finish()
                    } else {
                        val error = response.getString("mensaje")
                        _ShowAlert("Error", error)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                progressDialog?.dismiss()
                Log.e("Rest Response", error.toString())
            }
        ) { //here I want to post data to sever
        }
        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            MY_SOCKET_TIMEOUT_MS,
            maxRetries,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requstQueue.add(jsonObjectRequest)
    }

    private fun _ShowAlert(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    fun pedirPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_LOCATION)
            }
        }
    }
}
