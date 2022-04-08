package com.example.overplaytask.ui.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.overplaytask.R
import com.example.overplaytask.base.components.BaseFragment
import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.base.di.fragment.FragmentComponent
import com.example.overplaytask.databinding.FragmentMianBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.util.Util


class MainFragment : BaseFragment<FragmentMianBinding, MainFragmentViewModel>() {
    var CONTENT_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override val bindingInflater = { layoutInflater: LayoutInflater, viewGroup: ViewGroup?, b: Boolean ->
        FragmentMianBinding.inflate(layoutInflater, viewGroup, b)
    }

    override fun injectWith(component: FragmentComponent) = component.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun attachPlayer() {
        with(binding){
            player = ExoPlayer.Builder(requireContext())
                .build()
                .also { exoPlayer ->
                    playerMain.player = exoPlayer
                    val mediaItem = MediaItem.fromUri(CONTENT_URL)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.seekTo(currentWindow, playbackPosition)
                    exoPlayer.prepare()
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            attachPlayer()
        }
    }
    public override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            attachPlayer()
        }
    }
    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }
    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }
}