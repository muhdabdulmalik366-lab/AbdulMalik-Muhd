package com.example.data

import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object VibraRepository {

    // Current logged in user
    private val _currentUser = MutableStateFlow(
        User(
            id = "user_me",
            username = "vibra_creator",
            displayName = "Nova Sky",
            avatarRes = R.drawable.vibra_logo,
            bio = "Visual artist & audio architect ✨ Exploring cyber aesthetics & future fashion. Creating ripples in the matrix.",
            website = "vibra.social/@nova",
            followersCount = 4280,
            followingCount = 384,
            postsCount = 18,
            isVerified = true,
            isFollowing = false,
            isOnline = true
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Other creators
    val creatorElena = User(
        id = "user_elena",
        username = "elena.neon",
        displayName = "Elena Vance",
        avatarRes = R.drawable.vibra_post_fashion,
        bio = "Cyberpunk street style & futuristic textiles 🌌 Tokyo based.",
        followersCount = 18400,
        followingCount = 210,
        postsCount = 89,
        isVerified = true,
        isFollowing = false,
        isOnline = true
    )

    val creatorKai = User(
        id = "user_kai",
        username = "kaizen_visuals",
        displayName = "Kai Tanaka",
        avatarRes = R.drawable.vibra_post_cyberpunk,
        bio = "Neon streets, night reflections, ambient synth beats 🌆",
        followersCount = 32900,
        followingCount = 450,
        postsCount = 142,
        isVerified = true,
        isFollowing = true,
        isOnline = true
    )

    val creatorMaya = User(
        id = "user_maya",
        username = "maya_dusk",
        displayName = "Maya Chen",
        avatarRes = R.drawable.vibra_post_sunset,
        bio = "Chasing twilight hues & coastlines around the globe 🌴",
        followersCount = 12500,
        followingCount = 320,
        postsCount = 64,
        isVerified = false,
        isFollowing = false,
        isOnline = false
    )

    val creatorAero = User(
        id = "user_aero",
        username = "aero_sound",
        displayName = "Aero Bass",
        avatarRes = R.drawable.vibra_logo,
        bio = "Electronic producer | VIBRA Soundscape Resident 🎧",
        followersCount = 8920,
        followingCount = 115,
        postsCount = 31,
        isVerified = true,
        isFollowing = false,
        isOnline = true
    )

    // Posts
    private val _posts = MutableStateFlow(
        listOf(
            Post(
                id = "post_1",
                author = creatorKai,
                mediaRes = R.drawable.vibra_post_cyberpunk,
                mediaType = MediaType.IMAGE,
                caption = "Midnight reflections in Shibuya after the rain. The neon energy hits different at 2 AM. Turn on audio for the vibe 🌆✨",
                hashtags = listOf("cyberpunk", "tokyonights", "vibraaesthetic", "streetphotography"),
                location = "Shibuya, Tokyo",
                timestamp = "2h ago",
                likesCount = 1482,
                commentsCount = 84,
                isLiked = false,
                isSaved = false,
                soundTrack = "Kai Tanaka • Neon Rain Waves (Original Audio)"
            ),
            Post(
                id = "post_2",
                author = creatorElena,
                mediaRes = R.drawable.vibra_post_fashion,
                mediaType = MediaType.IMAGE,
                caption = "Testing holographic textures and chromatic specs for the Fall capsule collection. Which tint do you vibe with more? 💜🔥",
                hashtags = listOf("futurefashion", "streetwear", "chromatic", "designer"),
                location = "Neo Seoul Studio",
                timestamp = "5h ago",
                likesCount = 3290,
                commentsCount = 156,
                isLiked = true,
                isSaved = true,
                soundTrack = "Elena Vance • Hyperpop Glitch (Remix)"
            ),
            Post(
                id = "post_3",
                author = creatorMaya,
                mediaRes = R.drawable.vibra_post_sunset,
                mediaType = MediaType.IMAGE,
                caption = "Endless twilight gradients over the pacific waters. Nature is the ultimate colorist. Unwind and breathe. 🌅🌊",
                hashtags = listOf("sunsetvibes", "coastal", "dusk", "serenity"),
                location = "Bali, Indonesia",
                timestamp = "1d ago",
                likesCount = 2105,
                commentsCount = 92,
                isLiked = false,
                isSaved = false,
                soundTrack = "Lofi Sunset Lounge • Dream Haze"
            )
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // Stories
    private val _stories = MutableStateFlow(
        listOf(
            Story(
                id = "story_me",
                user = _currentUser.value,
                mediaRes = R.drawable.vibra_post_fashion,
                caption = "New drop coming at midnight! ⚡",
                timestamp = "30m ago",
                isViewed = false
            ),
            Story(
                id = "story_kai",
                user = creatorKai,
                mediaRes = R.drawable.vibra_post_cyberpunk,
                caption = "Live from the rooftop studio 🎧",
                timestamp = "2h ago",
                isViewed = false
            ),
            Story(
                id = "story_elena",
                user = creatorElena,
                mediaRes = R.drawable.vibra_post_fashion,
                caption = "Backstage fit check ✨",
                timestamp = "4h ago",
                isViewed = false
            ),
            Story(
                id = "story_maya",
                user = creatorMaya,
                mediaRes = R.drawable.vibra_post_sunset,
                caption = "Golden hour magic 🌴",
                timestamp = "8h ago",
                isViewed = true
            ),
            Story(
                id = "story_aero",
                user = creatorAero,
                mediaRes = R.drawable.vibra_logo,
                caption = "New single out on VIBRA Sound 🎶",
                timestamp = "11h ago",
                isViewed = true
            )
        )
    )
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    // Comments for posts
    private val _comments = MutableStateFlow(
        mapOf(
            "post_1" to listOf(
                Comment(
                    id = "c1",
                    postId = "post_1",
                    user = creatorElena,
                    text = "Those reflections are unreal! The purple grade is perfection 🔥",
                    timestamp = "1h ago",
                    likesCount = 38,
                    isLiked = true
                ),
                Comment(
                    id = "c2",
                    postId = "post_1",
                    user = creatorAero,
                    text = "The audio track complements this shot so well bro 🎧🙌",
                    timestamp = "45m ago",
                    likesCount = 14,
                    isLiked = false
                ),
                Comment(
                    id = "c3",
                    postId = "post_1",
                    user = _currentUser.value,
                    text = "Incredible atmosphere! Shibuya vibe is unmatched 💜",
                    timestamp = "20m ago",
                    likesCount = 9,
                    isLiked = false
                )
            ),
            "post_2" to listOf(
                Comment(
                    id = "c4",
                    postId = "post_2",
                    user = creatorKai,
                    text = "That holographic tint is insane! Need this jacket immediately 🔥",
                    timestamp = "4h ago",
                    likesCount = 62,
                    isLiked = true
                ),
                Comment(
                    id = "c5",
                    postId = "post_2",
                    user = creatorMaya,
                    text = "Future icon right here! Amazing styling Elena ✨",
                    timestamp = "3h ago",
                    likesCount = 21,
                    isLiked = false
                )
            ),
            "post_3" to listOf(
                Comment(
                    id = "c6",
                    postId = "post_3",
                    user = creatorElena,
                    text = "So peaceful... the pink and violet sky is breathtaking 🌸",
                    timestamp = "18h ago",
                    likesCount = 45,
                    isLiked = false
                )
            )
        )
    )
    val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "n1",
                type = NotificationType.LIKE,
                user = creatorKai,
                postPreviewRes = R.drawable.vibra_post_cyberpunk,
                message = "liked your photo",
                timestamp = "5m ago",
                isRead = false
            ),
            NotificationItem(
                id = "n2",
                type = NotificationType.COMMENT,
                user = creatorElena,
                postPreviewRes = R.drawable.vibra_post_fashion,
                message = "commented: \"Those colors are electric! ⚡\"",
                timestamp = "22m ago",
                isRead = false
            ),
            NotificationItem(
                id = "n3",
                type = NotificationType.FOLLOW,
                user = creatorAero,
                message = "started following you",
                timestamp = "2h ago",
                isRead = false
            ),
            NotificationItem(
                id = "n4",
                type = NotificationType.MENTION,
                user = creatorKai,
                postPreviewRes = R.drawable.vibra_post_cyberpunk,
                message = "mentioned you in a story: \"Collab soon?\"",
                timestamp = "6h ago",
                isRead = true
            ),
            NotificationItem(
                id = "n5",
                type = NotificationType.STORY,
                user = creatorMaya,
                message = "reacted 🔥 to your story",
                timestamp = "1d ago",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Conversations
    private val _conversations = MutableStateFlow(
        listOf(
            Conversation(
                id = "conv_kai",
                participant = creatorKai,
                lastMessage = "Sent you the synth preview, check it out!",
                lastTimestamp = "14:22",
                unreadCount = 2
            ),
            Conversation(
                id = "conv_elena",
                participant = creatorElena,
                lastMessage = "Loved your latest story post with the neon grade! 💜",
                lastTimestamp = "Yesterday",
                unreadCount = 0
            ),
            Conversation(
                id = "conv_aero",
                participant = creatorAero,
                lastMessage = "Let's do a live sound session this weekend.",
                lastTimestamp = "Tue",
                unreadCount = 0
            )
        )
    )
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    // Chat messages
    private val _messages = MutableStateFlow(
        mapOf(
            "conv_kai" to listOf(
                Message("m1", "conv_kai", "user_kai", "Hey Nova! Loved that neon color palette you used yesterday.", "14:15"),
                Message("m2", "conv_kai", "user_me", "Thanks Kai! It was inspired by your Shibuya series actually 😄", "14:18"),
                Message("m3", "conv_kai", "user_kai", "Awesome! I recorded a new atmospheric ambient track that matches the vibe.", "14:20"),
                Message("m4", "conv_kai", "user_kai", "Sent you the synth preview, check it out!", "14:22")
            ),
            "conv_elena" to listOf(
                Message("m5", "conv_elena", "user_me", "Hey Elena! Is the cyber jacket available in violet yet?", "10:30"),
                Message("m6", "conv_elena", "user_elena", "Yes! We just printed the holographic badges today ✨", "10:35"),
                Message("m7", "conv_elena", "user_elena", "Loved your latest story post with the neon grade! 💜", "10:38")
            ),
            "conv_aero" to listOf(
                Message("m8", "conv_aero", "user_aero", "Let's do a live sound session this weekend.", "Sep 15")
            )
        )
    )
    val messages: StateFlow<Map<String, List<Message>>> = _messages.asStateFlow()

    // Explore categories
    val categories = listOf(
        ExploreCategory("cat_foryou", "For You", "#vibra", "1.2M"),
        ExploreCategory("cat_cyberpunk", "Cyberpunk", "#neonmatrix", "840K"),
        ExploreCategory("cat_fashion", "Fashion", "#futuristic", "950K"),
        ExploreCategory("cat_photo", "Photography", "#visuals", "2.1M"),
        ExploreCategory("cat_music", "Music & Beats", "#synthwave", "670K"),
        ExploreCategory("cat_travel", "Wanderlust", "#aestheticplaces", "1.4M")
    )

    // Suggested creators
    val suggestedCreators = listOf(
        creatorElena,
        creatorKai,
        creatorMaya,
        creatorAero
    )

    // App Preferences / Settings
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(true)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _isPrivateAccount = MutableStateFlow(false)
    val isPrivateAccount: StateFlow<Boolean> = _isPrivateAccount.asStateFlow()

    private val _filterOffensiveComments = MutableStateFlow(true)
    val filterOffensiveComments: StateFlow<Boolean> = _filterOffensiveComments.asStateFlow()

    private val _blockedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val blockedUserIds: StateFlow<Set<String>> = _blockedUserIds.asStateFlow()

    private val _mutedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val mutedUserIds: StateFlow<Set<String>> = _mutedUserIds.asStateFlow()

    // ---------------- Actions ----------------

    fun toggleDarkMode() {
        _isDarkMode.update { !it }
    }

    fun toggleLike(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                    post.copy(isLiked = newLiked, likesCount = newCount)
                } else post
            }
        }
    }

    fun toggleSave(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(isSaved = !post.isSaved)
                } else post
            }
        }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "comment_${System.currentTimeMillis()}",
            postId = postId,
            user = _currentUser.value,
            text = text.trim(),
            timestamp = "Just now",
            likesCount = 0,
            isLiked = false
        )
        _comments.update { map ->
            val list = map[postId] ?: emptyList()
            map + (postId to (list + newComment))
        }
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(commentsCount = post.commentsCount + 1)
                } else post
            }
        }
    }

    fun toggleCommentLike(postId: String, commentId: String) {
        _comments.update { map ->
            val list = map[postId] ?: return@update map
            val updated = list.map { comment ->
                if (comment.id == commentId) {
                    val newLiked = !comment.isLiked
                    val newCount = if (newLiked) comment.likesCount + 1 else (comment.likesCount - 1).coerceAtLeast(0)
                    comment.copy(isLiked = newLiked, likesCount = newCount)
                } else comment
            }
            map + (postId to updated)
        }
    }

    fun createPost(
        caption: String,
        hashtags: List<String>,
        location: String?,
        allowComments: Boolean,
        mediaRes: Int
    ) {
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            author = _currentUser.value,
            mediaRes = mediaRes,
            mediaType = MediaType.IMAGE,
            caption = caption.trim(),
            hashtags = hashtags,
            location = location?.takeIf { it.isNotBlank() },
            timestamp = "Just now",
            likesCount = 0,
            commentsCount = 0,
            isLiked = false,
            isSaved = false,
            allowComments = allowComments,
            soundTrack = "VIBRA Original Sound • Nova Sky"
        )
        _posts.update { listOf(newPost) + it }
        _currentUser.update { it.copy(postsCount = it.postsCount + 1) }
    }

    fun addStory(mediaRes: Int, caption: String?) {
        val newStory = Story(
            id = "story_${System.currentTimeMillis()}",
            user = _currentUser.value,
            mediaRes = mediaRes,
            caption = caption,
            timestamp = "Just now",
            isViewed = false
        )
        _stories.update { listOf(newStory) + it }
    }

    fun markStoryViewed(storyId: String) {
        _stories.update { list ->
            list.map { if (it.id == storyId) it.copy(isViewed = true) else it }
        }
    }

    fun toggleFollow(userId: String) {
        // Toggle in suggested and post authors
        _posts.update { list ->
            list.map { post ->
                if (post.author.id == userId) {
                    val updatedAuthor = post.author.copy(isFollowing = !post.author.isFollowing)
                    post.copy(author = updatedAuthor)
                } else post
            }
        }
        _currentUser.update { user ->
            val isNowFollowing = _posts.value.any { it.author.id == userId && it.author.isFollowing }
            val countDelta = if (isNowFollowing) 1 else -1
            user.copy(followingCount = (user.followingCount + countDelta).coerceAtLeast(0))
        }
    }

    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = Message(
            id = "msg_${System.currentTimeMillis()}",
            conversationId = conversationId,
            senderId = "user_me",
            text = text.trim(),
            timestamp = "Just now"
        )
        _messages.update { map ->
            val list = map[conversationId] ?: emptyList()
            map + (conversationId to (list + newMsg))
        }
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(lastMessage = text.trim(), lastTimestamp = "Just now", unreadCount = 0)
                } else conv
            }
        }
    }

    fun markNotificationsAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun updateProfile(displayName: String, bio: String, website: String) {
        _currentUser.update {
            it.copy(displayName = displayName.trim(), bio = bio.trim(), website = website.trim())
        }
    }

    fun reportPost(postId: String, reason: String) {
        // Mark post or acknowledge report
    }

    fun blockUser(userId: String) {
        _blockedUserIds.update { it + userId }
        _posts.update { list -> list.filter { it.author.id != userId } }
    }

    fun muteUser(userId: String) {
        _mutedUserIds.update { it + userId }
    }

    fun togglePrivateAccount() {
        _isPrivateAccount.update { !it }
    }

    fun toggleFilterComments() {
        _filterOffensiveComments.update { !it }
    }

    fun login(username: String) {
        _currentUser.update { it.copy(username = username) }
        _isAuthenticated.value = true
    }

    fun logout() {
        _isAuthenticated.value = false
    }
}
