package br.iwan.oldpokedex.ui.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import java.io.File

@OptIn(UnstableApi::class)
object AppPlayer {
    private lateinit var player: ExoPlayer
    private lateinit var dataFactory: CacheDataSource.Factory

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

        val simpleCache = SimpleCache(
            File(applicationContext.cacheDir, "exoplayer_cache"),
            LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024),// 100MB cache
            StandaloneDatabaseProvider(applicationContext)
        )

        dataFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    fun playSound(url: String) {
        player.run {
            setMediaSource(
                ProgressiveMediaSource.Factory(dataFactory)
                    .createMediaSource(MediaItem.fromUri(url))
            )

            prepare()
            play()
        }
    }

    fun releasePlayer() {
        player.release()
    }
}