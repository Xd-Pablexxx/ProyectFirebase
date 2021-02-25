package com.examen.firebaseproyect.Clases

import java.io.Serializable

class CompraOferta: Serializable {
    var key:String=""
    var idOferta:String=""
    var idPedido:String=""
    var cantidad:String=""
    var precio:String=""
    var subtotal:String=""
    var sugerencia:String=""

    constructor()
    constructor(
        key: String,
        idOferta: String,
        idPedido: String,
        cantidad: String,
        precio: String,
        subtotal: String,
        sugerencia: String
    ) {
        this.key = key
        this.idOferta = idOferta
        this.idPedido = idPedido
        this.cantidad = cantidad
        this.precio = precio
        this.subtotal = subtotal
        this.sugerencia = sugerencia
    }
}