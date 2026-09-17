package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VibraBrandGradient
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@Composable
fun VibraTopBar(
    isDarkMode: Boolean,
    unreadMessagesCount: Int,
    onToggleDarkMode: () -> Unit,
    onOpenMessages: () -> Unit,
    onOpenSafety: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo & Brand Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("vibra_brand_header")
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VibraBrandGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "V",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "VIBRA",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Actions: Dark/Light Mode, Safety Shortcut, Direct Messages
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Theme toggle
            IconButton(
                onClick = onToggleDarkMode,
                modifier = Modifier.testTag("theme_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = if (isDarkMode) VibraCyan else VibraViolet
                )
            }

            // Safety shortcut
            IconButton(
                onClick = onOpenSafety,
                modifier = Modifier.testTag("safety_shortcut_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Safety and Privacy",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // DM Messaging with badge
            IconButton(
                onClick = onOpenMessages,
                modifier = Modifier.testTag("direct_messages_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadMessagesCount > 0) {
                            Badge(
                                containerColor = VibraMagenta,
                                contentColor = Color.White
                            ) {
                                Text("$unreadMessagesCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Direct Messages",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
