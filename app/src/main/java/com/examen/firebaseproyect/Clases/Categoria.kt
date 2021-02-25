package com.examen.firebaseproyect.Clases

import java.io.Serializable

class Categoria:Serializable {
    var key:String=""
    var nombre:String=""
    var descripcion:String=""
    var img:String=""

    constructor()
    constructor(key: String, nombre: String, descripcion: String, img: String) {
        this.key = key
        this.nombre = nombre
        this.descripcion = descripcion
        this.img = img
    }

}