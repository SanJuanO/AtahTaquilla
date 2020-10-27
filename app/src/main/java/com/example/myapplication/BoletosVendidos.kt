package com.example.myapplication

import BuscarModel
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONException
import org.json.JSONObject
import java.util.*


class BoletosVendidos : Fragment() {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private val ARG_PARAM1 = "param1"

    private val URL: String? = null
    var names = ArrayList<String>()
    var names2 = ArrayList<String>()
    lateinit var list:ListView;
    var myDialog: Dialog? = null
    lateinit var progressDialog:ProgressDialog
    var busqueda = String()
    var ids = ArrayList<String>()
    var idu=0
    var ida=0
    var idp=0
    var idc=0
    var ide=0
    private var recyclerView: RecyclerView? = null
    private var adapter: CardAdapter_busqueda_boletos? = null
    private var planetArrayList: ArrayList<Planet>? = null

    var idubicacion = ArrayList<String>()
    var ubicacion = ArrayList<String>()
    var idarea = ArrayList<String>()
    var areas = ArrayList<String>()
    var idproveedor = ArrayList<String>()
    var proveedor = ArrayList<String>()
    var idestado = ArrayList<String>()
    var estado = ArrayList<String>()
    var idclase = ArrayList<String>()
    var clase = ArrayList<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_boletos_vendidos, container, false)

        myDialog =  Dialog(requireActivity());
        recyclerView = rootView.findViewById<View>(R.id.recyclerViewb) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
        planetArrayList = ArrayList()
        adapter = CardAdapter_busqueda_boletos(requireActivity(), planetArrayList)
        recyclerView!!.adapter = adapter
        guardaDatos()

      return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() =BoletosVendidos()
    }

    fun guardaDatos() {

        val progressDialog = ProgressDialog(requireActivity(),
            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)
        val pk= preferencias.getString("pk","0")!!
        val datos = JSONObject()
        try {
            datos.put("PK_USUARIO",pk.toInt())

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(activity)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://appis.atah.online/api/Boletos/ObtieneBoletos", datos,
            Response.Listener { response ->
                try {
                    val result = response["result"] as Int
                    progressDialog.dismiss()
                    if (result == 1) {
                        names2.clear()
                        ids.clear()
                        planetArrayList?.clear()

                        val costos = response.getJSONArray("vendidos")
                        for (i in 0 until costos.length()) {
                            val producto = costos.getJSONObject(i)

                            var planet = Planet("  Folio:"+
                                producto.getString("folio"),
                                "  Total:"+"$"+producto.getString("precio")+ System.getProperty ("line.separator")+"  Tarifa:"+   producto.getString("tarifa"),
                                "  Asiento:"+producto.getString("asiento"),
                                " "+producto.getString("sucursal")+"-"+producto.getString("destinoboleto"),
                                producto.getString("salida"),
                                producto.getString("pk"),""
                            )
                            planetArrayList!!.add(planet)
                        }

                        adapter!!.notifyDataSetChanged()



                    } else {
                        progressDialog.dismiss()

                        val error = response.getString("mensaje")
                    }
                } catch (e: JSONException) {
                    progressDialog.dismiss()

                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                progressDialog.dismiss()

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


}


