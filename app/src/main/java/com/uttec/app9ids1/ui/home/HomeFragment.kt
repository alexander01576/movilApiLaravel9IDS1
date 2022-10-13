package com.uttec.app9ids1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.uttec.app9ids1.databinding.FragmentHomeBinding
import com.uttec.app9ids1.extras.ClientAdapter
import com.uttec.app9ids1.extras.Models
import com.uttec.app9ids1.extras.VariablesGlobales
import okhttp3.*
import java.io.IOException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private var TOKEN: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        TOKEN = activity?.intent?.extras?.getString("VAR_TOKEN")

        VariablesGlobales.TOKEN = TOKEN

        println("Token en home2: " + TOKEN)

        obtenerClientes()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun obtenerClientes(){

        val url = VariablesGlobales.url_get_client

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + TOKEN)
            .get()
            .build()

        val client = OkHttpClient()
        var gson = Gson()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrio un error: " + e.message.toString(), Toast.LENGTH_SHORT)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()

                println("Respuesta: " + respuesta)

                activity?.runOnUiThread {
                    var listItems = gson.fromJson(respuesta, Array<Models.Cliente>::class.java)

                    val adapter = ClientAdapter(listItems);
                    binding.rvDatos.layoutManager = LinearLayoutManager(context)
                    binding.rvDatos.adapter = adapter
                }
            }
        })
    }
}
