package com.transcendensoft.hedbanz.utils.extension

import android.text.SpannableString

/**
 * Copyright 2017. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * Kotlin extensions for spannable strings
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
fun SpannableString.spanWith(target: String, apply: SpannableBuilder.() -> Unit) {
    val builder = SpannableBuilder()
    apply(builder)

    val start = this.indexOf(target)
    val end =  start + target.length

    setSpan(builder.what, start, end, builder.flags)
}

class SpannableBuilder {
    lateinit var what: Any
    var flags: Int = 0
}