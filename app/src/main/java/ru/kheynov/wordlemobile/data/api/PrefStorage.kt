package ru.kheynov.wordlemobile.data.api

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kheynov.wordlemobile.presentation.util.Language
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_NAME = "PREFERENCES_STORAGE"

@Singleton
class PrefStorage @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var language: String? = null
        set(value) {
            if (field != value) {
                field = value
                saveToPreferences(value ?: Language.Russian.text)
            }
        }
        get() = prefs.getString("language", "")

    private fun saveToPreferences(value: String) {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("language", value)
        editor.apply()
    }
}
