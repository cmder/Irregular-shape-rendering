package io.agora.circlerender;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.agora.util.ShaderHelper;
import io.agora.util.TextResourceReader;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

@RuntimePermissions
public class MainActivity extends Activity {
    private static final int COORDS_PER_VERTEX = 3;

    private GLSurfaceView mGlSurfaceView;

    private FloatBuffer mVertexBuffer;

    private int mPositionHandle;
    private int mColorHandle;

    private int mProgram;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    // 顶点之间的偏移量
    // 每个顶点四个字节
    private final int vertexStride = 0;

    private float[] circleVerticesWithTriangles;
    private float radius = 1.0f;
    private int n = 360; // 切割份数
    private float height = 0.0f;

    private int mMatrixHandler;

    //设置颜色，依次为红绿蓝和透明通道
    private float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        mGlSurfaceView = new GLSurfaceView(this);

        circleVerticesWithTriangles = createPositions();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(circleVerticesWithTriangles.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(circleVerticesWithTriangles);
        mVertexBuffer.position(0);

        int vertexShader = ShaderHelper.compileVertexShader(TextResourceReader
                .readTextFileFromResource(this, R.raw.simple_vertex_shader));
        int fragmentShader = ShaderHelper.compileVertexShader(TextResourceReader
                .readTextFileFromResource(this, R.raw.simple_fragment_shader));

        mProgram = ShaderHelper.buildProgram()


        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                Constructor constructor = clazz.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                shape = (Shape) constructor.newInstance(mView);
                shape.onSurfaceCreated(gl, config);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                glViewport(0, 0, width, height);
                shape.onSurfaceChanged(gl, width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                glClear(GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                shape.onDrawFrame(gl);
            }
        });
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGlSurfaceView.requestRender();

        setContentView(mGlSurfaceView);

        MainActivityPermissionsDispatcher.initWithPermissionCheck(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void init() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             // 设置圆心坐标
        data.add(0.0f);
        data.add(height);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(height);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }
}
