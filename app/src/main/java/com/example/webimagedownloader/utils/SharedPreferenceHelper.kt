package com.example.webimagedownloader.utils

import android.content.Context
import android.content.SharedPreferences


object SharedPreferenceHelper {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun create(context: Context) {
        preferences = context.getSharedPreferences("shared pref", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun addBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).commit()
    }

    fun addString(key: String, value: String) {
        editor.putString(key, value).commit()
    }

    fun addInt(key: String, value: Int) {
        editor.putInt(key, value).commit()
    }

    fun addFloat(key: String, value: Float) {
        editor.putFloat(key, value).commit()
    }

    fun addLong(key: String, value: Long) {
        editor.putLong(key, value).commit()
    }

    fun addStringSet(key: String, value: Set<String>) {
        editor.putStringSet(key, value).commit()
    }

    /*fun addObject(key: String, value: Any) {
        val gson = Gson()
        editor.putString(key, gson.toJson(value)).commit()
    }*/

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        return preferences.getStringSet(key, defaultValue)
    }

    /*fun <T> getObject(key: String, classType: Class<T>): T? {
        if (preferences.contains(key)) {
            val gson = Gson()
            return gson.fromJson(preferences.getString(key, ""), classType)
        }
        return null
    }*/

    fun remove(key: String) {
        editor.remove(key).commit()
    }

    fun clearAll() {
        editor.clear().commit()
    }
}