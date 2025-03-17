package br.iwan.oldpokedex.ui.player

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object AppPlayer {
    private lateinit var player: ExoPlayer

    fun createPlayerInstance(applicationContext: Context) {
        player = ExoPlayer.Builder(applicationContext)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .build()
    }

    fun playSound(url: String) {
        player.run {
            setMediaItem(
                MediaItem.fromUri(url)
            )
            prepare()
            play()
        }
    }

    fun releasePlayer() {
        player.release()
    }
}