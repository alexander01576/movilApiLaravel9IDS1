package com.uttec.app9ids1.extras

class Models {
    data class RespuestaLogin(
        var acceso:String,
        var error:String,
        var token:String
        )

    data class Cliente(
        var id: Int,
        var nombre: String,
        var ap_pat: String,
        var ap_mat: String,
        var email: String,
        var celular: Long,
        var estatus: String,
        var imagen: String?,
        var nombreTransporte: String?
    )

    data class Tipos(
        var id: Int,
        var nombre: String
    ){
        override fun toString(): String {
            return id.toString() + ": " + nombre
        }
    }

    data class Reservas (
        var id_reserva: Int,
        var estatus_reserva: String,
        var fecha_reserva: String,
        var nombre_cliente: String
            )
}
