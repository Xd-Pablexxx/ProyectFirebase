package com.example.koalito.Bd.utilidades

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper : SQLiteOpenHelper {

    constructor(context: Context?, name: String?, factory: CursorFactory?, version: Int) : super(
        context,
        name,
        factory,
        version
    )


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Utilidades.CREAR_TABLA_COMPRA)
        db.execSQL(Utilidades.CREAR_TABLA_USUARIO)
        db.execSQL(Utilidades.CREAR_TABLA_UBICACION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Utilidades.ACTUALIZACION_TABLA_COMPRA)
        db.execSQL(Utilidades.ACTUALIZACION_TABLA_USUARIO)
        db.execSQL(Utilidades.ACTUALIZACION_TABLA_UBICACION)

        onCreate(db)
    }
}