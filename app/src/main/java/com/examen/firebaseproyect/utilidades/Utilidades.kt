package com.example.koalito.Bd.utilidades

object Utilidades {
    const val TABLA_COMPRA = "Compra"
    const val CAMPO_ID = "id"
    const val CAMPO_IDPRODUCTO="idproducto"
    const val CAMPO_CANTIDAD= "cantidad"
    const val CAMPO_PRECIO="precio"
    const val CAMPO_SUBTOTAL="subtotal"
    const val CAMPO_SUGERENCIA="sugerencia"

    // var for the users
    const val TABLA_USUARIOS= "Usuario"
    const val CAMPO_IDUSUARIO="key"
    const val CAMPO_USERNAME="username"
    const val CAMPO_PASSWORD="password"
    const val CAMPO_NOMBRE="nombre"
    const val CAMPO_APELLIDOS="apellidos"
    const val CAMPO_EDAD="edad"
    const val CAMPO_CELULAR="celular"
    const val CAMPO_TIPO="tipo"

    // var for the ubicacion
    const val TABLA_UBICACION="ubicacion"
    const val CAMPO_CALLE="calle"
    const val CAMPO_LATITUD="latitud"
    const val CAMPO_LONGITUD="longitud"
    const val CREAR_TABLA_COMPRA= "CREATE TABLE " + TABLA_COMPRA + "(" + CAMPO_ID + " INTEGER PRIMARY KEY,"+ CAMPO_IDPRODUCTO+" TEXT,"  + CAMPO_CANTIDAD + " TEXT,"+ CAMPO_PRECIO+ " TEXT,"+ CAMPO_SUBTOTAL  + " TEXT,"+ CAMPO_SUGERENCIA+" TEXT)"
    const val CREAR_TABLA_UBICACION="CREATE TABLE ${TABLA_UBICACION}(${CAMPO_CALLE} TEXT,${CAMPO_LATITUD} TEXT,${CAMPO_LONGITUD} TEXT)"

    const val CREAR_TABLA_USUARIO= "CREATE TABLE ${TABLA_USUARIOS}(${CAMPO_IDUSUARIO} INTEGER,${CAMPO_USERNAME} TEXT,${CAMPO_PASSWORD} TEXT," +
            "${CAMPO_NOMBRE} TEXT,${CAMPO_APELLIDOS} TEXT,${CAMPO_EDAD} TEXT,${CAMPO_CELULAR} TEXT,${CAMPO_TIPO} TEXT)"

    const val ACTUALIZACION_TABLA_COMPRA= "DROP TABLE IF EXISTS ${TABLA_COMPRA}"
    const val ACTUALIZACION_TABLA_UBICACION= "DROP TABLE IF EXISTS ${TABLA_UBICACION} "
    const val ACTUALIZACION_TABLA_USUARIO = "DROP TABLE IF EXISTS " + TABLA_USUARIOS
}