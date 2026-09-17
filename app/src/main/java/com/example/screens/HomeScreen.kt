package com.example.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.Post
import com.example.data.Story
import com.example.data.User
import com.example.ui.theme.HeartRed
import com.example.ui.theme.VibraBrandGradient
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraStoryRingGradient
import com.example.ui.theme.VibraViolet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    posts: List<Post>,
    stories: List<Story>,
    currentUser: User,
    onStoryClick: (index: Int) -> Unit,
    onAddStoryClick: () -> Unit,
    onLikePost: (postId: String) -> Unit,
    onSavePost: (postId: String) -> Unit,
    onCommentClick: (post: Post) -> Unit,
    onSharePost: (post: Post) -> Unit,
    onOptionsClick: (post: Post) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Stories Reel at the top
        item {
            StoriesReel(
                stories = stories,
                currentUser = currentUser,
                onStoryClick = onStoryClick,
                onAddStoryClick = onAddStoryClick
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Feed of Posts
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onLike = { onLikePost(post.id) },
                onSave = { onSavePost(post.id) },
                onComment = { onCommentClick(post) },
                onShare = { onSharePost(post) },
                onOptions = { onOptionsClick(post) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
fun StoriesReel(
    stories: List<Story>,
    currentUser: User,
    onStoryClick: (index: Int) -> Unit,
    onAddStoryClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Your Story" item with add badge
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = onAddStoryClick)
                    .testTag("add_story_button")
            ) {
                Box(
                    modifier = Modifier.size(68.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = currentUser.avatarRes),
                        contentDescription = "Your Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(VibraBrandGradient)
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Story",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your Story",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Other creators' stories
        itemsIndexed(stories) { index, story ->
            val hasViewed = story.isViewed
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onStoryClick(index) }
                    .testTag("story_circle_${story.user.username}")
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .then(
                            if (!hasViewed) {
                                Modifier.background(VibraStoryRingGradient)
                            } else {
                                Modifier.background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                            }
                        )
                        .padding(2.5.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = story.user.avatarRes),
                        contentDescription = story.user.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = story.user.username.take(9),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onOptions: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showDoubleTapHeart by remember { mutableStateOf(false) }
    val heartScale = remember { Animatable(0f) }

    fun triggerDoubleTapLike() {
        if (!post.isLiked) {
            onLike()
        }
        showDoubleTapHeart = true
        coroutineScope.launch {
            heartScale.snapTo(0f)
            heartScale.animateTo(
                targetValue = 1.3f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
            delay(400)
            heartScale.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200)
            )
            showDoubleTapHeart = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Author details & 3-dot options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = post.author.avatarRes),
                        contentDescription = post.author.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.author.username,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (post.author.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = VibraCyan,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        post.location?.let { loc ->
                            Text(
                                text = "$loc • ${post.timestamp}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } ?: run {
                            Text(
                                text = post.timestamp,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onOptions,
                    modifier = Modifier.testTag("post_options_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Post Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Media Container with Double-tap to Like detection
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { triggerDoubleTapLike() }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = post.mediaRes),
                    contentDescription = "Post Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Double tap heart popup
                if (showDoubleTapHeart) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = HeartRed.copy(alpha = 0.95f),
                        modifier = Modifier
                            .size(110.dp)
                            .scale(heartScale.value)
                    )
                }

                // Sound track audio pill at bottom left
                post.soundTrack?.let { sound ->
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Audio track",
                            tint = VibraCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = sound,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }

            // Action Row: Like, Comment, Share, Bookmark
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Like button with burst animation
                    IconButton(
                        onClick = onLike,
                        modifier = Modifier.testTag("like_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like Post",
                            tint = if (post.isLiked) HeartRed else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Comment button
                    IconButton(
                        onClick = onComment,
                        modifier = Modifier.testTag("comment_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.testTag("share_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Bookmark / Save button
                IconButton(
                    onClick = onSave,
                    modifier = Modifier.testTag("save_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save Post",
                        tint = if (post.isSaved) VibraViolet else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Likes count & Caption
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "${post.likesCount} likes",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Caption with author username bolded and hashtags colored
                val annotatedCaption = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${post.author.username} ")
                    }
                    append(post.caption)
                    if (post.hashtags.isNotEmpty()) {
                        append(" ")
                        post.hashtags.forEach { tag ->
                            withStyle(SpanStyle(color = VibraViolet, fontWeight = FontWeight.SemiBold)) {
                                append("#$tag ")
                            }
                        }
                    }
                }
                Text(
                    text = annotatedCaption,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Comments shortcut
                if (post.commentsCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "View all ${post.commentsCount} comments",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable(onClick = onComment)
                    )
                }
            }
        }
    }
}
