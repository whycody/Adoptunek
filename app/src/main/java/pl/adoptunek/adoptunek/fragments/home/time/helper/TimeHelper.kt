package pl.adoptunek.adoptunek.fragments.home.time.helper

import java.util.*

interface TimeHelper {

    fun howLongAgo(date: Date): String

    fun howLongIsWaiting(date: Date): String
}