package com.uttec.app9ids1.extras

class VariablesGlobales {
    companion object {
        //ip casa = 192.168.0.8
        //ip telefonorojo = 192.168.137.1

        val url_app = "http://192.168.0.8:8000/"
        val url_login = url_app + "api/login"
        val url_get_client = url_app + "api/cliente/list"
        val url_save_client = url_app + "api/cliente/save"
        val url_delete_client = url_app + "api/cliente/delete"

        var TOKEN:String? = null

    }


}
