package set.fabsolution.mwk;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onesignal.OneSignal;


public class Home extends AppCompatActivity {

    private WebView mWebView;
    private SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // OneSignal Initialization
        OneSignal.startInit(Home.this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        setContentView(R.layout.activity_home);






pullToRefresh=findViewById(R.id.pull);
    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MediaPlayer song = MediaPlayer.create(getApplicationContext(), R.raw.pullrefresh);
                song.start();
                refreshData();
//                 SoundPool sounds;
//                 int sExplosion;
//                //declare variables
//                sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
//                sExplosion = sounds.load(getApplicationContext(), R.raw.pullrefresh, 1);
//                sounds.play(sExplosion, 1.0f, 1.0f, 0, 0, 1.5f);



                pullToRefresh.setRefreshing(false);
            }
        });



        //Web View
        mWebView = findViewById(R.id.myWebView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        String dbPath = this.getApplicationContext().getDir("database", getApplicationContext().MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dbPath);

        //improve WebView performance
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        /*if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/


        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);

        //force links open in webview only
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("https://m0569760745.wixsite.com/sdr-news");
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            if (Uri.parse(url).getHost().contains("mwk-khanfar.blogspot.com/?m=1")) {
//                return false;
//            } else {
//                //here open external links in external app or browser
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;
//            }
            return false;
        }

        //progress Dialogue
        ProgressDialog progressDialog = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressDialog = new ProgressDialog(Home.this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();
        }

    }

    //goto previous page when press back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void refreshData() {

        mWebView.loadUrl("https://m0569760745.wixsite.com/sdr-news");


    }

}
