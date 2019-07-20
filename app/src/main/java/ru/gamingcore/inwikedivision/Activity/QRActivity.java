package ru.gamingcore.inwikedivision.Activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.gamingcore.inwikedivision.MyService;
import ru.gamingcore.inwikedivision.PlanarYUVLuminanceSource;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.network.ServerWork;

import static android.hardware.camera2.CameraCharacteristics.LENS_FACING;
import static android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT;

public class QRActivity extends AppCompatActivity {
    private static final String TAG = "INWIKE";

    private static final int sImageFormat = ImageFormat.YUV_420_888;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private MyService service;


    private ImageReader mImageReader;
    private TextureView textureView;

    private String mCameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private QRCodeReader mQrReader = new QRCodeReader();
    private ImageReader imageReader;
    private ProgressBar progressBar;


    private ImageView imageView;

    private Timer timer;
    private TimerTask timerTask;


    final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    };

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };


    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "MainActivity onServiceConnected");
            service = ((MyService.LocalBinder)binder).getService();
            service.serverWork.setListener(listener);
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "MainActivity onServiceDisconnected");
            service = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        textureView = findViewById(R.id.texture);
        progressBar = findViewById(R.id.pb);
        imageView = findViewById(R.id.border);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();

        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            if (texture != null) {
                texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

                Surface surface = new Surface(texture);
                Surface mImageSurface = mImageReader.getSurface();

                List<Surface> outputSurfaces = new ArrayList<>(2);
                outputSurfaces.add(mImageSurface);
                outputSurfaces.add(surface);

                captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(surface);
                captureRequestBuilder.addTarget(mImageSurface);

                cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        if (null == cameraDevice) {
                            return;
                        }

                        cameraCaptureSessions = cameraCaptureSession;
                        updatePreview();
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        Toast.makeText(QRActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                    }
                }, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera(int width, int height) {
        setUpCameraOutputs(width, height);

        try {
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(QRActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                Toast.makeText(QRActivity.this, "requestPermissions", Toast.LENGTH_SHORT).show();

                return;
            }

            CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            if (manager != null)
                manager.openCamera(mCameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (manager != null) {
            try {
                for (String cameraId : manager.getCameraIdList()) {
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                    if (characteristics.get(LENS_FACING) == null || characteristics.get(LENS_FACING) == LENS_FACING_FRONT)
                        continue;

                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map != null) {
                        List<Size> outputSizes = Arrays.asList(map.getOutputSizes(sImageFormat));
                        Size largest = Collections.max(outputSizes, new CompareSizesByArea());

                        mImageReader = ImageReader.newInstance(largest.getWidth() / 16, largest.getHeight() / 16, sImageFormat, 2);
                        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                        imageDimension = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, largest);
                    }
                    mCameraId = cameraId;
                    break;
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "setUpCameraOutputs X");
        }
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), captureListener, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(QRActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image img = reader.acquireLatestImage();
                    Result rawResult = null;
                    // Log.d(TAG, "onImageAvailable");

                    try {
                        if (img == null) throw new NullPointerException("cannot be null");
                        ByteBuffer buffer = img.getPlanes()[0].getBuffer();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        int width = img.getWidth();
                        int height = img.getHeight();
                        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        rawResult = mQrReader.decode(bitmap);
                    } catch (ReaderException ignored) {
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } finally {
                        mQrReader.reset();
                        if (img != null)
                            img.close();
                    }
                    if (rawResult != null) {
                        readDB(rawResult.getText());
                    } else {
                        //Log.d(TAG, "No QR code found");
                    }
                }

            };

    public void readDB(String uid) {
        try {
            cameraCaptureSessions.stopRepeating();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressBar != null && progressBar.getVisibility() != View.VISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
                if(service!= null ) {
                    service.serverWork.connect(uid);
                }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startInfo() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
        finish();
    }

    private ServerWork.ServerListener listener = new ServerWork.ServerListener() {

        @Override
        public void onFinished(JSONObject obj) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(R.drawable.border_green);
                }
            });
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    startInfo();
                }
            };
            timer.schedule(timerTask, 200);
            service.jsonData.Parse(obj);
        }

        @Override
        public void onError() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(R.drawable.border_red);
                }
            });
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            };

            timer.schedule(timerTask, 1000);
        }
    };
}