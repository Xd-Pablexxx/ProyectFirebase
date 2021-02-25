package com.examen.firebaseproyect.Clases

import java.io.Serializable

class Producto:Serializable {
    var key: String = ""
    var nombre: String = ""
    var precio: String = ""
    var descripcion:String=""
    var keycategoria:String=""
    var img:String=""

    constructor()
    constructor(
        key: String,
        nombre: String,
        precio: String,
        descripcion: String,
        keycategoria: String,
        img: String
    ) {
        this.key = key
        this.nombre = nombre
        this.precio = precio
        this.descripcion = descripcion
        this.keycategoria = keycategoria
        this.img = img
    }


}