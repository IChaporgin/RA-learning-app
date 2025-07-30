package ru.ichaporgin.ralearningapp

import android.app.Application
import ru.ichaporgin.ralearningapp.di.AppContainer

class RecipesApplication: Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}