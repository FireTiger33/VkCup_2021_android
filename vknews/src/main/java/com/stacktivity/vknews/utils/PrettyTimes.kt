package com.stacktivity.vknews.utils

import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

fun PrettyTime.format(unixTime: Long): String {
    return format(Date(unixTime * 1000))
}

fun PrettyTime.format(unixTime: Int): String {
    return format(unixTime.toLong())
}