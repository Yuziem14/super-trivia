package com.yuziem14.supertrivia.parsers

import android.os.Build
import android.text.Html

const val MINIMUM_SDK_VERSION = 24

/**
 * Reference: https://stackoverflow.com/questions/2918920/decode-html-entities-in-android
 */
class HTMLParser {
    companion object {
        fun parse(html: String = ""): String {
            if (Build.VERSION.SDK_INT >= MINIMUM_SDK_VERSION)
            {
                return Html.fromHtml(html , Html.FROM_HTML_MODE_LEGACY).toString()
            }

            return Html.fromHtml(html).toString()
        }
    }
}