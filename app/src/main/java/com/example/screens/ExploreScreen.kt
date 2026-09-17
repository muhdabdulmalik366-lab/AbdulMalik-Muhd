package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExploreCategory
import com.example.data.Post
import com.example.data.User
import com.example.ui.theme.VibraBrandGradient
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@Composable
fun ExploreScreen(
    posts: List<Post>,
    categories: List<ExploreCategory>,
    suggestedCreators: List<User>,
    onSelectPost: (Post) -> Unit,
    onToggleFollow: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("All") } // "All", "Creators", "Hashtags", "Posts"
    val searchTabs = listOf("All", "Creators", "Hashtags", "Posts")

    // Filter results dynamically as user types
    val filteredPosts by remember(searchQuery, posts) {
        derivedStateOf {
            if (searchQuery.isBlank()) posts
            else posts.filter { post ->
                post.caption.contains(searchQuery, ignoreCase = true) ||
                    post.author.username.contains(searchQuery, ignoreCase = true) ||
                    post.hashtags.any { it.contains(searchQuery, ignoreCase = true) } ||
                    (post.location?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
    }

    val filteredCreators by remember(searchQuery, suggestedCreators) {
        derivedStateOf {
            if (searchQuery.isBlank()) suggestedCreators
            else suggestedCreators.filter { user ->
                user.username.contains(searchQuery, ignoreCase = true) ||
                    user.displayName.contains(searchQuery, ignoreCase = true) ||
                    user.bio.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search creators, tags, visuals...", fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = VibraViolet
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VibraViolet,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("explore_search_input")
        )

        // Filter tabs (All, Creators, Hashtags, Posts)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            items(searchTabs) { tab ->
                FilterChip(
                    selected = (selectedTab == tab),
                    onClick = { selectedTab = tab },
                    label = { Text(tab) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibraViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Main Explore Content
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(bottom = 90.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // If user is searching and tabs allow, display matching creators
            if ((selectedTab == "All" || selectedTab == "Creators") && (searchQuery.isNotEmpty() || suggestedCreators.isNotEmpty())) {
                item(span = { GridItemSpan(3) }) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = if (searchQuery.isNotBlank()) "Matching Creators" else "Suggested Creators",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredCreators, key = { it.id }) { creator ->
                                CreatorSuggestedCard(
                                    user = creator,
                                    onFollowToggle = { onToggleFollow(creator.id) }
                                )
                            }
                        }
                    }
                }
            }

            // Categories / Trending tags pills
            if (searchQuery.isBlank() && (selectedTab == "All" || selectedTab == "Hashtags")) {
                item(span = { GridItemSpan(3) }) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Trending Vibes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories) { cat ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    modifier = Modifier.clickable { searchQuery = cat.tag.removePrefix("#") }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tag,
                                            contentDescription = null,
                                            tint = VibraMagenta,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Column {
                                            Text(
                                                text = cat.name,
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "${cat.count} posts",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Grid header
            item(span = { GridItemSpan(3) }) {
                Text(
                    text = if (searchQuery.isNotBlank()) "Posts matching \"$searchQuery\"" else "Explore Feed",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
                )
            }

            // Photo / Video Grid items
            items(filteredPosts, key = { it.id }) { post ->
                ExploreGridItem(
                    post = post,
                    onClick = { onSelectPost(post) }
                )
            }
        }
    }
}

@Composable
fun CreatorSuggestedCard(
    user: User,
    onFollowToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .width(135.dp)
            .padding(vertical = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = user.avatarRes),
                contentDescription = user.username,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = user.displayName,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (user.isFollowing) {
                OutlinedButton(
                    onClick = onFollowToggle,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    Text("Following", fontSize = 11.sp)
                }
            } else {
                Button(
                    onClick = onFollowToggle,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibraViolet),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    Text("Follow", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ExploreGridItem(
    post: Post,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag("explore_grid_item_${post.id}")
    ) {
        Image(
            painter = painterResource(id = post.mediaRes),
            contentDescription = "Explore Item",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Subtle gradient on bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                        startY = 100f
                    )
                )
        )

        // Reels play icon if video
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Video",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(18.dp)
        )

        // Like count at bottom left
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "${post.likesCount}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
