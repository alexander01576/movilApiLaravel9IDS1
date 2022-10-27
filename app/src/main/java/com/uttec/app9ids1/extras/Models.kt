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
        var imagen: String
        )

    data class Tipos(
        var id: Int,
        var name: String
    ){
        override fun toString(): String {
            return id.toString() + ": " + name
        }
    }
}
