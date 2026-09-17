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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Conversation
import com.example.data.Message
import com.example.data.User
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.VibraCyan
import com.example.ui.theme.VibraMagenta
import com.example.ui.theme.VibraViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectMessageScreen(
    conversations: List<Conversation>,
    activeConversationId: String?,
    messages: Map<String, List<Message>>,
    onSelectConversation: (String) -> Unit,
    onBackToList: () -> Unit,
    onSendMessage: (conversationId: String, text: String) -> Unit,
    onCloseMessages: () -> Unit
) {
    if (activeConversationId != null) {
        // Chat detail screen
        val conversation = conversations.find { it.id == activeConversationId }
        val chatMessages = messages[activeConversationId] ?: emptyList()

        ChatDetailScreen(
            conversation = conversation,
            messages = chatMessages,
            onBack = onBackToList,
            onSendMessage = { text -> onSendMessage(activeConversationId, text) }
        )
    } else {
        // Conversations list screen
        ConversationsListScreen(
            conversations = conversations,
            onSelectConversation = onSelectConversation,
            onClose = onCloseMessages
        )
    }
}

@Composable
fun ConversationsListScreen(
    conversations: List<Conversation>,
    onSelectConversation: (String) -> Unit,
    onClose: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredConversations = remember(conversations, searchQuery) {
        if (searchQuery.isBlank()) conversations
        else conversations.filter {
            it.participant.username.contains(searchQuery, ignoreCase = true) ||
                it.participant.displayName.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Direct Messages",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Search conversations
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search messages and creators...", fontSize = 14.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = VibraViolet)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VibraViolet,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        // Active creators online row
        Text(
            text = "Active Now",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(conversations) { conv ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSelectConversation(conv.id) }
                ) {
                    Box(modifier = Modifier.size(54.dp)) {
                        Image(
                            painter = painterResource(id = conv.participant.avatarRes),
                            contentDescription = conv.participant.username,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        if (conv.participant.isOnline) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(OnlineGreen)
                                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = conv.participant.displayName.take(8),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }
            }
        }

        Text(
            text = "Conversations",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Conversations list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredConversations, key = { it.id }) { conv ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectConversation(conv.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(50.dp)) {
                            Image(
                                painter = painterResource(id = conv.participant.avatarRes),
                                contentDescription = conv.participant.username,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            if (conv.participant.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(OnlineGreen)
                                        .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = conv.participant.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = conv.lastTimestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = conv.lastMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }

                        if (conv.unreadCount > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Badge(
                                containerColor = VibraMagenta,
                                contentColor = Color.White
                            ) {
                                Text("${conv.unreadCount}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatDetailScreen(
    conversation: Conversation?,
    messages: List<Message>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    if (conversation == null) return

    var inputText by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Box(modifier = Modifier.size(38.dp)) {
                    Image(
                        painter = painterResource(id = conversation.participant.avatarRes),
                        contentDescription = conversation.participant.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                    if (conversation.participant.isOnline) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(OnlineGreen)
                                .border(1.5.dp, MaterialTheme.colorScheme.background, CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = conversation.participant.displayName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (conversation.participant.isOnline) "Active now" else "Offline",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (conversation.participant.isOnline) OnlineGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Voice Call", tint = VibraViolet)
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Videocam, contentDescription = "Video Call", tint = VibraMagenta)
                }
            }
        }

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            reverseLayout = false,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isMe = msg.senderId == "user_me"
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isMe) 16.dp else 2.dp,
                                    bottomEnd = if (isMe) 2.dp else 16.dp
                                )
                            )
                            .background(
                                if (isMe) VibraViolet
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = msg.text,
                            color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = msg.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Emoji picker popup
        if (showEmojiPicker) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("💜", "🔥", "😂", "✨", "😍", "🙌", "💯", "🎉").forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                inputText += emoji
                                showEmojiPicker = false
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showEmojiPicker = !showEmojiPicker }
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEmotions,
                    contentDescription = "Emojis",
                    tint = VibraViolet
                )
            }

            IconButton(
                onClick = {
                    onSendMessage("Sent a photo [Sample] 📷")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Share Image",
                    tint = VibraCyan
                )
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Message...", fontSize = 14.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibraViolet,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true,
                trailingIcon = {
                    if (inputText.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onSendMessage(inputText)
                                inputText = ""
                            },
                            modifier = Modifier.testTag("send_chat_message_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = VibraViolet
                            )
                        }
                    }
                }
            )
        }
    }
}
