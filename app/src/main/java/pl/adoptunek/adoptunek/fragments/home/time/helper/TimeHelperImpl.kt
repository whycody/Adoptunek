package pl.adoptunek.adoptunek.fragments.home.time.helper

import java.util.*

class TimeHelperImpl: TimeHelper {
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    override fun howLongAgo(date: Date, type: Int): String {
        val diff = getDiff(date)
        val days = diff/DAY_MILLIS
        val howLongAgo: String
        if(diff<SECOND_MILLIS*5) howLongAgo = "${diff/SECOND_MILLIS} sekundy"
        else if(diff<MINUTE_MILLIS) howLongAgo = "${diff/SECOND_MILLIS} sekund"
        else if(diff<2*MINUTE_MILLIS) return "minutę"
        else if(diff<(5*MINUTE_MILLIS)||((diff<(60*MINUTE_MILLIS)&&remainderFromDivision(diff/MINUTE_MILLIS))))
            howLongAgo = ("${diff/MINUTE_MILLIS} minuty")
        else if(diff<(60*MINUTE_MILLIS)) howLongAgo = "${diff/MINUTE_MILLIS} minut"
        else if(diff<(90*MINUTE_MILLIS)) howLongAgo = "godzinę"
        else if(diff<5*HOUR_MILLIS||((diff<24*HOUR_MILLIS)&&remainderFromDivision(diff/HOUR_MILLIS)))
            howLongAgo = ("${diff/HOUR_MILLIS} godziny")
        else if(diff<24*HOUR_MILLIS) howLongAgo = "${diff/HOUR_MILLIS} godzin"
        else if(diff<48*HOUR_MILLIS) howLongAgo = "wczoraj"
        else if(days<30) howLongAgo = "$days dni"
        else if(days<60) howLongAgo = "miesiąc"
        else if(days<120) howLongAgo = "${days/30} miesiące"
        else if(days<364) howLongAgo = "${days/30} miesięcy"
        else if(days<420) howLongAgo = "rok"
        else if(days<364*2) howLongAgo = "ponad rok"
        else if(days<364*5) howLongAgo = "${days/364} lata"
        else howLongAgo = "${days/364} lat"

        return if(howLongAgo != "wczoraj" && type == POST_HOW_LONG_AGO) "$howLongAgo temu"
        else howLongAgo
    }

    private fun remainderFromDivision(number: Long) = number%10==2L||number%10==3L||number%10==4L

    private fun getDiff(date: Date): Long{
        val time = date.time
        val now = System.currentTimeMillis()
        val diff = now - time
        return diff
    }

    companion object{
        val POST_HOW_LONG_AGO = 0
        val PET_HOW_LONG_IS_WAITING = 1
    }
}