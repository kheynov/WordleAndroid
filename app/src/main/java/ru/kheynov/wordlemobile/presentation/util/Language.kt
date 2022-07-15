package ru.kheynov.wordlemobile.presentation.util

sealed class Language(val text: String, val layout: List<List<Key>>) {
    object Russian : Language("ru", KeyboardLayout.Russian)
    object English : Language("en", KeyboardLayout.English)
}