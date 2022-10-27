package com.uttec.app9ids1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.uttec.app9ids1.databinding.ActivityMainBinding
import com.uttec.app9ids1.extras.Models
import com.uttec.app9ids1.extras.VariablesGlobales
import okhttp3.*
import java.io.IOException
import java.util.jar.Manifest

private lateinit var binding: ActivityMainBinding

//var GPS
private lateinit var fusedLocationClient: FusedLocationProviderClient


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

        //inicialiacion del GPS
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        binding.fab.setOnClickListener{

            locationPermissionRequest.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                            CancellationTokenSource().token

                        override fun isCancellationRequested() = false

                    }).addOnSuccessListener {
                    //latGPS = it.latitude
                    //longGPS = it.longitude

                    println("Lat: "+ it.latitude.toString() + ", Long:" + it.longitude.toString())

                    try {
                        // Launch Waze
                        val url = "https://waze.com/ul?ll=${it.latitude.toString()},${it.longitude.toString()}&z=10"
                        //val url = "https://wa.me/15551234567?text=I'm%20interested%20in%20your%20car%20for%20sale"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        // If Waze is not installed, open it in Google Play:
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"))
                        startActivity(intent)
                    }

                }
            }
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

                println(objGson)
                println(respuesta)
                println(objGson.fromJson(respuesta, Models.RespuestaLogin::class.java))

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

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Fine location access granted
            }
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }
}
