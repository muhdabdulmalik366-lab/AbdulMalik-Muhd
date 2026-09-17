package com.example.data

import androidx.annotation.DrawableRes
import com.example.R

enum class MediaType {
    IMAGE,
    VIDEO
}

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION,
    STORY
}

data class User(
    val id: String,
    val username: String,
    val displayName: String,
    @DrawableRes val avatarRes: Int = R.drawable.vibra_logo,
    val bio: String = "",
    val website: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val isVerified: Boolean = false,
    val isFollowing: Boolean = false,
    val isOnline: Boolean = false
)

data class Post(
    val id: String,
    val author: User,
    @DrawableRes val mediaRes: Int,
    val mediaType: MediaType = MediaType.IMAGE,
    val caption: String,
    val hashtags: List<String> = emptyList(),
    val location: String? = null,
    val timestamp: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val allowComments: Boolean = true,
    val soundTrack: String? = null
)

data class Story(
    val id: String,
    val user: User,
    @DrawableRes val mediaRes: Int,
    val caption: String? = null,
    val timestamp: String,
    val isViewed: Boolean = false
)

data class Comment(
    val id: String,
    val postId: String,
    val user: User,
    val text: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val user: User,
    @DrawableRes val postPreviewRes: Int? = null,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    @DrawableRes val mediaRes: Int? = null
)

data class Conversation(
    val id: String,
    val participant: User,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0
)

data class ExploreCategory(
    val id: String,
    val name: String,
    val tag: String,
    val count: String
)
