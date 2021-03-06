package com.duan.musicoco.play;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.duan.musicoco.R;
import com.duan.musicoco.aidl.IPlayControl;
import com.duan.musicoco.aidl.Song;
import com.duan.musicoco.app.InspectActivity;
import com.duan.musicoco.app.interfaces.OnServiceConnect;
import com.duan.musicoco.app.interfaces.ThemeChangeable;
import com.duan.musicoco.app.manager.ActivityManager;
import com.duan.musicoco.app.manager.BroadcastManager;
import com.duan.musicoco.app.manager.PlayServiceManager;
import com.duan.musicoco.modle.SongInfo;
import com.duan.musicoco.play.album.PlayVisualizer;
import com.duan.musicoco.play.album.VisualizerFragment;
import com.duan.musicoco.play.bottomnav.BottomNavigationController;
import com.duan.musicoco.play.lyric.LyricFragment;
import com.duan.musicoco.preference.PlayPreference;
import com.duan.musicoco.preference.ThemeEnum;
import com.duan.musicoco.service.PlayController;
import com.duan.musicoco.service.PlayServiceCallback;
import com.duan.musicoco.util.ArrayUtils;
import com.duan.musicoco.util.ColorUtils;
import com.duan.musicoco.util.Utils;

/**
 * Created by DuanJiaNing on 2017/5/23.
 */

