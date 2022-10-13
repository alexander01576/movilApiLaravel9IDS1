package com.uttec.app9ids1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.uttec.app9ids1.databinding.ActivityMainBinding
import com.uttec.app9ids1.extras.Models
import com.uttec.app9ids1.extras.VariablesGlobales
import okhttp3.*
import java.io.IOException

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Picasso
            .get()
            .load("https://carta.v-card.es/assets/images/user/login.png")
            .fit()
            .into(binding.imgLogin)

        binding.btnAccesar.setOnClickListener{
            /* Toast.makeText(this@MainActivity, "Hola " + binding.txtUsuario.editText?.text.toString() , Toast.LENGTH_SHORT).show() */
            validarAcceso()
        }
    }
    private fun validarAcceso(){
        var url = VariablesGlobales.url_login

        val formBody: RequestBody = FormBody.Builder()
            .add("email", binding.txtUsuario.editText?.text.toString() )
            .add("password", binding.txtPassword.editText?.text.toString() )
            .build()

        val request = Request.Builder().url(url).post(formBody).build()
        val client = OkHttpClient()
        var objGson = Gson()

        client.newCall(request).enqueue( object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Fallo la peticion: " + e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()

                var objRespuesta = objGson.fromJson(respuesta, Models.RespuestaLogin::class.java)

                if(objRespuesta.acceso == "Ok"){
                    runOnUiThread {
                        /*Toast.makeText(this@MainActivity, "Acceso Correcto ", Toast.LENGTH_SHORT).show()*/
                       // var intento = Intent(this@MainActivity, HomeActivity::class.java)
                        var intento = Intent(this@MainActivity, Home2Activity::class.java).apply {
                            putExtra("VAR_TOKEN", objRespuesta.token)
                        }

                        startActivity(intento)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, objRespuesta.error, Toast.LENGTH_SHORT).show()

                    }
                }

                println("Error: " + objRespuesta.error)
                println("Token: " + objRespuesta.token)
                println("Acceso: " + objRespuesta.acceso)

            }

        })

        println("Termino peticion")

    }
}
