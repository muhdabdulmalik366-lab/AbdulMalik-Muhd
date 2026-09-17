package com.example.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Post
import com.example.ui.theme.HeartRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostOptionsSheet(
    post: Post,
    onDismiss: () -> Unit,
    onReport: () -> Unit,
    onMuteUser: (userId: String) -> Unit,
    onBlockUser: (userId: String) -> Unit,
    onShare: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Post by @${post.author.username}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            SheetActionItem(
                icon = Icons.Default.Share,
                title = "Share to External...",
                subtitle = "Send link to other apps",
                tint = MaterialTheme.colorScheme.primary,
                onClick = {
                    onDismiss()
                    onShare()
                }
            )

            SheetActionItem(
                icon = Icons.Default.ContentCopy,
                title = "Copy Post Link",
                subtitle = "https://vibra.social/p/${post.id}",
                tint = MaterialTheme.colorScheme.onSurface,
                onClick = onDismiss
            )

            SheetActionItem(
                icon = Icons.AutoMirrored.Filled.VolumeMute,
                title = "Mute @${post.author.username}",
                subtitle = "Hide posts from this author in your feed",
                tint = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    onMuteUser(post.author.id)
                    onDismiss()
                }
            )

            SheetActionItem(
                icon = Icons.Default.Block,
                title = "Block @${post.author.username}",
                subtitle = "They won't be able to view your profile or message you",
                tint = HeartRed,
                onClick = {
                    onBlockUser(post.author.id)
                    onDismiss()
                }
            )

            SheetActionItem(
                icon = Icons.Default.Flag,
                title = "Report Post",
                subtitle = "I'm concerned about this post",
                tint = HeartRed,
                onClick = {
                    onDismiss()
                    onReport()
                }
            )
        }
    }
}

@Composable
private fun SheetActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = if (tint == HeartRed) HeartRed else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
