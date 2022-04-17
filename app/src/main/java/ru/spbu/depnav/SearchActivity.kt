package ru.spbu.depnav

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.intl.Locale
import androidx.core.view.WindowInsetsControllerCompat
import ru.spbu.depnav.db.AppDatabase
import ru.spbu.depnav.model.MarkerText.LanguageId
import ru.spbu.depnav.ui.search.MarkerSearch
import ru.spbu.depnav.ui.search.MarkerSearchState
import ru.spbu.depnav.ui.theme.DepNavTheme

private const val TAG = "SearchActivity"

const val EXTRA_MARKER_ID = "ru.spbu.depnav.MARKER_ID"

class SearchActivity : ComponentActivity() {
    private val mMarkerSearchState: MarkerSearchState by viewModels()
    private lateinit var mAppDatabase: AppDatabase

    private val systemLanguage: LanguageId
        get() {
            val language = Locale.current.language

            Log.d(TAG, "Current system language is $language")

            return when (language) {
                "en" -> LanguageId.EN
                "ru" -> LanguageId.RU
                else -> LanguageId.EN
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAppDatabase = AppDatabase.getInstance(this)

        setContent {
            val searchMatches by mMarkerSearchState.matchedMarkers.collectAsState(emptyList()) // TODO: make safer

            window.statusBarColor = MaterialTheme.colors.background.toArgb()
            window.navigationBarColor = MaterialTheme.colors.surface.toArgb()

            if (!isSystemInDarkTheme()) {
                WindowInsetsControllerCompat(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }

            DepNavTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MarkerSearch(
                        matches = searchMatches,
                        onSearch = this::onSearch,
                        onClear = this::onClear,
                        onResultClick = this::onMarkerSelected
                    )
                }
            }
        }
    }

    private fun onSearch(text: String) {
        mMarkerSearchState.search(text, mAppDatabase.markerTextDao(), systemLanguage)
    }

    private fun onClear() {
        mMarkerSearchState.clear()
    }

    private fun onMarkerSelected(id: Int) {
        Log.i(TAG, "Marker $id has been selected")

        val intent = Intent(this, MainActivity::class.java).putExtra(EXTRA_MARKER_ID, id)
        setResult(Activity.RESULT_OK, intent)

        finish()
    }
}
