package com.uttec.app9ids1.extras

class VariablesGlobales {
    companion object {
        val url_app = "http://192.168.100.124:8000/"
        val url_login = url_app + "api/login"
        val url_get_client = url_app + "api/cliente/list"
        val url_save_client = url_app + "api/cliente/save"
        val url_delete_client = url_app + "api/cliente/delete"
        val url_get_tipos = url_app + "api/cliente/tipos"

        var TOKEN:String? = null

    }


}
