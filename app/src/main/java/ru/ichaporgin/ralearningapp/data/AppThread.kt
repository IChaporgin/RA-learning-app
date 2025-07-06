package ru.ichaporgin.ralearningapp.data

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppThread {
    val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
}