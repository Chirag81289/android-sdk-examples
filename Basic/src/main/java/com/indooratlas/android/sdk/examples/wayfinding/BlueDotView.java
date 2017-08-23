package com.indooratlas.android.sdk.examples.wayfinding;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.indooratlas.android.sdk.examples.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Extends great ImageView library by Dave Morrissey. See more:
 * https://github.com/davemorrissey/subsampling-scale-image-view.
 */
public class BlueDotView extends SubsamplingScaleImageView {

    private float radius = 1.0f;
    private PointF dotCenter = null;
    private List<PointF> points;
    private float[] path;
    private PointF vPointOld = new PointF(0,0);

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDotCenter(PointF dotCenter) {
        this.dotCenter = dotCenter;
    }

    public BlueDotView(Context context) {
        this(context, null);
    }

    public BlueDotView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    private void initialise() {
        setWillNotDraw(false);
        setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER);
    }

    public void addDrawPoints(List<PointF> pointList) {
        points = new ArrayList<>(pointList);
        path = new float[pointList.size()*2];
    }

    private void clearDrawPoints() {
        points = null;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        if (dotCenter != null) {
            PointF vPoint = sourceToViewCoord(dotCenter);
            float scaledRadius = getScale() * radius;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.ia_blue));
            canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
        }
        if (points != null && !points.isEmpty()) {
            // If we have multiple points we want to draw, draw them here
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.ia_blue));

            Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(getResources().getColor(R.color.red));

            float scaledRadius = 0.5f * getScale() * radius;

            int iter = 0;
            for (PointF point : points) {
                PointF vPoint = sourceToViewCoord(point);

                canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
                if (iter > 0) {
                    canvas.drawLine(vPointOld.x, vPointOld.y, vPoint.x, vPoint.y, paint2);
                }
                iter += 1;
                vPointOld = vPoint;
            }
        }
    }
}
