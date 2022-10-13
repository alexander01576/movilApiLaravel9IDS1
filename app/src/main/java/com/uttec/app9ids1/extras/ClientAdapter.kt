package com.uttec.app9ids1.extras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.uttec.app9ids1.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ClientAdapter (private val dataSet: Array<Models.Cliente>) :
    RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtNombre: TextView
        val txtEmail: TextView

        init {
            // Define click listener for the ViewHolder's View.
            txtNombre = view.findViewById(R.id.txtNombre)
            txtEmail= view.findViewById(R.id.txtEmail)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.itemView.setOnClickListener {

            var objGson = Gson()
            var json_cliente = objGson.toJson(dataSet[position])

            var navController = Navigation.findNavController(it)
            val bundle = bundleOf("json_cliente" to json_cliente)

            navController.navigate(R.id.nav_newCliente, bundle)
            //navController.popBackStack(R.id.nav_gallery, false)
            //println("Hola: " + dataSet[position])
        }

        viewHolder.txtNombre.text = dataSet[position]?.nombre
        viewHolder.txtEmail.text = dataSet[position]?.email
        //val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "US"))
        //val formatter: NumberFormat = DecimalFormat("$#,##0.00")

        //viewHolder.txtPrecio.text = formatter.format(dataSet[position]?.price)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
