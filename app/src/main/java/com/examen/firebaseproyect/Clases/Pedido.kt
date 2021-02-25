package com.examen.firebaseproyect.Clases

import java.io.Serializable

class Pedido:Serializable {
    var key:String=""
    var keyUsuario:String=""
    var keyUbicacion:String=""
    var total:String=""
    var fecha:String=""
    var estado:String=""

    constructor()
    constructor(
        key: String,
        keyUsuario: String,
        keyUbicacion: String,
        total: String,
        fecha: String,
        estado: String
    ) {
        this.key = key
        this.keyUsuario = keyUsuario
        this.keyUbicacion = keyUbicacion
        this.total = total
        this.fecha = fecha
        this.estado=estado
    }

}