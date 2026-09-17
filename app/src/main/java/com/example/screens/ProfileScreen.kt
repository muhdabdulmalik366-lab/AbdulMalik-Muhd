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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.Post
import com.example.data.User
import com.example.ui.theme.VibraBrandGradient
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@Composable
fun ProfileScreen(
    user: User,
    userPosts: List<Post>,
    savedPosts: List<Post>,
    onSelectPost: (Post) -> Unit,
    onUpdateProfile: (displayName: String, bio: String, website: String) -> Unit,
    onOpenSafetySettings: () -> Unit,
    onShareProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val tabs = listOf("Posts", "Saved", "Tagged")

    val displayedPosts = when (selectedTabIndex) {
        0 -> userPosts
        1 -> savedPosts
        else -> userPosts.take(1) // Sample tagged
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Profile Header spanning full width
        item(span = { GridItemSpan(3) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Top user info and counters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Glowing Avatar
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(VibraBrandGradient)
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = user.avatarRes),
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    // Counters
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem(count = "${userPosts.size}", label = "Posts")
                        ProfileStatItem(count = "${user.followersCount}", label = "Followers")
                        ProfileStatItem(count = "${user.followingCount}", label = "Following")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Name, Handle, Verified, Bio, Link
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (user.isVerified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = VibraCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = VibraViolet,
                    fontWeight = FontWeight.Medium
                )

                if (user.bio.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (user.website.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🔗 ${user.website}",
                        style = MaterialTheme.typography.labelMedium,
                        color = VibraCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action buttons: Edit Profile, Share Profile, Safety
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showEditProfileDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibraViolet),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("edit_profile_button")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = onShareProfile,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Profile")
                    }

                    IconButton(
                        onClick = onOpenSafetySettings,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("profile_safety_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Safety and Privacy",
                            tint = VibraMagenta
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Row for Grid, Saved, Tagged
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = VibraViolet,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = VibraViolet
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        val icon = when (index) {
                            0 -> Icons.Default.GridOn
                            1 -> Icons.Default.Bookmark
                            else -> Icons.Default.PersonPin
                        }
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp)) },
                            selectedContentColor = VibraViolet,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Empty state or Grid items
        if (displayedPosts.isEmpty()) {
            item(span = { GridItemSpan(3) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTabIndex == 1) "No saved posts yet." else "No posts to display.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(displayedPosts, key = { it.id }) { post ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onSelectPost(post) }
                ) {
                    Image(
                        painter = painterResource(id = post.mediaRes),
                        contentDescription = "User Post",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            user = user,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, bio, site ->
                onUpdateProfile(name, bio, site)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EditProfileDialog(
    user: User,
    onDismiss: () -> Unit,
    onSave: (displayName: String, bio: String, website: String) -> Unit
) {
    var name by remember { mutableStateOf(user.displayName) }
    var bio by remember { mutableStateOf(user.bio) }
    var website by remember { mutableStateOf(user.website) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Profile", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    label = { Text("Website") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, bio, website) },
                colors = ButtonDefaults.buttonColors(containerColor = VibraViolet)
            ) {
                Text("Save Changes", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
