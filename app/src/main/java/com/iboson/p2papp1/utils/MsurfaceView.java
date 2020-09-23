package com.iboson.p2papp1.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.iboson.p2papp1.R;


public class MsurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private TutorialThread _thread;

    public MsurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        _thread = new TutorialThread(getHolder(), this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap _scratch = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_foreground);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(_scratch, 10, 10, null);

    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        _thread.setRunning(true);
        _thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        _thread.setRunning(false);
        while (retry) {
            try {
                _thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private MsurfaceView _panel;
        private boolean _run = false;

        public TutorialThread(SurfaceHolder surfaceHolder, MsurfaceView panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.draw(c);
                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}