package com.examen.firebaseproyect.utilidades

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.koalito.Bd.utilidades.ConexionSQLiteHelper

class Conexion {
    fun bd(context: Context): SQLiteDatabase
    {
        var con = ConexionSQLiteHelper(context, "bd_compras", null, 1)
        val db: SQLiteDatabase = con.getReadableDatabase()
        return db
    }
}