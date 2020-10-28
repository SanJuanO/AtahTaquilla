package com.example.myapplication

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.permission.PermissionsChecker
import com.example.myapplication.utilidades.PrintBitmap
import com.example.myapplication.utilidades.Utilidades
import com.example.myapplication.utilidades.Utilidades.bluetoothSocket
import com.example.myapplication.utilidades.Utilidades.outputStream
import com.example.myapplication.utils.EscPrintCls
import com.example.myapplication.utils.Print
import com.example.myapplication.utils.PrintActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.Writer
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.android.synthetic.main.activity_infracciones.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*


class Infracciones : Fragment() {
    var idlinea =  ArrayList<String>()
    var lineaaa =  ArrayList<String>()
    var faltaa =  ArrayList<String>()
    var precio =  ArrayList<String>()
    var pkdestino =  ArrayList<String>()
    var destinoa =  ArrayList<String>()
    var imagenlogo ="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"
    var pkhorario =  ArrayList<String>()
    var horarios =  ArrayList<String>()
    var pkdescuento =  ArrayList<String>()
    var descuento =  ArrayList<String>()
    var descuentop =  ArrayList<String>()
    var pasaje =  ArrayList<String>()
    private val hiloComunicacion: Thread? = null
    var printThread: PrintActivity.Print_Thread? = null
    var BatteryV = 0

    var nombreoficial= String()
    var numeroparquimetro= String()
    var tarifa= 0.0
    var placa= String()
    var descuent= 0.0
    var faltao= String()
    var garantiao= String()
    var folio= String()
    var fecha= String()
    var hora= String()
    var pklinea= String()
    var posicionlinea= String()
    var posicionhorario= String()
    var posiciondestino= String()
    var PKCORRIDA= String()

     var Imgbase64: String? = ""
    var mContext: Context? = null

    var checker: PermissionsChecker? = null
 var LLEGADA_C = String()
 var SALIDA_C = String()
 var LINEA = String()
 var AUTOBUS = String()
 var FECHA = String()
 var ASIENTO = String()
 var TARIFA = String()
 var ORIGEN = String()
 var DESTINO_COMPLETO = String()
 var DESTINO = String()
 var SALIDA = String()
 var PRECIO = String()
 var PASAJERO = String()
 var STATUS = String()
 var FILA = String()
 var COLUMNA = String()
 var VENDEDOR = String()
 var PISO = String()
 var CORTE = String()
 var RUTA = String()
 var CHOFER = String()
 var TIPO = String()
 var FORMADEPAGO = String()
 var PK_CORRIDA_DIA = String()



    @Volatile
    private var pararLectura = false


    companion object {
        fun newInstance(): Infracciones = Infracciones()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.activity_infracciones, container, false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(state: Bundle?)
    {
        super.onActivityCreated(state)

        val fech = SimpleDateFormat("yyyy-MM-dd")
        fecha = fech.format(Date())



        val preferencias = this.requireActivity().getSharedPreferences(
            "variables",
            Context.MODE_PRIVATE
        )
val a= preferencias.getInt("cantidadlineas", 0)
        VENDEDOR= preferencias.getString("nombre", "")!! +" "+ preferencias.getString(
            "apellidos",
            ""
        )!!
        val fech2 = SimpleDateFormat("dd/MM/yyyy")

        ORIGEN =preferencias.getString("sucursaltext", "")!!

            FECHA=fech2.format(Date())
        idlinea.add("0")
        lineaaa.add("Seleccione una linea")
        for (i in 0 until a) {

         idlinea.add(preferencias.getString("pklinea" + i.toString(), "")!!)
         lineaaa.add(preferencias.getString("linea" + i.toString(), "")!!)
            econombre.setText("")
        }

        val adapter0: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            lineaaa
        )
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_item)
        linea.setAdapter(adapter0)


        mContext = getActivity()?.getApplicationContext()



        checker = PermissionsChecker(activity)

        btnguia.setOnClickListener(View.OnClickListener {
            if (bluetoothSocket != null) {

                generarguia()
            } else {
                Toast.makeText(requireActivity(), "Conecte una impresora", Toast.LENGTH_SHORT)
                    .show()

                Log.e(
                    "",
                    "Socket nulo"
                )

            }
        })
//        total.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//            }
//
//            override fun afterTextChanged(editable: Editable) {
//                PRECIO = total.text.toString()
//            }
//        })

