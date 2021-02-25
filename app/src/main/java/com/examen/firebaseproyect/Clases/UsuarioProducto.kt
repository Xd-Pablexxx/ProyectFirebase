package com.examen.firebaseproyect.Clases

import java.io.Serializable

class UsuarioProducto:Serializable {
    var key:String=""
    var keyUsuario:String=""
    var keyProducto:String=""
    var keyUsuarioProducto:String=""
    var puntuacion:String=""
    constructor()
    constructor(
        key: String,
        keyUsuario: String,
        keyProducto: String,
        keyUsuarioProducto: String,
        puntuacion: String
    ) {
        this.key = key
        this.keyUsuario = keyUsuario
        this.keyProducto = keyProducto
        this.keyUsuarioProducto = keyUsuarioProducto
        this.puntuacion = puntuacion
    }


}