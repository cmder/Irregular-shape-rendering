package io.agora.circlerender.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.agora.circlerender.R;
import io.agora.circlerender.util.MyCameraManager;
import io.agora.circlerender.widget.CircleRendererView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends Activity {
    private Context mContext;

    private CircleRendererView mCircleRendererView;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private MyCameraManager mMyCameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityPermissionsDispatcher.initWithPermissionCheck(this);

        setContentView(R.layout.activity_main);

        mCircleRendererView = findViewById(R.id.circleRendererView);
        mMyCameraManager = new MyCameraManager(this);
        if (!mMyCameraManager.openCamera(mCameraId)) {
            return;
        }
        mCircleRendererView.init(mMyCameraManager, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCircleRendererView.destroy();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void init() {
        mContext = this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