public class PlayActivity extends InspectActivity implements
        PlayServiceCallback,
        OnServiceConnect,
        View.OnClickListener,
        ThemeChangeable {

    private VisualizerFragment visualizerFragment;
    private LyricFragment lyricFragment;
    protected IPlayControl control;

    private PlayServiceConnection mServiceConnection;
    private PlayServiceManager playServiceManager;

    private BottomNavigationController bottomNavigationController;
    private PlayBgDrawableController bgDrawableController;
    private PlayViewsController viewsController;

    private BroadcastReceiver themeChangeReceiver;
    private BroadcastReceiver songFavoriteChangeReceiver;
    private BroadcastManager broadcastManager;

    private PlayVisualizer playVisualizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Utils.transitionStatusBar(this);
        Utils.hideNavAndStatus(getWindow().getDecorView());

        //??????????????????????????? permissionGranted ??? permissionDenied
        checkPermission();

    }

    @Override
    public void permissionGranted(int requestCode) {

        broadcastManager = BroadcastManager.getInstance();
        playServiceManager = new PlayServiceManager(this);
        bgDrawableController = new PlayBgDrawableController(this, playPreference);
        viewsController = new PlayViewsController(this);
        bottomNavigationController = new BottomNavigationController(this, dbController, mediaManager, playPreference, appPreference);

        initViews();
        bindService();

    }

    private void initViews() {

        bgDrawableController.initViews();
        viewsController.initViews();
        bottomNavigationController.initViews();
        initSelfViews();

    }

    private void initSelfViews() {
        FrameLayout flFragmentContainer;
        flFragmentContainer = (FrameLayout) findViewById(R.id.play_fragment_container);
        flFragmentContainer.setClickable(true);
        flFragmentContainer.setOnClickListener(this);
        flFragmentContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (bottomNavigationController.isListTitleHide()) {
                    bottomNavigationController.showPlayListTitle();
                    return true;
                }
                return false;
            }
        });

        View nameContainer = findViewById(R.id.play_name_container);
        nameContainer.setClickable(true);
        nameContainer.setOnClickListener(this);

        lyricFragment = new LyricFragment();
        visualizerFragment = new VisualizerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //?????? ????????????
        transaction.add(R.id.play_fragment_container, visualizerFragment, VisualizerFragment.TAG);
        transaction.add(R.id.play_fragment_container, lyricFragment, LyricFragment.TAG);
        transaction.hide(lyricFragment);
        transaction.commit();
    }

    private void bindService() {
        mServiceConnection = new PlayServiceConnection(this, this, this);
        // ????????????????????? onConnected
        playServiceManager.bindService(mServiceConnection);
    }

    @Override
    public void onConnected(final ComponentName name, IBinder service) {
        this.control = IPlayControl.Stub.asInterface(service);

        initSelfData();
        initBroadcastReceivers();

    }

    private void initSelfData() {
        try {
            Song song = control.currentSong();

            if (song == null) { //??????????????????????????????
                noSongInService();
                viewsController.updateText(0, 0, "", "");
            } else {

                bottomNavigationController.initData(control);
                viewsController.initData(playPreference, control);

                // ??? updateCurrentSongInfo ?????????initData ???????????????updateCurrentSongInfo ?????????????????????????????? VARYING ????????????????????????
                // ?????? VARYING ???????????????????????????????????????
                themeChange(null, null);
                initViewsColors();

                // ???????????? onCreate ???????????? songChanged ???PlayActivity ?????????????????????????????????????????????
                // ????????????????????????
                songChanged(song, control.currentSongIndex(), true);

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateVisualizer() {
        try {

            if (playVisualizer == null) {

                int audioSessionId = control.getAudioSessionId();
                playVisualizer = new PlayVisualizer();
                playVisualizer.setupVisualizer(160, audioSessionId, new PlayVisualizer.OnFftDataCaptureListener() {
                    @Override
                    public void onFftCapture(float[] fft) {
//                        float[] ffs = handleFFT(fft);
                        viewsController.updateBarWaveHeight(fft);
                        viewsController.updateBarWaveColors(
                                handleColors(fft, viewsController.getBarWaveColor()));
                    }
                });
            }

            playVisualizer.setVisualizerEnable(settingPreference.getDotWaveEnable() &&
                    control.status() == PlayController.STATUS_PLAYING);

            viewsController.updateBarWaveVisible(settingPreference.getDotWaveEnable());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private int[][] handleColors(float[] fft, PlayViewsController.BarWaveColor barWaveColor) {

        int[][] cs = new int[fft.length][2];
        float average = ArrayUtils.average(fft);
        for (int i = 0; i < fft.length; i++) {
            cs[i][0] = barWaveColor.barColor;
            cs[i][1] = barWaveColor.waveColor;

        }

        return cs;
    }

    private float[] handleFFT(float[] fft) {

        int splitLength = fft.length / 3; // 20

        float average = ArrayUtils.average(fft);
        for (int i = 0; i < fft.length; i++) {
            if (fft[i] > average * 3) fft[i] = fft[i] - average;
        }

        float[] t1 = new float[20];
        float[] t2 = new float[40];
        float[] t2_ = new float[20];
        float[] t3 = new float[20];

        System.arraycopy(fft, 19, t1, 0, 20);
        t1 = ArrayUtils.reverse(t1);
        System.arraycopy(fft, 39, t3, 0, 20);

        System.arraycopy(fft, 0, t2_, 0, 20);
        int j = 0;
        for (int i = 0; i < t2_.length - 1; i++) {
            t2[j++] = t2_[i];
            t2[j++] = t2_[i] + (t2_[i + 1] - t2_[i]) / 2;
        }

//        float[] fft_ = new float[80]; // ??? play_bar_waves ??????
//        System.arraycopy(t1, 0, fft_, 0, 20);
//        System.arraycopy(t2, 0, fft_, 20, 40);
//        System.arraycopy(t3, 0, fft_, 60, 20);
//
//        float[] fft__ = new float[160];
//        j = 0;
//        for (int i = 0; i < fft_.length - 1; i++) {
//            fft__[j++] = fft_[i];
//            fft__[j++] = fft_[i] + (fft_[i + 1] - fft_[i]) / 2;
//        }

        float[] fft__ = new float[80];
        j = 0;
        for (int i = 0; i < t2.length - 1; i++) {
            fft__[j++] = t2[i];
            fft__[j++] = t2[i] + (t2[i + 1] - t2[i]) / 2;
        }

        float[] fft___ = new float[160];
        j = 0;
        for (int i = 0; i < fft__.length - 1; i++) {
            fft___[j++] = fft__[i];
            fft___[j++] = fft__[i] + (fft__[i + 1] - fft__[i]) / 2;
        }

        return fft___;
    }


    // ??????????????????????????????????????????????????? ContentUpdatable ?????????????????????????????????????????????????????????????????????????????????
    // ?????????????????????????????????????????????????????????????????????????????????????????????????????? MainActivity ???????????? PlayService ???
    // PlayActivity ????????????
    public void noSongInService() {
        bottomNavigationController.noSongInService();
    }

    private void initBroadcastReceivers() {
        themeChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int which = intent.getIntExtra(BroadcastManager.Play.PLAY_THEME_CHANGE_TOKEN, Integer.MAX_VALUE);
                if (which == BroadcastManager.Play.PLAY_APP_THEME_CHANGE) {
                    themeChange(null, null);
                } else if (which == BroadcastManager.Play.PLAY_PLAY_THEME_CHANGE) {
                    updateViewsColorsIfNeed(null);
                    initViewsColors();
                }
            }
        };

        songFavoriteChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bottomNavigationController.updateFavorite();
            }
        };

        broadcastManager.registerBroadReceiver(this, songFavoriteChangeReceiver, BroadcastManager.FILTER_MAIN_SHEET_UPDATE);
        broadcastManager.registerBroadReceiver(this, themeChangeReceiver, BroadcastManager.FILTER_PLAY_UI_MODE_CHANGE);
    }

    //--------------------------------------------------------------------//--------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
        unregisterReceiver();
        if (settingPreference.getDotWaveEnable()) {
            playVisualizer.stopListen();
        }
    }

    private void unbindService() {
        if (mServiceConnection != null && mServiceConnection.hasConnected) {
            mServiceConnection.unregisterListener();
            unbindService(mServiceConnection);
            mServiceConnection.hasConnected = false;
        }
    }

    private void unregisterReceiver() {
        if (themeChangeReceiver != null) {
            broadcastManager.unregisterReceiver(this, themeChangeReceiver);
        }

        if (songFavoriteChangeReceiver != null) {
            broadcastManager.unregisterReceiver(this, songFavoriteChangeReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
        visualizerFragment.stopSpin();
        viewsController.stopProgressUpdateTask();
        updateVisualizer();

    }

    private void savePreference() {

        try {
            Song song = control.currentSong();
            if (song == null) {
                return;
            }

            String path = song.path;

            int index = control.currentSongIndex();
            int pro = control.getProgress();
            int mode = control.getPlayMode();

            playPreference.updateLastPlaySong(new PlayPreference.CurrentSong(path, pro, index));
            playPreference.updatePlayMode(mode);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationController.visible()) {
            bottomNavigationController.hide();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (control != null) {
            updateCurrentSongInfo(null, true);
            updateViewsColorsIfNeed(null);
            updateVisualizer();
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param song   ??????????????????????????????????????? PlayActivity ??? ??? null ?????????????????????????????? songChanged ??????
     * @param isNext true ???????????????false???????????????????????????????????????????????????????????????
     */
    public void updateCurrentSongInfo(@Nullable Song song, boolean isNext) {

        if (song == null) {
            try {
                song = control.currentSong();
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }

        if (song == null) {
            return;
        }

        SongInfo info = mediaManager.getSongInfo(this, song);
        if (info == null) {
            return;
        }

        //????????????
        int pro = 0;
        try {
            pro = control.getProgress();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        int duration = (int) info.getDuration();
        int progress = pro;
        String name = info.getTitle();
        String arts = info.getArtist();
        viewsController.updateText(duration, progress, name, arts);

        //????????????
        updateStatus();
        bottomNavigationController.updatePlayMode();

        // ??????????????????????????????????????????????????????????????????
        boolean updateBg = playPreference.getTheme().equals(ThemeEnum.VARYING);
        visualizerFragment.songChanged(song, isNext, updateBg);

        //??? initViewsColors ????????????????????? updateViewsColorsIfNeed ?????????????????????
        // updateViewsColorsIfNeed ??????????????? VARYING ???????????????????????? VARYING ???
        // ????????????????????????????????????
        bottomNavigationController.updateFavorite();

        bottomNavigationController.update(null, null);

    }

    private void updateStatus() {

        try {
            boolean playing = control.status() == PlayController.STATUS_PLAYING;

            viewsController.updatePlayButtonStatus(playing);
            if (playing) {
                visualizerFragment.startSpin();
                viewsController.startProgressUpdateTask();
            } else {
                visualizerFragment.stopSpin();
                viewsController.stopProgressUpdateTask();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void songChanged(Song song, int index, boolean isNext) {
        if (song == null || index == -1) {
            return;
        }

        try {
            // UPDATE: 2017/8/26 ?????? ????????????????????????
            if (control.status() == PlayController.STATUS_PLAYING) {
                dbController.addSongPlayTimes(song);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        updateCurrentSongInfo(song, isNext);
        updateViewsColorsIfNeed(song);

    }

    @Override
    public void startPlay(Song song, int index, int status) {
        viewsController.startProgressUpdateTask();
        viewsController.updatePlayButtonStatus(true);
        visualizerFragment.startSpin();
        updateVisualizer();
    }

    @Override
    public void stopPlay(Song song, int index, int status) {
        viewsController.stopProgressUpdateTask();
        viewsController.updatePlayButtonStatus(false);
        visualizerFragment.stopSpin();
        updateVisualizer();

    }

    @Override
    public void onPlayListChange(Song current, int index, int id) {
        if (current == null || index < 0) {
            return;
        }

        bottomNavigationController.update(null, null);
    }

    @Override
    public void dataIsReady(IPlayControl mControl) {

    }

    // App ????????????
    @Override
    public void themeChange(ThemeEnum themeEnum, int[] colors) {
        bottomNavigationController.themeChange(null, null);
    }

    // ???????????? playPreference ???????????????
    private void initViewsColors() {

        ThemeEnum theme = playPreference.getTheme();
        if (theme != ThemeEnum.VARYING) {
            // VARYING ???????????? updateCurrentSongInfo ??? updateViewsColorsIfNeed ????????????????????????
            int colors[] = ColorUtils.get10ThemeColors(this, theme);

            int statusC = colors[0];
            int toolbarC = colors[1];
            int accentC = colors[2];
            int mainBC = colors[3];
            int vicBC = colors[4];
            int mainTC = colors[5];
            int vicTC = colors[6];
            int navC = colors[7];
            int toolbarMainTC = colors[8];
            int toolbarVicTC = colors[9];

            viewsController.updateColors(new int[]{mainBC, mainTC, vicBC, vicTC});
            bottomNavigationController.updateColors(vicBC, false);
            bgDrawableController.initBackgroundColor(mainBC);

        }

    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????<br>
     * ?????? updateCurrentSongInfo ????????????<br>
     * <p>
     * 0 ?????????????????? ????????????<br>
     * 1 ?????????????????? ??????????????????????????? ????????????<br>
     * 2 ?????????????????? ????????????<br>
     * 3 ?????????????????? ??????????????????????????? ????????????<br>
     */
    private void updateViewsColorsIfNeed(@Nullable Song song) {

        if (!playPreference.getTheme().equals(ThemeEnum.VARYING)) {
            return;
        }

        if (visualizerFragment == null) {
            return;
        }

        int[] colors = visualizerFragment.getCurrColors();

        int mainBC = colors[0];
        int mainTC = colors[1];
        int vicBC = colors[2];
        int vicTC = colors[3];

        viewsController.updateColors(new int[]{mainBC, mainTC, vicBC, vicTC});
        bottomNavigationController.updateColors(vicBC, true);

        Bitmap album = visualizerFragment.getAlbum();
        int defaultColor = getResources().getColor(R.color.default_play_text_color, null);
        int waveColor[] = new int[2];
        ColorUtils.get2ColorFormBitmap(album, defaultColor, waveColor);
        viewsController.updateWaveColors(waveColor[1], waveColor[0]);

        if (song == null) {
            try {
                song = control.currentSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (song != null) {
            bgDrawableController.updateBackground(mainBC, vicBC, mediaManager.getSongInfo(this, song));
        }

        // ????????? updateCurrentSongInfo ??????????????????????????????
        // ??? VARYING ????????????????????????
        bottomNavigationController.updateFavorite();
    }

    @Override
    public void permissionDenied(int requestCode) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_fragment_container:
                if (!bottomNavigationController.visible() && !bottomNavigationController.isAniming()) {
                    bottomNavigationController.show();
                }
                break;
            case R.id.play_name_container:
                try {
                    Song song = control.currentSong();
                    if (song != null) {
                        ActivityManager.getInstance().startSongDetailActivity(this, song, true);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void disConnected(ComponentName name) {
        mServiceConnection = null;
        mServiceConnection = new PlayServiceConnection(this, this, this);
        playServiceManager.bindService(mServiceConnection);
    }

}
