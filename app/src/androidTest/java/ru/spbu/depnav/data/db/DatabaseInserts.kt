/**
 * DepNav -- department navigator.
 * Copyright (C) 2022  Timofey Pushkin
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

package ru.spbu.depnav.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.spbu.depnav.data.model.MapInfo
import ru.spbu.depnav.data.model.Marker
import ru.spbu.depnav.data.model.MarkerText

private fun MapInfo.values() =
    listOf(name, floorWidth, floorHeight, tileSize, levelsNum, floorsNum)

private fun Marker.values() = listOf(id, mapName, type.name, isClosed, floor, x, y)

private fun MarkerText.values() = listOf(markerId, languageId.ordinal, title, description)

private fun AppDatabase.insertAllValues(tableName: String, allValues: List<List<Any?>>) {
    val columnNames = query("SELECT * FROM $tableName", arrayOf()).use { it.columnNames }
    val db = openHelper.writableDatabase
    for (values in allValues) {
        if (columnNames.size != values.size) {
            throw IllegalArgumentException(
                "Expected ${columnNames.size} values, but was: ${values.size}"
            )
        }
        val content = ContentValues().apply {
            for ((col, v) in columnNames.zip(values)) {
                when (v) {
                    is Boolean -> put(col, v)
                    is Byte -> put(col, v)
                    is ByteArray -> put(col, v)
                    is Double -> put(col, v)
                    is Float -> put(col, v)
                    is Int -> put(col, v)
                    is Long -> put(col, v)
                    is Short -> put(col, v)
                    is String -> put(col, v)
                    null -> putNull(col)
                    else -> throw IllegalArgumentException(
                        "Value $v is of unsupported type ${v.javaClass}"
                    )
                }
            }
        }
        db.insert(tableName, SQLiteDatabase.CONFLICT_FAIL, content)
    }
}

@JvmName("insertAllMapInfos")
fun AppDatabase.insertAll(mapInfos: Iterable<MapInfo>) =
    insertAllValues("map_info", mapInfos.map { it.values() })

@JvmName("insertAllMarkers")
fun AppDatabase.insertAll(markers: Iterable<Marker>) =
    insertAllValues("marker", markers.map { it.values() })

@JvmName("insertAllMarkerTexts")
fun AppDatabase.insertAll(markerTexts: Iterable<MarkerText>) =
    insertAllValues("marker_text", markerTexts.map { it.values() })
