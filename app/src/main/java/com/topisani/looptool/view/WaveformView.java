package com.topisani.looptool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.topisani.looptool.misc.LimitedSizeQueue;

import java.io.IOException;

public class WaveformView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    Thread t = null;
    SurfaceHolder holder;
    boolean run = false;
    final int scale = 2;
    Paint mLinePaint;
    MediaRecorder mRecorder;
    LimitedSizeQueue<Float> points;
    double amp = 0;
    float maxMeasuredAmp = 0;
    float xscale = 8;

    public WaveformView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mRecorder = new MediaRecorder();
        points = new LimitedSizeQueue<>((int) (getWidth()/xscale));
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setColor(Color.BLACK);

        holder = getHolder();
        holder.addCallback(this);
    }

    public float getAmplitude() {
        double maxamp = mRecorder.getMaxAmplitude();
        if (mRecorder != null) {
            amp = (maxamp > 0 ) ? (maxamp) : amp;
        }
        else {
            return 0;
        }
        return (float) amp;
    }

    @Override
    public void run() {
        while (run) {
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas = drawWaveform(canvas);
            Log.d("", "run: true");
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private Canvas drawWaveform(Canvas c) {
        float height = getHeight()/2;
        float width = getWidth();
        float xoffset = width - ( points.size() * xscale);
        float waveHeight = (float) (height * 0.75);
        float amp = getAmplitude();
        points.add(amp);
        if (amp > this.maxMeasuredAmp ) this.maxMeasuredAmp = amp;
        for (int i = 0; i < points.size() -1; i++) {
            float y     = waveHeight - ( waveHeight / ( ( points.get(  i  ) / this.maxMeasuredAmp ) + 1 ) );
            float yNext = waveHeight - ( waveHeight / ( ( points.get(i + 1) / this.maxMeasuredAmp ) + 1 ) );
            float x     = xoffset + (    i    * xscale);
            float xNext = xoffset + ( i + 1 ) * xscale;
            c.drawLine(x, height - y, xNext, height - yNext, mLinePaint);
            c.drawLine(x, height + y, xNext, height + yNext, mLinePaint);
        }
        mLinePaint.setTextSize(60F);
        c.drawText(String.valueOf(getAmplitude()), 100F, 200F, mLinePaint);
        return c;
    }
    public void pause() {
        run = false;
        while(true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume() {
        run = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        points = new LimitedSizeQueue<>((int) (getWidth()/xscale));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }
}