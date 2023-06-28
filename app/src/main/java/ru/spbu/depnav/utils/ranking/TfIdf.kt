/**
 * DepNav -- department navigator.
 * Copyright (C) 2022  Timofei Pushkin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.spbu.depnav.utils.ranking

import kotlin.math.log10

/** tf-idf ranking algorithm. */
class TfIdf : Ranker {
    override fun rank(
        queryWordStats: Iterable<Ranker.QueryWordStat>,
        docWordNum: Int,
        avgWordNum: Double,
        docsNum: Int
    ): Double {
        var rank = 0.0
        for (queryWordStat in queryWordStats) {
            rank += tf(queryWordStat.appearanceNum, docWordNum) *
                idf(docsNum, queryWordStat.matchedDocsNum)
        }
        return rank
    }

    private fun tf(appearanceNum: Int, docWordNum: Int) = appearanceNum.toDouble() / docWordNum

    private fun idf(docsCount: Int, matchedDocsCount: Int) =
        log10((docsCount + 1.0) / (matchedDocsCount + 1))
}
