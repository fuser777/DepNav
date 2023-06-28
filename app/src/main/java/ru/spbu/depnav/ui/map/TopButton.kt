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

package ru.spbu.depnav.ui.map

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.spbu.depnav.R
import ru.spbu.depnav.ui.theme.DepNavTheme

/** Button with a search icon, text, and additional nested buttons. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopButton(
    text: String,
    onInfoClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSurfaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onSurfaceClick,
        modifier = modifier,
        shape = CircleShape
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box( // Aligns this Icon with the trailing IconButton
                modifier = Modifier.size(LocalViewConfiguration.current.minimumTouchTargetSize),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.label_search)
                )
            }

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    maxLines = 1,
                )
            }

            IconButton(onClick = onInfoClick) {
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = stringResource(R.string.label_open_map_info)
                )
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = stringResource(R.string.label_open_settings)
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun TopButtonPreview() {
    DepNavTheme {
        TopButton(
            text = "Search markers",
            onInfoClick = {},
            onSettingsClick = {},
            onSurfaceClick = {}
        )
    }
}
