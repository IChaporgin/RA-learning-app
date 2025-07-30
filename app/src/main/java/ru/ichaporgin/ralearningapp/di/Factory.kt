package ru.ichaporgin.ralearningapp.di

interface Factory<T> {
    fun create(): T
}