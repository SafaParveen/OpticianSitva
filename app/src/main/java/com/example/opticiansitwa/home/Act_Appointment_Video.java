package com.example.opticiansitwa.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opticiansitwa.databinding.ActAppointmentVideoBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Act_Appointment_Video extends AppCompatActivity {

    ActAppointmentVideoBinding binding;

    SimpleExoPlayer simpleExoPlayer;

    FirebaseStorage storage = FirebaseStorage.getInstance();;
    StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActAppointmentVideoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        storageRef.child("doctor_recordings/7bMqysJ - Imgur.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


                LoadControl loadControl = new DefaultLoadControl();

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                TrackSelector trackSelector = new DefaultTrackSelector(
                        new AdaptiveTrackSelection.Factory(bandwidthMeter)
                );


                simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(Act_Appointment_Video.this,trackSelector,loadControl);

                DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                MediaSource mediaSource = new ExtractorMediaSource(uri,factory,extractorsFactory,null,null);

                binding.recordingVideo.setPlayer(simpleExoPlayer);

                binding.recordingVideo.setKeepScreenOn(true);

                simpleExoPlayer.prepare(mediaSource);

                simpleExoPlayer.setPlayWhenReady(true);

                simpleExoPlayer.addListener(new Player.EventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                        if(playbackState == Player.STATE_BUFFERING)
                        {
                            binding.progressBar.setVisibility(View.VISIBLE);

                        }
                        else if(playbackState == Player.STATE_READY)
                        {
                            binding.progressBar.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onRepeatModeChanged(int repeatMode) {

                    }

                    @Override
                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {

                    }

                    @Override
                    public void onPositionDiscontinuity(int reason) {

                    }

                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                    }

                    @Override
                    public void onSeekProcessed() {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Act_Appointment_Video.this, "Error in loading!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayer.setPlayWhenReady(false);

        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.getPlaybackState();


    }
}
