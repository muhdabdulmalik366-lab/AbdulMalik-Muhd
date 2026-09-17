package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NotificationItem
import com.example.data.NotificationType
import com.example.ui.theme.HeartRed
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onMarkAllRead: () -> Unit,
    onFollowToggle: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Likes", "Comments", "Followers", "Mentions")

    val filteredNotifications = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "Likes" -> notifications.filter { it.type == NotificationType.LIKE }
            "Comments" -> notifications.filter { it.type == NotificationType.COMMENT }
            "Followers" -> notifications.filter { it.type == NotificationType.FOLLOW }
            "Mentions" -> notifications.filter { it.type == NotificationType.MENTION }
            else -> notifications
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header with Mark all read
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(
                onClick = onMarkAllRead,
                modifier = Modifier.testTag("mark_all_read_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = VibraViolet
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mark read", color = VibraViolet, fontSize = 13.sp)
            }
        }

        // Filter Pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = (selectedFilter == filter),
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibraViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Notifications List
        LazyColumn(
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredNotifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No notifications right now.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredNotifications, key = { it.id }) { item ->
                    NotificationCard(
                        notification = item,
                        onFollowToggle = { onFollowToggle(item.user.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onFollowToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("notification_card_${notification.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with type icon badge
                Box(modifier = Modifier.size(46.dp)) {
                    Image(
                        painter = painterResource(id = notification.user.avatarRes),
                        contentDescription = notification.user.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )

                    // Type badge
                    val (badgeIcon, badgeColor) = when (notification.type) {
                        NotificationType.LIKE -> Icons.Default.Favorite to HeartRed
                        NotificationType.COMMENT -> Icons.Default.ModeComment to VibraViolet
                        NotificationType.FOLLOW -> Icons.Default.PersonAdd to VibraCyan
                        NotificationType.MENTION -> Icons.Default.AlternateEmail to VibraMagenta
                        NotificationType.STORY -> Icons.Default.Whatshot to Color(0xFFF97316)
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = badgeIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text message
                Column {
                    val annotatedText = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(notification.user.username)
                        }
                        append(" ${notification.message}")
                    }
                    Text(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = notification.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Right side action or thumbnail
            if (notification.type == NotificationType.FOLLOW) {
                if (notification.user.isFollowing) {
                    OutlinedButton(
                        onClick = onFollowToggle,
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Following", fontSize = 11.sp)
                    }
                } else {
                    Button(
                        onClick = onFollowToggle,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibraViolet),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Follow back", fontSize = 11.sp, color = Color.White)
                    }
                }
            } else if (notification.postPreviewRes != null) {
                Image(
                    painter = painterResource(id = notification.postPreviewRes),
                    contentDescription = "Post Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}
