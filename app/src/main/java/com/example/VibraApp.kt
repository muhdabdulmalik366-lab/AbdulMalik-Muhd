package com.example

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.CommentsBottomSheet
import com.example.components.PostDetailDialog
import com.example.components.PostOptionsSheet
import com.example.components.ReportDialog
import com.example.components.VibraTopBar
import com.example.data.Post
import com.example.data.VibraRepository
import com.example.screens.AuthDialog
import com.example.screens.CreatePostScreen
import com.example.screens.DirectMessageScreen
import com.example.screens.ExploreScreen
import com.example.screens.HomeScreen
import com.example.screens.NotificationsScreen
import com.example.screens.ProfileScreen
import com.example.screens.SafetySettingsSheet
import com.example.screens.StoryViewerScreen
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraTheme
import com.example.ui.theme.VibraViolet
import kotlinx.coroutines.launch

enum class VibraNavDestination(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
    EXPLORE("Explore", Icons.Filled.Explore, Icons.Outlined.Explore, "nav_explore"),
    CREATE("Create", Icons.Filled.AddBox, Icons.Outlined.AddBox, "nav_create"),
    NOTIFICATIONS("Alerts", Icons.Filled.Notifications, Icons.Outlined.Notifications, "nav_notifications"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
}

@Composable
fun VibraApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Repository states
    val isDarkMode by VibraRepository.isDarkMode.collectAsState()
    val currentUser by VibraRepository.currentUser.collectAsState()
    val posts by VibraRepository.posts.collectAsState()
    val stories by VibraRepository.stories.collectAsState()
    val comments by VibraRepository.comments.collectAsState()
    val notifications by VibraRepository.notifications.collectAsState()
    val conversations by VibraRepository.conversations.collectAsState()
    val messages by VibraRepository.messages.collectAsState()
    val isPrivateAccount by VibraRepository.isPrivateAccount.collectAsState()
    val filterOffensiveComments by VibraRepository.filterOffensiveComments.collectAsState()
    val blockedUserIds by VibraRepository.blockedUserIds.collectAsState()
    val mutedUserIds by VibraRepository.mutedUserIds.collectAsState()

    // Navigation & Overlay states
    var currentDestination by remember { mutableStateOf(VibraNavDestination.HOME) }
    var viewingStoryIndex by remember { mutableStateOf<Int?>(null) }
    var activePostForComments by remember { mutableStateOf<Post?>(null) }
    var activePostForOptions by remember { mutableStateOf<Post?>(null) }
    var reportingPostId by remember { mutableStateOf<String?>(null) }
    var inspectedPost by remember { mutableStateOf<Post?>(null) }
    var showSafetySettings by remember { mutableStateOf(false) }
    var showDirectMessages by remember { mutableStateOf(false) }
    var activeConversationId by remember { mutableStateOf<String?>(null) }
    var showAuthDialog by remember { mutableStateOf(false) }

    val unreadMessagesCount = conversations.sumOf { it.unreadCount }
    val unreadNotificationsCount = notifications.count { !it.isRead }

    VibraTheme(darkTheme = isDarkMode) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 720.dp

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    if (!showDirectMessages && viewingStoryIndex == null) {
                        VibraTopBar(
                            isDarkMode = isDarkMode,
                            unreadMessagesCount = unreadMessagesCount,
                            onToggleDarkMode = { VibraRepository.toggleDarkMode() },
                            onOpenMessages = {
                                showDirectMessages = true
                                activeConversationId = null
                            },
                            onOpenSafety = { showSafetySettings = true }
                        )
                    }
                },
                bottomBar = {
                    if (!isWideScreen && !showDirectMessages && viewingStoryIndex == null) {
                        NavigationBar(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .testTag("vibra_bottom_nav"),
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            VibraNavDestination.entries.forEach { dest ->
                                val isSelected = currentDestination == dest
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentDestination = dest },
                                    icon = {
                                        if (dest == VibraNavDestination.NOTIFICATIONS && unreadNotificationsCount > 0) {
                                            BadgedBox(
                                                badge = {
                                                    Badge(containerColor = VibraMagenta) {
                                                        Text("$unreadNotificationsCount")
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) dest.selectedIcon else dest.unselectedIcon,
                                                    contentDescription = dest.title
                                                )
                                            }
                                        } else {
                                            Icon(
                                                imageVector = if (isSelected) dest.selectedIcon else dest.unselectedIcon,
                                                contentDescription = dest.title
                                            )
                                        }
                                    },
                                    label = { Text(dest.title, fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = VibraViolet,
                                        selectedTextColor = VibraViolet,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.testTag(dest.testTag)
                                )
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Wide screen navigation rail for desktop/tablets
                    if (isWideScreen && !showDirectMessages && viewingStoryIndex == null) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            VibraNavDestination.entries.forEach { dest ->
                                val isSelected = currentDestination == dest
                                NavigationRailItem(
                                    selected = isSelected,
                                    onClick = { currentDestination = dest },
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) dest.selectedIcon else dest.unselectedIcon,
                                            contentDescription = dest.title
                                        )
                                    },
                                    label = { Text(dest.title) },
                                    colors = NavigationRailItemDefaults.colors(
                                        selectedIconColor = VibraViolet,
                                        selectedTextColor = VibraViolet,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            }
                        }
                    }

                    // Content switcher
                    Box(modifier = Modifier.weight(1f)) {
                        when (currentDestination) {
                            VibraNavDestination.HOME -> {
                                HomeScreen(
                                    posts = posts,
                                    stories = stories,
                                    currentUser = currentUser,
                                    onStoryClick = { index -> viewingStoryIndex = index },
                                    onAddStoryClick = {
                                        currentDestination = VibraNavDestination.CREATE
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Select media to share as post or story")
                                        }
                                    },
                                    onLikePost = { postId -> VibraRepository.toggleLike(postId) },
                                    onSavePost = { postId ->
                                        VibraRepository.toggleSave(postId)
                                        coroutineScope.launch {
                                            val post = posts.find { it.id == postId }
                                            val msg = if (post?.isSaved == true) "Post removed from Saved" else "Post added to Saved"
                                            snackbarHostState.showSnackbar(msg)
                                        }
                                    },
                                    onCommentClick = { post -> activePostForComments = post },
                                    onSharePost = { post ->
                                        showDirectMessages = true
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Select a creator to forward post")
                                        }
                                    },
                                    onOptionsClick = { post -> activePostForOptions = post }
                                )
                            }

                            VibraNavDestination.EXPLORE -> {
                                ExploreScreen(
                                    posts = posts,
                                    categories = VibraRepository.categories,
                                    suggestedCreators = VibraRepository.suggestedCreators,
                                    onSelectPost = { post -> inspectedPost = post },
                                    onToggleFollow = { userId ->
                                        VibraRepository.toggleFollow(userId)
                                    }
                                )
                            }

                            VibraNavDestination.CREATE -> {
                                CreatePostScreen(
                                    onPublishPost = { caption, hashtags, location, allowComments, mediaRes ->
                                        VibraRepository.createPost(
                                            caption = caption,
                                            hashtags = hashtags,
                                            location = location,
                                            allowComments = allowComments,
                                            mediaRes = mediaRes
                                        )
                                        currentDestination = VibraNavDestination.HOME
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Post published to VIBRA feed! ⚡")
                                        }
                                    }
                                )
                            }

                            VibraNavDestination.NOTIFICATIONS -> {
                                NotificationsScreen(
                                    notifications = notifications,
                                    onMarkAllRead = {
                                        VibraRepository.markNotificationsAsRead()
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("All notifications marked as read")
                                        }
                                    },
                                    onFollowToggle = { userId ->
                                        VibraRepository.toggleFollow(userId)
                                    }
                                )
                            }

                            VibraNavDestination.PROFILE -> {
                                val savedPosts = posts.filter { it.isSaved }
                                val myPosts = posts.filter { it.author.id == currentUser.id }
                                ProfileScreen(
                                    user = currentUser,
                                    userPosts = if (myPosts.isEmpty()) posts.take(1) else myPosts,
                                    savedPosts = savedPosts,
                                    onSelectPost = { post -> inspectedPost = post },
                                    onUpdateProfile = { displayName, bio, website ->
                                        VibraRepository.updateProfile(displayName, bio, website)
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Profile updated successfully")
                                        }
                                    },
                                    onOpenSafetySettings = { showSafetySettings = true },
                                    onShareProfile = {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Profile link copied: vibra.social/@${currentUser.username}")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Fullscreen Story Viewer Overlay
            viewingStoryIndex?.let { startIndex ->
                StoryViewerScreen(
                    stories = stories,
                    initialIndex = startIndex,
                    onClose = { viewingStoryIndex = null },
                    onStoryViewed = { storyId -> VibraRepository.markStoryViewed(storyId) },
                    onSendReaction = { storyId, reaction ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Reaction sent: $reaction")
                        }
                    }
                )
            }

            // Fullscreen Direct Messages Overlay
            if (showDirectMessages) {
                DirectMessageScreen(
                    conversations = conversations,
                    activeConversationId = activeConversationId,
                    messages = messages,
                    onSelectConversation = { convId -> activeConversationId = convId },
                    onBackToList = { activeConversationId = null },
                    onSendMessage = { convId, text ->
                        VibraRepository.sendMessage(convId, text)
                    },
                    onCloseMessages = {
                        showDirectMessages = false
                        activeConversationId = null
                    }
                )
            }

            // Comments Bottom Sheet
            activePostForComments?.let { post ->
                val postComments = comments[post.id] ?: emptyList()
                CommentsBottomSheet(
                    post = post,
                    comments = postComments,
                    currentUser = currentUser,
                    onDismiss = { activePostForComments = null },
                    onAddComment = { text ->
                        VibraRepository.addComment(post.id, text)
                        // Refresh active post comment count
                        activePostForComments = posts.find { it.id == post.id }
                    },
                    onToggleCommentLike = { commentId ->
                        VibraRepository.toggleCommentLike(post.id, commentId)
                    }
                )
            }

            // Post Options Sheet (Report, Mute, Block, etc.)
            activePostForOptions?.let { post ->
                PostOptionsSheet(
                    post = post,
                    onDismiss = { activePostForOptions = null },
                    onReport = {
                        activePostForOptions = null
                        reportingPostId = post.id
                    },
                    onMuteUser = { userId ->
                        VibraRepository.muteUser(userId)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Muted @${post.author.username}")
                        }
                    },
                    onBlockUser = { userId ->
                        VibraRepository.blockUser(userId)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Blocked @${post.author.username}")
                        }
                    },
                    onShare = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Post link copied to clipboard")
                        }
                    }
                )
            }

            // Report Dialog
            reportingPostId?.let { postId ->
                ReportDialog(
                    postId = postId,
                    onDismiss = { reportingPostId = null },
                    onReportSubmitted = { reason ->
                        VibraRepository.reportPost(postId, reason)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Thank you. Report received for: $reason")
                        }
                    }
                )
            }

            // Post Detail Lightbox
            inspectedPost?.let { post ->
                PostDetailDialog(
                    post = post,
                    onDismiss = { inspectedPost = null },
                    onLikeToggle = {
                        VibraRepository.toggleLike(post.id)
                        inspectedPost = posts.find { it.id == post.id }
                    },
                    onCommentClick = {
                        val target = post
                        inspectedPost = null
                        activePostForComments = target
                    }
                )
            }

            // Safety & Privacy Settings Sheet
            if (showSafetySettings) {
                SafetySettingsSheet(
                    isPrivateAccount = isPrivateAccount,
                    filterOffensiveComments = filterOffensiveComments,
                    blockedCount = blockedUserIds.size,
                    mutedCount = mutedUserIds.size,
                    onTogglePrivateAccount = {
                        VibraRepository.togglePrivateAccount()
                        coroutineScope.launch {
                            val status = if (!isPrivateAccount) "Account is now Private" else "Account is now Public"
                            snackbarHostState.showSnackbar(status)
                        }
                    },
                    onToggleFilterComments = {
                        VibraRepository.toggleFilterComments()
                        coroutineScope.launch {
                            val status = if (!filterOffensiveComments) "Automated comment filter enabled" else "Comment filter disabled"
                            snackbarHostState.showSnackbar(status)
                        }
                    },
                    onLogout = {
                        showSafetySettings = false
                        showAuthDialog = true
                    },
                    onDismiss = { showSafetySettings = false }
                )
            }

            // Auth Dialog
            if (showAuthDialog) {
                AuthDialog(
                    onDismiss = { showAuthDialog = false },
                    onLoginSuccess = { username ->
                        VibraRepository.login(username)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Welcome back to VIBRA, @$username! ✨")
                        }
                    }
                )
            }
        }
    }
}
