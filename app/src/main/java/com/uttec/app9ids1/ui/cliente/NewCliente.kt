package com.uttec.app9ids1.ui.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.uttec.app9ids1.R
import com.uttec.app9ids1.databinding.FragmentNewClienteBinding
import com.uttec.app9ids1.extras.Models
import com.uttec.app9ids1.extras.VariablesGlobales
import okhttp3.*
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "json_cliente"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewCliente.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewCliente : Fragment() {

    private var _binding: FragmentNewClienteBinding? = null
    private val binding get() = _binding!!

    private var id_cliente:Int = 0


    // TODO: Rename and change types of parameters
    private var json_cliente: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            json_cliente = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewClienteBinding.inflate(inflater, container, false)
        val view = binding.root

        if(json_cliente != null){
            var objGson = Gson()
            var objCliente = objGson.fromJson(json_cliente, Models.Cliente::class.java)

            id_cliente = objCliente.id

            binding.txtNombre.editText?.setText(objCliente.nombre)
            binding.txtApPat.editText?.setText(objCliente.ap_pat)
            binding.txtApMat.editText?.setText(objCliente.ap_mat)
            binding.txtEmail.editText?.setText(objCliente.email)
            binding.txtCelular.editText?.setText(objCliente.celular.toString())
            binding.txtStatus.editText?.setText(objCliente.estatus)
        } else {
            binding.btnEliminar.isVisible = false
        }

        binding.btnGuardar.setOnClickListener {

            var url = VariablesGlobales.url_save_client

            val formBody: RequestBody = FormBody.Builder()
                .add("id", id_cliente.toString() )
                .add("nombre", binding.txtNombre.editText?.text.toString() )
                .add("ap_pat", binding.txtApPat.editText?.text.toString() )
                .add("ap_mat", binding.txtApMat.editText?.text.toString() )
                .add("email", binding.txtEmail.editText?.text.toString() )
                .add("celular", binding.txtCelular.editText?.text.toString() )
                .add("estatus", binding.txtStatus.editText?.text.toString() )

                .build()

            val request = Request
                .Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + VariablesGlobales.TOKEN)
                .post(formBody)
                .build()
            val client = OkHttpClient()
            var objGson = Gson()

            client.newCall(request).enqueue( object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Fallo la peticion: " + e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    activity?.runOnUiThread {
                        findNavController().navigate(R.id.nav_home)
                    }
                    println("Guardado Correctamente: " +response.body?.string() )
                }

            })

            println("Termino peticion")
        }

        binding.btnEliminar.setOnClickListener{
            if(id != 0){
                var url = VariablesGlobales.url_delete_client

                val formBody: RequestBody = FormBody.Builder()
                    .add("id", id_cliente.toString() )
                    .build()

                val request = Request
                    .Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + VariablesGlobales.TOKEN)
                    .post(formBody)
                    .build()
                val client = OkHttpClient()
                var objGson = Gson()

                client.newCall(request).enqueue( object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Fallo la peticion: " + e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        activity?.runOnUiThread {
                            findNavController().navigate(R.id.nav_home)
                        }
                        println("Eliminado Correctamente: " +response.body?.string() )
                    }

                })

                println("Termino peticion")
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewCliente.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewCliente().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
