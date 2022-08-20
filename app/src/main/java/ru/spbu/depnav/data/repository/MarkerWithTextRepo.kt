package ru.spbu.depnav.data.repository

import android.util.Log
import ru.spbu.depnav.data.db.MarkerWithTextDao
import ru.spbu.depnav.data.model.Marker
import ru.spbu.depnav.data.model.MarkerText
import javax.inject.Inject

private const val TAG = "MarkerWithTextRepo"

/** Repository for loading and saving [Marker] objects with associated [MarkerText] objects. */
class MarkerWithTextRepo @Inject constructor(private val dao: MarkerWithTextDao) {
    /** Saves the provided objects. */
    suspend fun insertAll(markersWithText: Map<Marker, MarkerText>) {
        dao.insertMarkers(markersWithText.keys)
        dao.insertMarkerTexts(markersWithText.values)
    }

    /** Loads a [Marker] by its ID and its corresponding [MarkerText] on the current language. */
    suspend fun loadById(id: Int): Pair<Marker, MarkerText> {
        val language = MarkerText.LanguageId.getCurrent()
        val (marker, markerTexts) = dao.loadById(id, language).entries.firstOrNull()
            ?: throw IllegalArgumentException("No markers with ID $id")
        val markerText = markerTexts.squeezedFor(marker, language)
        return marker to markerText
    }

    /**
     * Loads all [Markers][Marker] on the specified floor with their corresponding [MarkerText] on
     * the current language.
     */
    suspend fun loadByFloor(floor: Int): Map<Marker, MarkerText> {
        val language = MarkerText.LanguageId.getCurrent()
        val markersWithTexts = dao.loadByFloor(floor, language)
        return markersWithTexts.entries.associate { (marker, markerTexts) ->
            val markerText = markerTexts.squeezedFor(marker, language)
            marker to markerText
        }
    }

    private fun List<MarkerText>.squeezedFor(marker: Marker, language: MarkerText.LanguageId) =
        firstOrNull() ?: run {
            Log.w(TAG, "Marker $marker has no text on $language")
            MarkerText(marker.id, language, null, null)
        }

    /**
     * Loads a [Marker] and its corresponding [MarkerText] on the current language so that the text
     * has the specified tokens in it.
     */
    suspend fun loadByTokens(tokens: String): Map<Marker, MarkerText> {
        val language = MarkerText.LanguageId.getCurrent()
        val textsWithMarkers = dao.loadByTokens(tokens, language)
        return textsWithMarkers.entries.associate { (markerText, markers) ->
            val marker = markers.firstOrNull()
            checkNotNull(marker) { "MarkerText $markerText has no associated marker" }
            marker to markerText
        }
    }
}
