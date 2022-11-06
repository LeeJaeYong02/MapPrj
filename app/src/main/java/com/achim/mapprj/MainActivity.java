package com.achim.mapprj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView mainWebView;
    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadActionEvent();
    }

    private void loadActionEvent() {
        mainWebView = (WebView) findViewById(R.id.mainWebView);

        mainWebView.setWebViewClient(new MyWebViewClient());

        WebSettings mainWebViewSettings = mainWebView.getSettings();
        mainWebViewSettings.setJavaScriptEnabled(true); // 자바스크립트 사용여부
        mainWebViewSettings.setDomStorageEnabled(true); // localStorage 사용을 위해
        mainWebViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);  // 자바스크립트가 창을 자동으로 열 수 있게할지 여부
        mainWebViewSettings.setLoadsImagesAutomatically(true); // 이미지 자동 로드
        mainWebViewSettings.setUseWideViewPort(true); // wide viewport 설정
        mainWebViewSettings.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클때 스크린크기에 맞추기
        mainWebViewSettings.setSupportZoom(false); // 줌설정
        mainWebViewSettings.setBuiltInZoomControls(false); // 줌 컨트롤 제거
        mainWebViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 캐시설정
        mainWebViewSettings.setAllowFileAccess(true); // 웹뷰 내에서 파일 액세스 활성화 여부

        mainWebViewSettings.setUserAgentString(mainWebViewSettings.getUserAgentString() + " AndroidApp");

        serverUrl = getString(R.string.web_server_protocol) + "://" + getString(R.string.web_server_ip) + ":" + getString(R.string.web_server_port);
        mainWebView.loadUrl(serverUrl);

    }

    /**
     * 페이지 탐색 처리 참고 : https://developer.android.com/guide/webapps/webview#HandlingNavigation
     * 사용자가 WebView에서 웹페이지의 링크를 클릭하면 URL을 처리하는 앱이
     * Android에서 실행되는 것이 기본 동작입니다. 대개 기본 웹브라우저에 도착 URL이 열리고 로드됩니다.
     * 하지만 링크가 WebView 내에서 열리도록 WebView의 이 동작을 재정의할 수 있습니다.
     * 그러면 WebView에 의해 유지 관리되는 웹페이지 방문 기록을 통해 사용자가 앞뒤로 탐색할 수 있습니다.
     *
     * WebView에서 location.href 동작 시 외부 브라우저앱을 호출하는 문제로 인해 정의하였습니다.
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (getString(R.string.web_server_ip).equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}