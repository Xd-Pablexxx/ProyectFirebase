package com.examen.firebaseproyect.Clases

import java.io.Serializable

class Recomendacion: Serializable {
    var key: String = ""
    var nombre: String = ""
    var precio: String = ""
    var img:String=""

    constructor()
    constructor(
        key: String,
        nombre: String,
        precio: String,
        img: String
    ) {
        this.key = key
        this.nombre = nombre
        this.precio = precio
        this.img = img
    }


}