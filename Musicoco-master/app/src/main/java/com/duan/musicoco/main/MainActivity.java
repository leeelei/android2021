package com.duan.musicoco.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.duan.musicoco.R;
import com.duan.musicoco.aidl.IPlayControl;
import com.duan.musicoco.app.RootActivity;
import com.duan.musicoco.app.interfaces.ContentUpdatable;
import com.duan.musicoco.app.interfaces.OnServiceConnect;
import com.duan.musicoco.app.interfaces.OnUpdateStatusChanged;
import com.duan.musicoco.app.interfaces.ThemeChangeable;
import com.duan.musicoco.app.manager.ActivityManager;
import com.duan.musicoco.app.manager.BroadcastManager;
import com.duan.musicoco.app.manager.MediaManager;
import com.duan.musicoco.app.manager.PlayServiceManager;
import com.duan.musicoco.db.MainSheetHelper;
import com.duan.musicoco.main.bottomnav.BottomNavigationController;
import com.duan.musicoco.main.leftnav.LeftNavigationController;
import com.duan.musicoco.play.PlayActivity;
import com.duan.musicoco.play.PlayServiceConnection;
import com.duan.musicoco.preference.ThemeEnum;
import com.duan.musicoco.service.HeadphoneWireControlReceiver;
import com.duan.musicoco.service.PlayController;
import com.duan.musicoco.setting.AutoSwitchThemeController;
import com.duan.musicoco.util.ColorUtils;
import com.duan.musicoco.util.Utils;
import com.duan.musicoco.view.AppBarStateChangeListener;

