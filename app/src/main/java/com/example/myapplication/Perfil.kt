package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_infracciones.*
import kotlinx.android.synthetic.main.activity_perfil.*


class Perfil : Fragment() {
    companion object {
        fun newInstance(): Perfil = Perfil()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_perfil, container, false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(state: Bundle?)
    {
        super.onActivityCreated(state)
        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)

        var role=preferencias.getString("rol", "").toString()

            var nombre = preferencias.getString("nombre", "").toString()+" "+
                    preferencias.getString("apellidos", "").toString()

        var  telefono= preferencias.getString("sucursaltext", "").toString()

        nombretext.text=nombre
        direcciontext.text=telefono
        cerrarcesion.setOnClickListener(View.OnClickListener {
            cerrarsesio()
        })

    }
    fun cerrarsesio(){
        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)

        val editor = preferencias.edit()
        editor.putString("sesion", "no")
        editor.commit()
        val intent = Intent(requireActivity(), Login::class.java)

        // start your next activity
        startActivity(intent)
        requireActivity().finish();
    }

}
