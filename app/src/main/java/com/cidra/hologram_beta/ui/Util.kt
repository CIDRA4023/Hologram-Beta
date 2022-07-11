package com.cidra.hologram_beta.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.domain.model.LiveItem
import com.cidra.hologramjetpackcompose.Common.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


fun liveViewersFormatter(viewers: String): String {
    val defaultLanguage = Locale.getDefault().language
    if (viewers.toIntOrNull() == null) return "プレミアム公開"

    return if (defaultLanguage != Locale.JAPANESE.language) {
        when (viewers.length) {
            0, 1, 2, 3 -> viewers.plus("watching")
            4, 5, 6 -> (ceil(viewers.toDouble() / 1000)).toInt().toString().plus("K watching")
            else -> "Premieres"
        }
    } else {
        when (viewers.length) {
            0, 1, 2, 3, 4 -> viewers.plus("人視聴中")
            5, 6 -> (ceil(viewers.toDouble() / 1000) / 10).toString().plus("万人視聴中")
            // プレミアム公開だった時
            else -> ""
        }
    }
}

fun archiveViewersFormatter(viewers: String): String {
    val defaultLanguage = Locale.getDefault().language
    if (viewers.toIntOrNull() == null) return ""

    return if (defaultLanguage != Locale.JAPANESE.language) {
        when (viewers.length) {
            0, 1, 2, 3 -> viewers.plus("views")
            4, 5, 6 -> (ceil(viewers.toDouble() / 1000)).toInt().toString().plus("K views")
            else -> "Premieres"
        }
    } else {
        when (viewers.length) {
            0, 1, 2, 3, 4 -> viewers.plus("回再生")
            5, 6 -> (ceil(viewers.toDouble() / 1000) / 10).toString().plus("万回再生")
            // プレミアム公開だった時
            else -> ""
        }
    }
}


fun durationFormatter(duration: String?): String? {
    // 再生時間のパターン
    val sdfDurationSS = SimpleDateFormat("'PT'ss'S'", Locale.getDefault())
    val sdfDurationMM = SimpleDateFormat("'PT'mm'M'", Locale.getDefault())
    val sdfDurationMMSS = SimpleDateFormat("'PT'mm'M'ss'S'", Locale.getDefault())
    val sdfDurationHH = SimpleDateFormat("'PT'HH'H'", Locale.getDefault())
    val sdfDurationHHSS = SimpleDateFormat("'PT'HH'H'ss'S'", Locale.getDefault())
    val sdfDurationHHMM = SimpleDateFormat("'PT'HH'H'mm'M'", Locale.getDefault())
    val sdfDurationHHMMSS = SimpleDateFormat("'PT'HH'H'mm'M'ss'S'", Locale.getDefault())

    val setTextMMSS = SimpleDateFormat("mm:ss", Locale.getDefault())
    val setTextHHMMSS = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    val formattedDuration = duration?.let {
        val isMatchedH = Regex("H").containsMatchIn(it)
        val isMatchedM = Regex("M").containsMatchIn(it)
        val isMatchedS = Regex("S").containsMatchIn(it)

        when {
            // 1時間以上の再生時間があるとき
            isMatchedH -> {
                return@let if (isMatchedM && isMatchedS) {
                    val durationObject = sdfDurationHHMMSS.parse(it)
                    setTextHHMMSS.format(durationObject!!)
                } else if (isMatchedH && isMatchedM) {
                    val durationObject = sdfDurationHHMM.parse(it)
                    setTextHHMMSS.format(durationObject!!)
                } else if (isMatchedH && isMatchedS) {
                    val durationObject = sdfDurationHHSS.parse(it)
                    setTextHHMMSS.format(durationObject!!)
                } else {
                    val durationObject = sdfDurationHH.parse(it)
                    setTextHHMMSS.format(durationObject!!)
                }
            }
            // 1時間未満で1分以上再生時間があるとき
            isMatchedM -> {
                Log.i("duration", "MS")
                return@let if (isMatchedM && isMatchedS) {
                    val durationObject = sdfDurationMMSS.parse(it)
                    setTextMMSS.format(durationObject!!)
                } else {
                    val durationObject = sdfDurationMM.parse(it)
                    setTextMMSS.format(durationObject!!)
                }
            }
            // 1分未満で1秒以上再生時間があるとき
            isMatchedS -> {
                Log.i("duration", "S")
                val durationObject = sdfDurationSS.parse(it)
                return@let setTextMMSS.format(durationObject!!)
            }

            else -> {
                return@let ""
            }
        }

    }

    return formattedDuration
}