public class MainActivity extends RootActivity implements
        OnServiceConnect,
        ThemeChangeable,
        ContentUpdatable {

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private Menu menu;

    // UPDATE: 2017/8/26 ?????? ????????????
    private static PlayServiceConnection sServiceConnection;
    private PlayServiceManager playServiceManager;
    protected MediaManager mediaManager;
    protected IPlayControl control;

    private BottomNavigationController bottomNavigationController;
    private LeftNavigationController leftNavigationController;
    private RecentMostPlayController mostPlayController;
    private MainSheetsController mainSheetsController;
    private MySheetsController mySheetsController;

    private BroadcastReceiver mySheetDataUpdateReceiver;
    private BroadcastReceiver appQuitTimeCountdownReceiver;
    private BroadcastReceiver appThemeChangeAutomaticReceiver;
    private BroadcastReceiver headsetPlugReceiver;
    private BroadcastReceiver mainSheetUpdateReceiver;
    private BroadcastManager broadcastManager;

    private boolean updateColorByCustomThemeColor = false;

    // ???????????????????????????????????????????????????
    private boolean justOpenTheApplication = true;

    private ComponentName headphoneWireControlReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.transitionStatusBar(this);

        playServiceManager = new PlayServiceManager(this);
        mediaManager = MediaManager.getInstance();

        // ??????????????? Context ??? MainActivity ???????????????????????????
        broadcastManager = BroadcastManager.getInstance();
        bottomNavigationController = new BottomNavigationController(this, mediaManager);
        leftNavigationController = new LeftNavigationController(this, appPreference, auxiliaryPreference);
        mostPlayController = new RecentMostPlayController(this, mediaManager);
        mainSheetsController = new MainSheetsController(this, mediaManager);
        mySheetsController = new MySheetsController(this, dbController, mediaManager);

        initViews();
        bindService();

    }

    private void initViews() {
        mySheetsController.initView();
        initSelfViews();
        bottomNavigationController.initView();
        mostPlayController.initView();
        mainSheetsController.initView();
        leftNavigationController.initViews();
    }

    private void bindService() {

        sServiceConnection = new PlayServiceConnection(bottomNavigationController, this, this);
        // ????????????????????? onConnected
        playServiceManager.bindService(sServiceConnection);

    }

    @Override
    public void onConnected(ComponentName name, IBinder service) {
        this.control = IPlayControl.Stub.asInterface(service);

        initSelfData();

        // ????????????????????????????????????
        update(null, null);

        // ??????????????????
        themeChange(null, null);

        //????????????
        initBroadcastReceivers();

        // ??????????????????????????????????????? ?
        initPlayStatus();

    }

    private void initPlayStatus() {

        if (settingPreference.openAutoPlay()) {
            try {

                if (control.currentSong() != null && control.status() != PlayController.STATUS_PLAYING) {
                    control.resume();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void initSelfData() {
        bottomNavigationController.initData(control, dbController);
        mostPlayController.initData(dbController);
        mainSheetsController.initData(dbController);
        mySheetsController.initData(control);
        leftNavigationController.initData(dbController);
    }

    @Override
    public void update(Object obj, OnUpdateStatusChanged statusChanged) {

        bottomNavigationController.update(obj, statusChanged);
        mostPlayController.update(getString(R.string.rmp_history), statusChanged);
        mainSheetsController.update(obj, statusChanged);
        mySheetsController.update(obj, statusChanged);
    }

    // ???????????????????????????????????? Controller ?????????
    @Override
    public void noData() {
    }

    private void initBroadcastReceivers() {
        mySheetDataUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mySheetsController.update(null, null);
            }
        };

        appQuitTimeCountdownReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(
                        BroadcastManager.Countdown.APP_QUIT_TIME_COUNTDOWN_STATUS,
                        BroadcastManager.Countdown.STOP_COUNTDOWN);
                if (status == BroadcastManager.Countdown.STOP_COUNTDOWN) {
                    leftNavigationController.stopQuitCountdown(true);
                } else {
                    leftNavigationController.startQuitCountdown();
                }
            }
        };

        appThemeChangeAutomaticReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int va = intent.getIntExtra(BroadcastManager.APP_THEME_CHANGE_AUTOMATIC_TOKEN, BroadcastManager.APP_THEME_CHANGE_AUTOMATIC_WHITE);
                ThemeEnum theme = va == BroadcastManager.APP_THEME_CHANGE_AUTOMATIC_WHITE ?
                        ThemeEnum.WHITE : ThemeEnum.DARK;
                appPreference.updateTheme(theme);
                switchThemeMode(theme);
            }
        };

        headsetPlugReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (justOpenTheApplication) {
                    justOpenTheApplication = false;
                    return;
                }
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) { // ????????????
                        try {
                            if (control.status() == PlayController.STATUS_PLAYING) {
                                control.pause();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else if (intent.getIntExtra("state", 0) == 1) { // ????????????

                    }
                }
            }
        };

        mainSheetUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bottomNavigationController.updateNotifyFavorite();
                mainSheetsController.update(null, null);
            }
        };

        broadcastManager.registerBroadReceiver(this, mainSheetUpdateReceiver, BroadcastManager.FILTER_MAIN_SHEET_UPDATE);
        broadcastManager.registerBroadReceiver(this, headsetPlugReceiver, BroadcastManager.FILTER_HEADSET_PLUG);
        broadcastManager.registerBroadReceiver(this, appQuitTimeCountdownReceiver, BroadcastManager.FILTER_APP_QUIT_TIME_COUNTDOWN);
        broadcastManager.registerBroadReceiver(this, appThemeChangeAutomaticReceiver, BroadcastManager.FILTER_APP_THEME_CHANGE_AUTOMATIC);
        broadcastManager.registerBroadReceiver(this, mySheetDataUpdateReceiver, BroadcastManager.FILTER_MY_SHEET_UPDATE);

        if (settingPreference.preHeadphoneWire()) {
            headphoneWireControlReceiver = new ComponentName(getPackageName(), HeadphoneWireControlReceiver.class.getName());
            ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).registerMediaButtonEventReceiver(headphoneWireControlReceiver);
        } else {
            headphoneWireControlReceiver = null;
        }

    }

    /**
     * ???????????????????????????
     */
    public void shutDownServiceAndApp() {
        // ?????????????????????
        bottomNavigationController.hidePlayNotify();

        // ?????? PlayActivity ???????????????????????????PlayActivity ???????????????????????????????????????
        Activity activity = ActivityManager.getInstance().getActivity(PlayActivity.class.getName());
        if (activity != null) {
            activity.finish();
        }

        // ???????????? ?????? ????????????
        finish();

    }

    private void initSelfViews() {

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        final View topContainer = findViewById(R.id.activity_main_top_container);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                // fitsSystemWindows ??? false ?????????????????? padding ???????????????
                toolbar.setPadding(0, Utils.getStatusBarHeight(MainActivity.this), 0, 0);
                // CollapsingToolbarLayout ?????? LinearLayout ??????????????? padding
                topContainer.setPadding(0, Utils.getStatusBarHeight(MainActivity.this), 0, 0);
            }
        });
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.activity_main_app_bar);
        AppBarStateChangeListener barStateChangeListener = new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case EXPANDED:
                        mySheetsController.setLineVisible(false);
                        break;
                    case COLLAPSED:
                        mySheetsController.setLineVisible(true);
                        break;
                    case IDLE:
                        mySheetsController.setLineVisible(false);
                        break;
                }
            }
        };
        appBarLayout.addOnOffsetChangedListener(barStateChangeListener);
    }

    //--------------------------------------------------------------------//--------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationController != null && bottomNavigationController.hasInitData()) {
            // ???????????????????????????????????? initSelfData ?????????????????????
            bottomNavigationController.update(null, null);
        }

        if (mySheetsController != null && mySheetsController.hasInitData()) {
            mySheetsController.update(null, null);
        }

        if (mainSheetsController != null && mainSheetsController.hasInitData()) {
            mainSheetsController.update(null, null);
        }

        if (mostPlayController != null && mostPlayController.hasInitData()) {
            mostPlayController.update(getString(R.string.rmp_history), null);
        }

        if (updateColorByCustomThemeColor) {
            themeChange(null, null);
            switchThemeMode(appPreference.getTheme());
            updateColorByCustomThemeColor = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService();

        // ????????????
        broadcastManager.sendBroadcast(this, BroadcastManager.FILTER_PLAY_SERVICE_QUIT, null);

        unregisterReceiver();
        bottomNavigationController.unregisterReceiver();

        auxiliaryPreference.setTimeSleepDisable();

        AutoSwitchThemeController instance = AutoSwitchThemeController.getInstance(this);
        if (settingPreference.autoSwitchNightTheme() && instance.isSet()) {
            instance.cancelAlarm();
        }

        // PlayService ?????? MediaPlayer ?????????????????????????????????????????????????????????????????????
        Process.killProcess(Process.myPid());
    }

    private void unregisterReceiver() {
        if (mySheetDataUpdateReceiver != null) {
            broadcastManager.unregisterReceiver(this, mySheetDataUpdateReceiver);
        }

        if (appQuitTimeCountdownReceiver != null) {
            broadcastManager.unregisterReceiver(this, appQuitTimeCountdownReceiver);
        }

        if (appThemeChangeAutomaticReceiver != null) {
            broadcastManager.unregisterReceiver(this, appThemeChangeAutomaticReceiver);
        }

        if (headsetPlugReceiver != null) {
            broadcastManager.unregisterReceiver(this, headsetPlugReceiver);
        }

        if (mainSheetUpdateReceiver != null) {
            broadcastManager.unregisterReceiver(this, mainSheetUpdateReceiver);
        }

        if (headphoneWireControlReceiver != null) {
            ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).unregisterMediaButtonEventReceiver(headphoneWireControlReceiver);
        }

    }

    private void unbindService() {
        if (sServiceConnection != null && sServiceConnection.hasConnected) {
            sServiceConnection.unregisterListener();
            unbindService(sServiceConnection);
            sServiceConnection.hasConnected = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (leftNavigationController.onBackPressed()) {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bottomNavigationController != null) {
            bottomNavigationController.stopProgressUpdateTask();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        this.menu = menu;
        updateToolbarColors();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_main_search:
                int sheetID = MainSheetHelper.SHEET_ALL;
                ActivityManager.getInstance().startSearchActivity(this, sheetID);
                break;
            case android.R.id.home:
                if (leftNavigationController.visible()) {
                    leftNavigationController.hide();
                } else {
                    leftNavigationController.show();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void disConnected(ComponentName name) {
        sServiceConnection = null;
        sServiceConnection = new PlayServiceConnection(bottomNavigationController, this, this);
        // ????????????
        playServiceManager.bindService(sServiceConnection);
    }

    @Override
    public void themeChange(ThemeEnum t, int[] colors) {
        ThemeEnum themeEnum = appPreference.getTheme();
        bottomNavigationController.themeChange(themeEnum, null);
        mostPlayController.themeChange(themeEnum, null);
        mySheetsController.themeChange(themeEnum, null);
        updateToolbarColors();
    }

    // ?????????????????????
    private void updateToolbarColors() {
        if (toolbar == null || toggle == null) {
            return;
        }

        int[] colors = ColorUtils.get2ToolbarTextColors(this);

        int mainTC = colors[0];
        toolbar.setTitleTextColor(mainTC);
        toggle.getDrawerArrowDrawable().setColor(mainTC);

        if (menu != null) {
            MenuItem search = menu.findItem(R.id.action_main_search);
            if (search != null) {
                Drawable icon = search.getIcon();
                if (icon != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        icon.setTint(mainTC);
                    }
                }
            }
        }

        CollapsingToolbarLayout coll = (CollapsingToolbarLayout) findViewById(R.id.activity_main_coll_tool_bar);
        int[] cs = ColorUtils.get2ActionStatusBarColors(this);
        coll.setStatusBarScrimColor(cs[0]);
        coll.setContentScrimColor(cs[1]);

        toolbar.setBackgroundColor(cs[1]);

        // ?????????????????????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(cs[0]);
        }
    }

    /**
     * ???????????????????????????????????????
     */
    public void switchThemeMode(final ThemeEnum theme) {
        int[] colors = ColorUtils.get10ThemeColors(this, theme);
        int to = colors[3];

        View view = getWindow().getDecorView();
        view.setBackgroundColor(to);
        leftNavigationController.themeChange(theme, null);
        themeChange(null, null);

    }

    /**
     * ??????????????????????????????
     * LeftNavigationController??????????????????????????????
     * ????????????????????????????????????
     */
    public void updateColorByCustomThemeColor() {
        this.updateColorByCustomThemeColor = true;
    }

    public static IPlayControl getControl() {
        return sServiceConnection == null ? null : sServiceConnection.takeControl();
    }

}