        button4.setOnClickListener(View.OnClickListener {
            if (bluetoothSocket != null) {

                vender()
            } else {
                Toast.makeText(requireActivity(), "Conecte una impresora", Toast.LENGTH_SHORT)
                    .show()

                Log.e(
                    "",
                    "Socket nulo"
                )

            }
        })
        linea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                posicionlinea = linea.selectedItemPosition.toString()
                if(posicionlinea!="0"){
                    pkdestino.clear()
                    destinoa.clear()
                    precio.clear()

                    horarios.clear()
                    descuento.clear()
                    pasaje.clear()
                    econombre.setText("")

                    val adapter0: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        horarios
                    )
                    adapter0.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    destino.setAdapter(adapter0)

                    horario.setAdapter(adapter0)
                    tarifat.setAdapter(adapter0)



                    consultar()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        tarifat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val des = tarifat.selectedItemPosition
                val t= descuento.get(des)
              descuent=t.toDouble()
TARIFA=pasaje.get(des)

                val porcentaje=descuent/100
                val tota= tarifa-tarifa*(porcentaje)
                PRECIO=tota.toString()
                numberPicker.value = tota.toInt()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        horario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var i = horario.selectedItemPosition
                posicionhorario = horario.selectedItemPosition.toString()
SALIDA=horarios.get(i)

                corrida()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        destino.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                posiciondestino = destino.selectedItemPosition.toString()
                if(posiciondestino!="0"){

                    val des = destino.selectedItemPosition
                    econombre.setText("")

                    DESTINO = destinoa.get(des)

                    val prec=precio.get(des)
                    tarifa=prec.toDouble()
                    val porcentaje=descuent/100
                    val tota= tarifa-tarifa*(porcentaje)
                    PRECIO=tota.toString()
//                    total.setText(tota.toString())
                    consultarhorarios()
                    corrida()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)




        //spinner



