package com.examen.firebaseproyect.Clases

class Ubicacion {
    var key:String=""
    var IdUsuario:String=""
    var Latitud:String=""
    var Longitud:String=""
    var Calle:String=""

    constructor()
    constructor(key: String, IdUsuario: String, Latitud: String, Longitud: String, Calle: String) {
        this.key = key
        this.IdUsuario = IdUsuario
        this.Latitud = Latitud
        this.Longitud = Longitud
        this.Calle = Calle
    }

}