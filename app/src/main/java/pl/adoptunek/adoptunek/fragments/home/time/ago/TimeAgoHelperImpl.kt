package pl.adoptunek.adoptunek.fragments.home.time.ago

import java.util.*

class TimeAgoHelperImpl: TimeAgoHelper {

    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    override fun howLongAgo(date: Date): String {
        val time = date.time
        val now = System.currentTimeMillis()
        val diff = now - time
        return howLongIsIt(diff)
    }

    private fun howLongIsIt(diff: Long): String{
        if(diff<MINUTE_MILLIS) return ("${diff/1000} sekund temu")
        else if(diff<2*MINUTE_MILLIS) return ("minutę temu")
        else if(diff<(5*MINUTE_MILLIS)) return ("${diff/MINUTE_MILLIS} minuty temu")
        else if(diff<(60*MINUTE_MILLIS)) return ("${diff/MINUTE_MILLIS} minut temu")
        else if(diff<(90*HOUR_MILLIS)) return ("godzinę temu")
        else if(diff<5*HOUR_MILLIS) return ("${diff/HOUR_MILLIS} godziny temu")
        else if(diff<24*HOUR_MILLIS) return ("${diff/HOUR_MILLIS} godzin temu")
        else if(diff<48*HOUR_MILLIS) return ("wczoraj")
        else return("${diff/DAY_MILLIS} dni temu")
    }
}