package com.nishiyama.lista

import android.content.Context
import android.content.SharedPreferences

class DataProcess(context: Context) {
    var context: Context
    fun setInt(key: String?, value: Int) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()

    }

    fun getInt(key: String?): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt(key, 0)
    }

    fun setStr(key: String?, value: String?) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStr(key: String?): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "DNF")
    }

    fun setBool(key: String?, value: Boolean) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?): Boolean {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, false)
    }

    fun getSize(): Int {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        return (sharedPref.all.size) / 4
    }

    fun clean() {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.clear().commit()
    }

    companion object {
        const val PREFS_NAME = "lista_data"
    }

    init {
        this.context = context
    }
}