            numberPicker.minValue = 0
            numberPicker.maxValue =400
            numberPicker.value = 5

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
PRECIO=numberPicker.value.toString()

        }

        }


    fun validar() {





        if(numeroparquimetro.equals("")){
            Toast.makeText(mContext, "Escriba el numero del parquimetro", Toast.LENGTH_SHORT).show()

            return;
        }
        if(placa.equals("")){
            Toast.makeText(mContext, "Escriba la placa", Toast.LENGTH_SHORT).show()

            return;
        }

        if(Imgbase64.equals("")){
            Toast.makeText(mContext, "Tome la foto de la evidencia", Toast.LENGTH_SHORT).show()

            return;
        }


    }

    fun tomarFoto() {
        val imageTakeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (imageTakeIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(
                imageTakeIntent,
                1
            )
        }
    }



    private fun encodeImage(bm: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    fun getByteString(
        str: String,
        bold: Int,
        font: Int,
        widthsize: Int,
        heigthsize: Int
    ): ByteArray? {
        if ((str.length == 0) or (widthsize < 0) or (widthsize > 3) or (heigthsize < 0) or (heigthsize > 3
                    ) or (font < 0) or (font > 1)
        ) return null
        var strData: ByteArray? = null
        strData = try {
            str.toByteArray(charset("iso-8859-1"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }
        val command = ByteArray(strData.size + 9)
        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30) //
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03) //
        command[0] = 27 // caracter ESC para darle comandos a la impresora
        command[1] = 69
        command[2] = bold.toByte()
        command[3] = 27
        command[4] = 77
        command[5] = font.toByte()
        command[6] = 29
        command[7] = 33
        command[8] = (intToWidth[widthsize] + intToHeight[heigthsize]).toByte()
        System.arraycopy(strData, 0, command, 9, strData.size)
        return command
    }

    fun createPdf() {

        if (Utilidades.bluetoothSocket != null) {
            try {
                val texto: String = "texto"+ "\n"+"texto"+ "\n"+"texto"+ "\n"
                val fuente: Int = 1
                val negrita = 1
                val ancho: Int = 2
                val alto: Int = 2

                // Para que acepte caracteres espciales
                Utilidades.outputStream!!.write(0x1C)
                Utilidades. outputStream!!.write(0x2E) // Cancelamos el modo de caracteres chino (FS .)
                Utilidades.outputStream!!.write(0x1B)
                Utilidades.outputStream!!.write(0x74)
                Utilidades. outputStream!!.write(0x10) // Seleccionamos los caracteres escape (ESC t n) - n = 16(0x10) para WPC1252
                Utilidades. outputStream!!.write(
                    getByteString(
                        texto,
                        negrita,
                        fuente,
                        ancho,
                        alto
                    )
                )
                Utilidades.outputStream!!.write("\n\n".toByteArray())
            } catch (e: IOException) {
                Log.e(
                    "",
                    "Error al escribir en el socket"
                )
                Toast.makeText(
                    requireActivity(),
                    "Error al interntar imprimir texto",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        } else {
            Log.e(
                "",
                "Socket nulo"
            )

        }
    }
    fun consultar() {

        val progressDialog = ProgressDialog(
            requireActivity(),
            R.style.Theme_AppCompat_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val preferencias = this.requireActivity().getSharedPreferences(
            "variables",
            Context.MODE_PRIVATE
        )
        val pk_origen= preferencias.getString("sucursal", "0")!!
val pko=pk_origen.toInt()
        val pkl=idlinea.get(posicionlinea.toInt())
        val datos = JSONObject()
        try {
            datos.put("FECHA", fecha)
              datos.put("PK_LINEA", pkl)
            datos.put("PK_ORIGEN", pko)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Method.POST, "https://appis.atah.online/api/Corridas/getDestinos", datos,
            Response.Listener { response ->
                try {
                    progressDialog?.dismiss()
                    val result = response["resultado"] as Int
                    if (result == 1) {
                        precio.clear()

                        pkdestino.clear()
                        destinoa.clear()
                        pkdestino.add("0")
                        precio.add("0")
                        destinoa.add("Seleccione un destino")

                        val costos = response.getJSONArray("destinos")
                        var a = 0
                        for (i in 0 until costos.length()) {
                            val producto = costos.getJSONObject(i)

                            pkdestino.add(producto.getString("pk"))
                            destinoa.add(producto.getString("destino"))
                            precio.add(producto.getString("precio"))


                        }

                        val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(
                            requireActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            destinoa
                        )
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        destino.setAdapter(adapter2)

                    } else {
                        val error = response.getString("mensaje")
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

    fun consultarhorarios() {

        val progressDialog = ProgressDialog(
            requireActivity(),
            R.style.Theme_AppCompat_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val preferencias = this.requireActivity().getSharedPreferences(
            "variables",
            Context.MODE_PRIVATE
        )
        val pk_origen= preferencias.getString("sucursal", "0")!!
val pko=pk_origen.toInt()
        val pkl=idlinea.get(posicionlinea.toInt())
        val pkd=pkdestino.get(posiciondestino.toInt())
        val datos = JSONObject()
        try {
            datos.put("FECHA", fecha)
              datos.put("PK_LINEA", pkl)
            datos.put("PK_ORIGEN", pko)
            datos.put("PK_DESTINO", pkd)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Method.POST,
            "https://appis.atah.online/api/Corridas/CorridasByFechaPkOrigenLineaPkDestino",
            datos,
            Response.Listener { response ->
                try {
                    progressDialog?.dismiss()
                    val result = response["result"] as Int
                    if (result == 1) {

                        horarios.clear()
                        descuento.clear()
                        pasaje.clear()


                        val adapter0: ArrayAdapter<String> = ArrayAdapter<String>(
                            requireActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            horarios
                        )
                        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_item)

                        horario.setAdapter(adapter0)
                        tarifat.setAdapter(adapter0)


                        val costos = response.getJSONArray("horarios")
                        var a = 0
                        for (i in 0 until costos.length()) {
                            val producto = costos.getJSONObject(i)

                            horarios.add(producto.getString("hora"))


                        }
                        val costos2 = response.getJSONArray("tarifas")

                        for (i in 0 until costos2.length()) {
                            val producto2 = costos2.getJSONObject(i)

                            pkdescuento.add(producto2.getString("pki"))
                            pasaje.add(producto2.getString("pasaje"))
                            descuento.add(producto2.getString("porcentaje"))
                            descuentop.add(producto2.getString("permitidos"))


                        }
                        val adapter3: ArrayAdapter<String> = ArrayAdapter<String>(
                            requireActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            horarios
                        )
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        horario.setAdapter(adapter3)
                        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            requireActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            pasaje
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        tarifat.setAdapter(adapter)


                    } else {
                        val error = response.getString("mensaje")
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
    fun vender() {

        val progressDialog = ProgressDialog(
            requireActivity(),
            R.style.Theme_AppCompat_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val preferencias = this.requireActivity().getSharedPreferences(
            "variables",
            Context.MODE_PRIVATE
        )
        val pk_origen= preferencias.getString("sucursal", "0")!!
        val PK= preferencias.getString("pk", "0")!!
        val pko=pk_origen.toInt()
        val pkl=idlinea.get(posicionlinea.toInt())
        val pkd=pkdestino.get(posiciondestino.toInt())
        val datos = JSONObject()
        try {

TIPO="PIE"
            STATUS="VENDIDO"
            FILA="100"
            COLUMNA="100"
            PISO="1"
            CORTE="0"
            FORMADEPAGO="Efectivo"
var t = TARIFA
            datos.put("LLEGADA_C", LLEGADA_C)
            datos.put("SALIDA_C", SALIDA_C)
            datos.put("LINEA", LINEA)
            datos.put("AUTOBUS", AUTOBUS)
            datos.put("FECHA", FECHA)
            datos.put("ASIENTO", ASIENTO)
            datos.put("TARIFA", TARIFA)
            datos.put("ORIGEN", ORIGEN)
            datos.put("DESTINO_COMPLETO", DESTINO_COMPLETO)
            datos.put("DESTINO", DESTINO)
            datos.put("SALIDA", SALIDA)
            datos.put("PRECIO", PRECIO)
            datos.put("PASAJERO", PASAJERO)
            datos.put("STATUS", STATUS)
            datos.put("FILA", FILA)
            datos.put("COLUMNA", COLUMNA)
            datos.put("VENDEDOR", VENDEDOR)
            datos.put("PISO", PISO)
            datos.put("CORTE", CORTE)
            datos.put("PK_USUARIO", PK)
            datos.put("RUTA", RUTA)
            datos.put("TIPO", TIPO)
            datos.put("FORMADEPAGO", FORMADEPAGO)
            datos.put("SUCURSAL", ORIGEN)
            datos.put("PK_CORRIDA_DIA", PK_CORRIDA_DIA)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Method.POST, "https://appis.atah.online/api/MapaAutobus/Venta", datos,
            Response.Listener { response ->
                try {

                    progressDialog?.dismiss()
                    val result = response["resultado"] as String





                    if (result == "1") {
                        folio = response["folio"] as String
//                        if(FECHA.length<25)
//                        {
//                            for (i in FECHA.length..25) {
//                                FECHA = FECHA+" "
//                            }
//                        }
//                        if(folio.length<25)
//                        {
//                            for (i in folio.length..25) {
//                                folio = folio+" "
//                            }
//                        }
//                        if(ASIENTO.length<20)
//                        {
//                            for (i in ASIENTO.length..20) {
//                                ASIENTO = ASIENTO+" "
//                            }
//                        }
//                        if(FORMADEPAGO.length<20)
//                        {
//                            for (i in FORMADEPAGO.length..20) {
//                                FORMADEPAGO = folio+" "
//                            }
//                        }
                        var org2 = ORIGEN
                        if (org2.length < 15) {
                            for (i in org2.length..15) {
                                org2 = org2 + " "
                            }
                        }

                        var DESTINO2 = DESTINO
                        if (DESTINO.length > 18) {
                     DESTINO2=DESTINO.substring( 0, 17)

                            DESTINO2 = DESTINO2 + "..."

                        }


                        try {
                            val texto1 = "FECHA:" + FECHA
                            val tlinea  =  "  LINEA:" + LINEA + "\n"
                            // val texto11 = FECHA+ "  "+LINEA+"\n"
                            val texto2 = "            " + folio+"          "
                            val tautobus= "     AUTOBUS:" + AUTOBUS + "\n"
                            // val texto22 = folio+ "  "+AUTOBUS+"\n"
                            val texto3 = "ASIENTO:" + ASIENTO
                            val tsalida= " SALIDA:" + SALIDA + "\n"

                            //  val texto33 =  ASIENTO+ "  "+PASAJERO+"\n"
                            val texto4 = "ORIGEN:"+org2
                            val textodest="DESTINO:"+"\n"
val tdestino= DESTINO2 + "\n"

                            //     val texto5 = "          SALIDA:"+"\n"
                            //   val texto55 = "       "+SALIDA+"\n"


                            val texto6 = "PAGO:EFECTIVO  PRECIO:$" + PRECIO + "\n"
                            // val texto66 =
                            //"EFECTIVO" + "                     " + "#" + PRECIO + ".00"
                            val texto666 =
                                "Presente identificación original y vigenete al momento de abordar.\n"
                            val texto6666 =
                                "Atención a clientes: 01 800 836 0726 \nTterminos y condiciónes www.atah.online\n\n\n"


                            val fuente1 = 1
                            val negrita1 = 1
                            val ancho2 = 1

                            val fuente = 0
                            val negrita = 0
                            val ancho = 0
                            val alto = 0
                            val ANCHO_IMG_58_MM = 384
                            val MODE_PRINT_IMG = 0
                            // Get the bitmap from assets and display into image view
                            // Get the bitmap from assets and display into image view
                            val decodedString =
                                Base64.decode(imagenlogo, Base64.DEFAULT)
                            val bitmap =
                                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                            outputStream!!.write(
                                PrintBitmap.POS_PrintBMP(
                                    bitmap,
                                    100,
                                    MODE_PRINT_IMG
                                )
                            )


                            // Para que acepte caracteres espciales


                            outputStream!!.write(
                                getByteString(
                                    texto1,
                                    negrita,
                                    fuente1,
                                    ancho,
                                    alto
                                )
                            )

                            outputStream!!.write(
                                getByteString(
                                    tlinea,
                                    negrita,
                                    fuente,
                                    ancho,
                                    alto
                                )
                            )
                            outputStream!!.write(
                                getByteString(
                                    texto4,
                                    negrita,
                                    fuente,
                                    ancho,
                                    alto
                                )
                            )


                            outputStream!!.write(
                                getByteString(
                                    textodest,
                                    0,
                                    fuente1,
                                    0,
                                    0
                                )
                            )


                            outputStream!!.write(
                                getByteString(
                                    tdestino,
                                    1,
                                    fuente1,
                                    1,
                                    1
                                )
                            )



                            //    outputStream!!.write(getByteString(texto11, negrita1, fuente1, ancho, alto))

                            outputStream!!.write(
                                getByteString(
                                    tautobus,
                                    2,
                                    fuente1,
                                    1,
                                    1
                                )
                            )
                            //  outputStream!!.write(getByteString(texto22, negrita1, fuente1, ancho, alto))
                            outputStream!!.write(
                                getByteString(
                                    texto3,
                                    negrita,
                                    fuente,
                                    ancho,
                                    alto
                                )
                            )
                            outputStream!!.write(
                                getByteString(
                                    tsalida,
                                    2,
                                    fuente1,
                                    1,
                                    1
                                )
                            )
                            // outputStream!!.write(getByteString(texto33, negrita1, fuente1, ancho, alto))


                            //outputStream!!.write(getByteString(texto5, negrita, fuente, ancho, alto))
                            //outputStream!!.write(getByteString(texto55, negrita1, fuente1, ancho2, alto))
                            outputStream!!.write(
                                getByteString(
                                    texto6,
                                    negrita,
                                    fuente,
                                    ancho,
                                    alto
                                )
                            )
//                            outputStream!!.write(
//                                getByteString(
//                                    texto66,
//                                    negrita1,
//                                    fuente1,
//                                    ancho,
//                                    alto
//                                )
//                            )

                            outputStream.write("\n".toByteArray())

//                            val bitmapqr = QRCode.from(folio).withSize(ANCHO_IMG_58_MM ,ANCHO_IMG_58_MM) .bitmap()
//                            outputStream!!.write(
//                                PrintBitmap.POS_PrintBMP(
//                                    bitmapqr,
//                                    295,
//                                    MODE_PRINT_IMG
//                                )
//                            )


                            try {
                                val productId: String = folio
                                val hintMap: Hashtable<EncodeHintType, ErrorCorrectionLevel> =
                                    Hashtable<EncodeHintType, ErrorCorrectionLevel>()
                                hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
                                val codeWriter: Writer
                                codeWriter = Code128Writer()
                                val byteMatrix: BitMatrix = codeWriter.encode(
                                    productId,
                                    BarcodeFormat.CODE_128,
                                    400,
                                    100,
                                    hintMap
                                )
                                val width: Int = byteMatrix.getWidth()
                                val height: Int = byteMatrix.getHeight()
                                val bitmap =
                                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                                for (i in 0 until width) {
                                    for (j in 0 until height) {
                                        bitmap.setPixel(
                                            i,
                                            j,
                                            if (byteMatrix.get(i, j)) Color.BLACK else Color.WHITE
                                        )
                                    }
                                }
                                val bitmapqr = bitmap

                                outputStream!!.write(
                                    PrintBitmap.POS_PrintBMP(
                                        bitmapqr,
                                        380,
                                        MODE_PRINT_IMG
                                    )
                                )

                            } catch (e: Exception) {
                                val a = e
                            }
                            outputStream!!.write(
                                getByteString(
                                    texto2,
                                    negrita,
                                    fuente,
                                    ancho,
                                    alto
                                )
                            )
                            outputStream.write("\n".toByteArray())
                            outputStream!!.write(
                                getByteString(
                                    texto666,
                                    negrita,
                                    fuente1,
                                    0,
                                    0
                                )
                            )
                            outputStream!!.write(
                                getByteString(
                                    texto6666,
                                    negrita,
                                    fuente1,
                                    0,
                                    0
                                )
                            )


                        } catch (e: IOException) {

                            Toast.makeText(
                                requireActivity(),
                                "Error al interntar imprimir texto",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }

                    } else {
                        val error = response.getString("mensaje")
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()

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

    fun corrida() {

//        val progressDialog = ProgressDialog(
//            requireActivity(),
//            R.style.Theme_AppCompat_Light_Dialog
//        )
//        progressDialog.isIndeterminate = true
//        progressDialog.setMessage("Cargando datos...")
//        progressDialog.show()
        val preferencias = this.requireActivity().getSharedPreferences(
            "variables",
            Context.MODE_PRIVATE
        )
        val pk_origen= preferencias.getString("sucursal", "0")!!
        val pko=pk_origen.toInt()
        val pkl=idlinea.get(posicionlinea.toInt())
        val pkd=pkdestino.get(posiciondestino.toInt())

        val datos = JSONObject()
        try {
            datos.put("FECHA", fecha)
              datos.put("PK_LINEA", pkl)
            datos.put("PK_ORIGEN", pko)
            datos.put("PK_DESTINO", pkd)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Method.POST, "https://appis.atah.online/api/Corridas/CorridasByFechaOrigenLinea", datos,
            Response.Listener { response ->
                try {
//                    progressDialog?.dismiss()
                    val result = response["result"] as Int
                    if (result == 1) {

                        var i = horario.selectedItemPosition
                        posicionhorario = horario.selectedItemPosition.toString()
                        SALIDA = horarios.get(i)
                        val costos = response.getJSONArray("corridas")
                        var a = 0
                        for (i in 0 until costos.length()) {
                            val producto = costos.getJSONObject(i)
                            val SALID = producto.getString("salida")

                            if (SALID == SALIDA) {

                                LINEA = producto.getString("linea")
                                PKCORRIDA = producto.getString("pk")
                                AUTOBUS = producto.getString("autobus")
                                econombre.setText(AUTOBUS)

                                ASIENTO = "PIE"
                                DESTINO_COMPLETO = producto.getString("destinO_COMPLETO")
                                SALIDA = producto.getString("salida")
                                LLEGADA_C = producto.getString("llegadA_C")
                                SALIDA_C = producto.getString("salidA_C")
                                PK_CORRIDA_DIA = producto.getString("pk")
                                PASAJERO = "Publico en general"
                                STATUS = "VENDIDO"
                                FILA = "100"
                                COLUMNA = "100"
                                PISO = "1"
                                CORTE = "0"
                                RUTA = producto.getString("ruta")
                                //   CHOFER = producto.getString("CHOFER")
                                TIPO = "PIE"
                                FORMADEPAGO = "Efectivo"
                            }

                        }


                    } else {
                        val error = response.getString("mensaje")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
//                progressDialog?.dismiss()
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


    fun onClickTest() {
        Print.Lib_PrnInit()
        Print.Lib_PrnSetVoltage(this@Infracciones.BatteryV * 2 / 100)
        Print.Lib_PrnSetCharSpace(0)
        Print.Lib_PrnSetLineSpace(0)
        Print.Lib_PrnSetLeftIndent(0)
        Print.Lib_PrnSetAlign(0)

            Print.Lib_PrnSetFont(EscPrintCls.DLE, EscPrintCls.DLE, 51.toByte())
            Print.Lib_PrnStr("POS签购单/POS SALES SLIP\n")
            Print.Lib_PrnSetFont(EscPrintCls.DLE, EscPrintCls.DLE, 0.toByte())
            Print.Lib_PrnStr("商户存根MERCHANT COPY\n")
            Print.Lib_PrnStr("- - - - - - - - - - - - - - - - - - - - - - - -\n")
            Print.Lib_PrnSetFont(24.toByte(), 24.toByte(), 0.toByte())
            Print.Lib_PrnStr("商户名称(MERCHANT NAME):\n")
            Print.Lib_PrnStr("中国银联直连测试\n")
            Print.Lib_PrnStr("商户编号(MERCHANT NO):\n")
            Print.Lib_PrnStr("    001420183990573\n")
            Print.Lib_PrnStr("终端编号(TERMINAL NO):00026715\n")
            Print.Lib_PrnStr("操作员号(OPERATOR NO):12345678\n")
            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n")
            Print.Lib_PrnStr("发卡行(ISSUER):01020001 工商银行\n")
            Print.Lib_PrnStr("卡号(CARD NO):\n")
            Print.Lib_PrnStr("    9558803602109503920\n")
            Print.Lib_PrnStr("收单行(ACQUIRER):03050011民生银行\n")
            Print.Lib_PrnStr("交易类型(TXN. TYPE):消费/SALE\n")
            Print.Lib_PrnStr("卡有效期(EXP. DATE):2013/08\n")
            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n")
            Print.Lib_PrnStr("批次号(BATCH NO)  :000023\n")
            Print.Lib_PrnStr("凭证号(VOUCHER NO):000018\n")
            Print.Lib_PrnStr("授权号(AUTH NO)   :987654\n")
            Print.Lib_PrnStr("日期/时间(DATE/TIME):\n")
            Print.Lib_PrnStr("    2008/01/28 16:46:32\n")
            Print.Lib_PrnStr("交易参考号(REF. NO):200801280015\n")
            Print.Lib_PrnStr("金额(AMOUNT):  RMB:2.55\n")
            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n")
            Print.Lib_PrnStr("备注/REFERENCE\n")
            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n")
            Print.Lib_PrnSetFont(EscPrintCls.DLE, EscPrintCls.DLE, 0.toByte())
            Print.Lib_PrnStr("持卡人签名(CARDHOLDER SIGNATURE)\n")
            Print.Lib_PrnStr("\n")
            Print.Lib_PrnStr("- - - - - - - - - - - - - - - - - - - - - - - -\n")
            Print.Lib_PrnStr("  本人确认以上交易，同意将其计入本卡帐户\n")
            Print.Lib_PrnStr("  I ACKNOWLEDGE SATISFACTORY RECEIPT\n")
            Print.Lib_PrnStr("                                         ")
            Print.Lib_PrnStr("\n")
            Print.Lib_PrnStr("                                         ")
            Print.Lib_PrnStr("\n")
            Print.Lib_PrnStr("\n")
            Print.Lib_PrnStr("\n")
            Print.Lib_PrnStr("\n")
            Print.Lib_PrnStr("\n")
           val ret = Print.Lib_PrnStart()


    }

    fun generarguia(){

        val intent = Intent(requireActivity(), GenerarGuia::class.java)
        intent.putExtra("ORIGEN", ORIGEN);
        intent.putExtra("HORA", SALIDA_C);
        intent.putExtra("PKCORRIDA", PKCORRIDA);

        intent.putExtra("DESTINO", DESTINO_COMPLETO);
        intent.putExtra("FECHA", FECHA);
        intent.putExtra("LINEA", LINEA);
        intent.putExtra("AUTOBUS", AUTOBUS);

        startActivity(intent)
    }

}




