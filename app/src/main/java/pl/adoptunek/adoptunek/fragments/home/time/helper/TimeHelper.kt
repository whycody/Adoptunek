package pl.adoptunek.adoptunek.fragments.home.time.helper

import java.util.*

interface TimeHelper {

    fun howLongAgo(date: Date, type: Int): String
}