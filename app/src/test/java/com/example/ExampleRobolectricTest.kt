package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("VIBRA", appName)
  }

  @Test
  fun `verify initial posts and toggle like`() {
    val initialPosts = com.example.data.VibraRepository.posts.value
    assert(initialPosts.isNotEmpty())

    val firstPostId = initialPosts.first().id
    val initialLikes = initialPosts.first().likesCount
    val wasLiked = initialPosts.first().isLiked

    com.example.data.VibraRepository.toggleLike(firstPostId)
    val updatedPosts = com.example.data.VibraRepository.posts.value
    val updatedPost = updatedPosts.first { it.id == firstPostId }

    if (wasLiked) {
      assertEquals(initialLikes - 1, updatedPost.likesCount)
    } else {
      assertEquals(initialLikes + 1, updatedPost.likesCount)
    }
  }
}
