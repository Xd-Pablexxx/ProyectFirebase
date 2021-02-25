package com.examen.firebaseproyect.Clases

import java.io.Serializable

class Usuario:Serializable {
    var key:String=""
    var usuario:String=""
    var password:String=""
    var nombre:String=""
    var apellidos:String=""
    var edad:String=""
    var celular:String=""
    var fechaCreacion:String=""
    var tipo:String=""

    constructor(
        key: String,
        usuario: String,
        password: String,
        nombre: String,
        apellidos: String,
        edad: String,
        celular: String,
        fechaCreacion:String,
        tipo:String
    ) {
        this.key = key
        this.usuario = usuario
        this.password = password
        this.nombre = nombre
        this.apellidos = apellidos
        this.edad = edad
        this.celular = celular
        this.fechaCreacion=fechaCreacion
        this.tipo=tipo
    }

    constructor()

}