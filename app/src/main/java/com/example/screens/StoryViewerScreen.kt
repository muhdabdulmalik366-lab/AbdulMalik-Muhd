package com.example.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Story
import com.example.ui.theme.HeartRed
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@Composable
fun StoryViewerScreen(
    stories: List<Story>,
    initialIndex: Int = 0,
    onClose: () -> Unit,
    onStoryViewed: (storyId: String) -> Unit,
    onSendReaction: (storyId: String, reaction: String) -> Unit
) {
    if (stories.isEmpty()) {
        onClose()
        return
    }

    var currentIndex by remember { mutableIntStateOf(initialIndex.coerceIn(0, stories.size - 1)) }
    val currentStory = stories[currentIndex]
    val progress = remember(currentIndex) { Animatable(0f) }
    var replyText by remember { mutableStateOf("") }
    var feedbackReaction by remember { mutableStateOf<String?>(null) }

    // Auto-advance timer (5 seconds per story)
    LaunchedEffect(currentIndex) {
        onStoryViewed(currentStory.id)
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
        )
        if (currentIndex < stories.size - 1) {
            currentIndex++
        } else {
            onClose()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Story image background
        Image(
            painter = painterResource(id = currentStory.mediaRes),
            contentDescription = "Story Media",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay at top and bottom for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Touch zones for tap left/right to navigate
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (currentIndex > 0) {
                                currentIndex--
                            } else {
                                onClose()
                            }
                        }
                    }
            )
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (currentIndex < stories.size - 1) {
                                currentIndex++
                            } else {
                                onClose()
                            }
                        }
                    }
            )
        }

        // Top controls: Progress indicators & Author info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Segmented progress bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stories.forEachIndexed { index, _ ->
                    val segmentProgress = when {
                        index < currentIndex -> 1f
                        index == currentIndex -> progress.value
                        else -> 0f
                    }
                    LinearProgressIndicator(
                        progress = { segmentProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Author row & Close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = currentStory.user.avatarRes),
                        contentDescription = currentStory.user.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = currentStory.user.username,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${currentStory.timestamp} • 24h expires",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_story_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Story",
                        tint = Color.White
                    )
                }
            }
        }

        // Center Caption if present
        currentStory.caption?.let { caption ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = caption,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // Reaction animation indicator
        feedbackReaction?.let { emoji ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 48.sp)
            }
        }

        // Bottom Reply & Emoji Reaction Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Quick emojis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("🔥", "❤️", "😂", "👏", "⚡", "😍").forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                feedbackReaction = emoji
                                onSendReaction(currentStory.id, emoji)
                            }
                            .padding(4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    placeholder = { Text("Send message to ${currentStory.user.username}...", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("story_reply_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = VibraViolet,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                        focusedContainerColor = Color.Black.copy(alpha = 0.4f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.4f)
                    ),
                    singleLine = true,
                    trailingIcon = {
                        if (replyText.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    onSendReaction(currentStory.id, replyText)
                                    replyText = ""
                                    feedbackReaction = "✨"
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send Reply",
                                    tint = VibraViolet
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        feedbackReaction = "❤️"
                        onSendReaction(currentStory.id, "❤️")
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like Story",
                        tint = HeartRed
                    )
                }
            }
        }
    }
}
