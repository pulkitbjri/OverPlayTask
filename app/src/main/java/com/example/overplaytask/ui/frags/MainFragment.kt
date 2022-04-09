package com.example.overplaytask.ui.frags

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.overplaytask.base.components.BaseFragment
import com.example.overplaytask.base.di.fragment.FragmentComponent
import com.example.overplaytask.databinding.FragmentMianBinding
import com.example.overplaytask.exts.PermissionResponseHandler
import com.example.overplaytask.exts.withAllPermissions
import com.example.overplaytask.useCases.DetectRotationUseCase
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.Util


class MainFragment : BaseFragment<FragmentMianBinding, MainFragmentViewModel>() {
    var CONTENT_URL =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override val bindingInflater =
        { layoutInflater: LayoutInflater, viewGroup: ViewGroup?, b: Boolean ->
            FragmentMianBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun injectWith(component: FragmentComponent) = component.inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createSubscriptions()
        withAllPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            responseHandler = object : PermissionResponseHandler {
                override fun onPermissionGranted() {
                    viewModel.initLocation()
                }

                override fun onPermissionRejected() {

                }
            }
        )
    }

    private fun createSubscriptions() {
        with(binding) {
            launchForFlow {
                viewModel.dataFlow.collect {
                    with(it.restartVideo) {
                        if (this) {
                            player?.seekTo(0)
                        }
                    }
                    with(it.valPauseVideo) {
                        if (this) {
                            player?.pause()
                        }
                    }
                    with(it.rotationData) {
                        when (this) {
                            DetectRotationUseCase.TaskToPerform.FORWARD -> player?.seekForward()
                            DetectRotationUseCase.TaskToPerform.PREVIOUS -> player?.seekBack()
                            DetectRotationUseCase.TaskToPerform.VOLUME_UP -> player?.increaseDeviceVolume()
                            DetectRotationUseCase.TaskToPerform.VOLUME_DOWN -> player?.decreaseDeviceVolume()
                            DetectRotationUseCase.TaskToPerform.NONE -> {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun attachPlayer() {
        with(binding) {
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