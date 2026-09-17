package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.VibraBrandGradient
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@Composable
fun CreatePostScreen(
    onPublishPost: (caption: String, hashtags: List<String>, location: String?, allowComments: Boolean, mediaRes: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Available visual media to pick from
    val mediaOptions = listOf(
        R.drawable.vibra_post_cyberpunk to "Cyber City",
        R.drawable.vibra_post_fashion to "Neo Fashion",
        R.drawable.vibra_post_sunset to "Pastel Dusk",
        R.drawable.vibra_logo to "Vibra Wave"
    )

    var selectedMediaIndex by remember { mutableIntStateOf(0) }
    var caption by remember { mutableStateOf("") }
    var hashtagInput by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("Shibuya, Tokyo") }
    var allowComments by remember { mutableStateOf(true) }
    var allowAudioReuse by remember { mutableStateOf(true) }

    val popularHashtags = listOf("vibra", "neonwaves", "tokyonights", "cyberpunk", "futurefashion", "sunsetvibes")
    val locationOptions = listOf("Shibuya, Tokyo", "SoHo, New York", "Kreuzberg, Berlin", "Gangnam, Seoul", "Metaverse Studio")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(bottom = 90.dp)
    ) {
        Text(
            text = "Create New Post",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)
        )

        // Selected Media Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = mediaOptions[selectedMediaIndex].first),
                    contentDescription = "Media Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay pill for selected preset
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = mediaOptions[selectedMediaIndex].second,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Choose visual media source
        Text(
            text = "Select Media Asset",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(mediaOptions.indices.toList()) { index ->
                val (resId, label) = mediaOptions[index]
                val isSelected = index == selectedMediaIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedMediaIndex = index }
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (isSelected) Modifier.border(3.dp, VibraViolet, RoundedCornerShape(14.dp))
                                else Modifier
                            )
                    ) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (isSelected) VibraViolet else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caption TextField
        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            placeholder = { Text("Write a vibrant caption... Share your thoughts and vibe ✨") },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .testTag("create_caption_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VibraViolet,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Emoji quick insertion
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("⚡", "🔥", "💜", "✨", "🌆", "🎧", "🌴").forEach { emoji ->
                Text(
                    text = emoji,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { caption += " $emoji" }
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Hashtags suggestions
        Text(
            text = "Suggested Hashtags",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(popularHashtags) { tag ->
                val isAdded = hashtagInput.contains(tag)
                FilterChip(
                    selected = isAdded,
                    onClick = {
                        hashtagInput = if (isAdded) {
                            hashtagInput.replace("#$tag", "").trim()
                        } else {
                            if (hashtagInput.isBlank()) "#$tag" else "$hashtagInput #$tag"
                        }
                    },
                    label = { Text("#$tag") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibraMagenta,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Location Selector
        Text(
            text = "Add Location",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(locationOptions) { loc ->
                val isSelected = selectedLocation == loc
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedLocation = if (isSelected) "" else loc },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text(loc) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibraCyan,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Advanced Options / Toggles
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Allow Comments", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text("Let other creators comment on this post", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = allowComments,
                        onCheckedChange = { allowComments = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = VibraViolet, checkedTrackColor = VibraViolet.copy(alpha = 0.5f))
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("High Fidelity Audio", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text("Attach original high resolution audio wave", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = allowAudioReuse,
                        onCheckedChange = { allowAudioReuse = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = VibraCyan, checkedTrackColor = VibraCyan.copy(alpha = 0.5f))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Publish CTA Button
        Button(
            onClick = {
                val tags = hashtagInput
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .map { it.removePrefix("#") }
                val chosenMedia = mediaOptions[selectedMediaIndex].first
                onPublishPost(
                    caption.ifBlank { "Just sharing good vibes on VIBRA ✨" },
                    tags,
                    selectedLocation.takeIf { it.isNotBlank() },
                    allowComments,
                    chosenMedia
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("publish_post_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VibraViolet)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "Publish to VIBRA",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
