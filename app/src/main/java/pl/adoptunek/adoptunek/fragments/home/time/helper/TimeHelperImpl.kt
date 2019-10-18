package pl.adoptunek.adoptunek.fragments.home.time.helper

import java.util.*

class TimeHelperImpl: TimeHelper {
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    override fun howLongAgo(date: Date): String {
        val diff = getDiff(date)
        if(diff<SECOND_MILLIS*5) return ("${diff/SECOND_MILLIS} sekundy temu")
        else if(diff<MINUTE_MILLIS) return ("${diff/SECOND_MILLIS} sekund temu")
        else if(diff<2*MINUTE_MILLIS) return ("minutę temu")
        else if(diff<(5*MINUTE_MILLIS)||((diff<(60*MINUTE_MILLIS)&&remainderFromDivision(diff/MINUTE_MILLIS))))
            return ("${diff/MINUTE_MILLIS} minuty temu")
        else if(diff<(60*MINUTE_MILLIS)) return ("${diff/MINUTE_MILLIS} minut temu")
        else if(diff<(90*MINUTE_MILLIS)) return ("godzinę temu")
        else if(diff<5*HOUR_MILLIS||((diff<24*HOUR_MILLIS)&&remainderFromDivision(diff/HOUR_MILLIS)))
            return ("${diff/HOUR_MILLIS} godziny temu")
        else if(diff<24*HOUR_MILLIS) return ("${diff/HOUR_MILLIS} godzin temu")
        else if(diff<48*HOUR_MILLIS) return ("wczoraj")
        else return("${diff/DAY_MILLIS} dni temu")
    }

    private fun remainderFromDivision(number: Long) = number%10==2L||number%10==3L||number%10==4L

    override fun howLongIsWaiting(date: Date): String {
        val diff = getDiff(date)
        val days = diff/DAY_MILLIS
        if(diff<DAY_MILLIS*2) return ("dzień")
        else if(days<30) return ("${diff/DAY_MILLIS} dni")
        else if(days<40) return ("miesiąc")
        else if(days<60) return ("ponad miesiąc")
        else if(days<364) return ("${days/30} miesięcy")
        else if(days<420) return ("rok")
        else if(days<364*2) return ("ponad rok")
        else if(days<364*5) return ("${days/364} lata")
        else return ("${days/364} lat")
    }

    private fun getDiff(date: Date): Long{
        val time = date.time
        val now = System.currentTimeMillis()
        val diff = now - time
        return diff
    }
}