fun timeNotationFormatter(item: String, settingValue: Int): String {

    val lang = Locale.getDefault().language

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sdf12 = SimpleDateFormat("hh:mm \n  a", Locale.getDefault())

    val dateObject = sdf.parse(item)


    return if (settingValue == R.string.setting_time_notation_12hour) {
        sdf12.format(dateObject!!)
    } else {
        sdf24.format(dateObject!!)
    }


}

fun dateFormatter(date: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sdf12 = SimpleDateFormat("hh:mm \n  a", Locale.getDefault())

    val dateObject = sdf.parse(date)
    return sdf24.format(dateObject!!)
}

fun dateTimeToLong(publishedAt: String): Long {
    //  ISO 8601（YYYY-MM-DDThh:mm:ss.sZ）形式でパース
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    return sdf.parse(publishedAt)?.time ?: 0
}

/**
 * 時刻切り捨て
 */
fun truncate(datetime: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = datetime

    return GregorianCalendar(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DATE)
    ).time
}

/**
 * 日付フォーマット
 * yyyy-MM-dd
 */
fun sdf(date: String): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(date)
}

/**
 * 昨日の日付
 */
fun yesterday(): Date {
    val today = Date()
    val cal = Calendar.getInstance()
    cal.time = today
    cal.add(Calendar.DAY_OF_MONTH, -1)
    return cal.time
}

fun dateAgo(amount: Int): Date {
    val today = Date()
    val cal = Calendar.getInstance()
    cal.time = today
    cal.add(Calendar.DAY_OF_MONTH, amount)
    Log.i("date", cal.time.toString())
    return cal.time
}

/**
 * 明日の日付
 */
fun tomorrow(): Date {
    val today = Date()
    val cal = Calendar.getInstance()
    cal.time = today
    cal.add(Calendar.DAY_OF_MONTH, 1)
    return cal.time
}

/**
 * 相対時間
 */
fun relativeTimeFormatter(context: Context, current: Long, previous: Long): String {
    val msPerMinute = 1.minutes.inWholeMilliseconds
    val msPerHour = 1.hours.inWholeMilliseconds
    val msPerDay = 1.days.inWholeMilliseconds
    val msPerMonth = msPerDay * 30

    val elapsed = current - previous

    val res = context.resources

    return when {
        elapsed < msPerMinute -> {
            "${(elapsed / 1000)}" + res.getString(R.string.seconds_ago_suffix)
        }
        elapsed < msPerHour -> {
            "${(elapsed / msPerMinute)}" + res.getString(R.string.minutes_ago_suffix)
        }
        elapsed < msPerDay -> {
            "${(elapsed / msPerHour)}" + res.getString(R.string.hours_ago_suffix)
        }
        elapsed < msPerMonth -> {
            "${(elapsed / msPerDay)}" + res.getString(R.string.days_ago_suffix)
        }
        else -> {
            ""
        }
    }
}

fun openApp(videoId: String, settingValue: Int, context: Context) {

    val intent = Intent(Intent.ACTION_VIEW)
    val url = Constants.YOUTUBE_WATCH_BASE_URL + videoId
    intent.data = Uri.parse(url)

    when (settingValue) {
        R.string.setting_open_app_youtube -> {
            context.startActivity(intent)
        }
        R.string.setting_open_app_web_view -> {
//            WebViewScreens(url = url)
        }
        R.string.setting_open_app_browser -> {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://"))
            val defaultResInfo =
                context.packageManager.resolveActivity(
                    browserIntent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            if (defaultResInfo != null) {
                intent.setPackage(defaultResInfo.activityInfo.packageName)
                context.startActivity(intent)
            }
        }
    }
}

