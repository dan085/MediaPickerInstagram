package me.ningsk.mediascanlibrary.widget.cropper.cropview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CropView extends View implements ICropView {


    private static final CopyType sDefaultCropType = CopyType.SQUARE;

    protected DisplayHelper mDisplayHelper;
    private CopyType mCropType;
    private GestureDetector mClickGestureDetector;
    private OnClickListener mOnClickListener;
    private boolean mIsAdvancedMode;

    public CropView(Context context) {
        this(context, null);
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayHelper = new DisplayHelper(context, this);
        mClickGestureDetector = new GestureDetector(context, new ClickGestureDetector());
        mCropType = sDefaultCropType;


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //use "|" deliberately
        return mClickGestureDetector.onTouchEvent(event) | mDisplayHelper.processTouchEvent(event);
    }


    protected void resetCropArea() {
        updateCropArea();
    }

    protected void resetMatrix() {
        resetCropArea();
        mDisplayHelper.resetMatrix();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mDisplayHelper.draw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateCropArea();
    }

    private void updateCropArea() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width == 0 || height == 0) {
            return;
        }
        //compute cropArea
        float left, rigth, top, bottom;
        float finalWidth = 0;
        float finalHeight = 0;
        switch (mCropType) {
            case SQUARE:
                finalWidth = finalHeight = width < height ? width : height;
                break;
            case SCALE_2_3:
                finalHeight = height;
                finalWidth = finalHeight * 2 / 3;
                break;
            case SCALE_3_4:
                finalHeight = height;
                finalWidth = finalHeight * 3 / 4;
                break;
            case SCALE_9_16:
                finalHeight = height;
                finalWidth = finalHeight * 9 / 16;
                break;
            case SCALE_3_2:
                finalWidth = width;
                finalHeight = finalWidth * 2 / 3;
                break;
            case SCALE_4_3:
                finalWidth = width;
                finalHeight = finalWidth * 3 / 4;
                break;
            case SCALE_16_9:
                finalWidth = width;
                finalHeight = finalWidth * 9 / 16;
                break;

        }
        left = (width - finalWidth) * 0.5f;
        top = (height - finalHeight) * 0.5f;
        rigth = left + finalWidth;
        bottom = top + finalHeight;

        RectF cropArea = new RectF(left, top, rigth, bottom);
        mDisplayHelper.setCropArea(cropArea);
    }

    @Override
    public boolean isFillMode() {
        return mDisplayHelper.isFillMode();
    }

    @Override
    public void setFillMode(boolean isFillMode) {
        mDisplayHelper.setIsFillMode(isFillMode);
    }



    @Override
    public void setCropType(CopyType type) {
        if (mCropType != type) {
            mCropType = type;
            updateCropArea();
        }
    }

    @Override
    public void setIsAdvancedMode(boolean isAdvancedMode) {
        mIsAdvancedMode = isAdvancedMode;
        mDisplayHelper.setIsAdvancedMode(isAdvancedMode);
    }

    public boolean isAdvancedMode() {
        return mIsAdvancedMode;
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        mDisplayHelper.setBitmap(bitmap);
    }

    public void  setBitmapFit(Bitmap srcBitmap, Matrix baseMatrix, Matrix drawMatrix, Matrix suppMatrix, float scaleFocusX, float scaleFocusY) {
        mDisplayHelper.setBitmapFit(srcBitmap, baseMatrix, drawMatrix, suppMatrix, scaleFocusX, scaleFocusY);
    }

    @Override
    public void setImageDrawable(Drawable d) {
        Bitmap bm = null;
        if (d != null) {
            if (d instanceof BitmapDrawable) {
                bm = ((BitmapDrawable) d).getBitmap();
            } else {
                bm = drawableToBitmap(d);
            }
        }
        mDisplayHelper.setBitmap(bm);
    }

    public void getCroppedBitmap(CropperCallback callback) {
        mDisplayHelper.getCroppedBitmap(callback);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bm = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bm;
    }


    @Override
    public void setImageResource(@DrawableRes int resId) {
        mDisplayHelper.setBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    @Override
    public void setImageUri(Uri uri) {
        if (uri == null) {
            mDisplayHelper.setBitmap(null);
        } else {
            mDisplayHelper.setBitmap(BitmapFactory.decodeFile(uri.getPath()));
        }
    }

    @Override
    public void setImageFile(String path) {
        mDisplayHelper.setBitmap(BitmapFactory.decodeFile(path));
    }

    @Override
    public void setOnFillableChangeListener(OnFillableChangeListener l) {
        mDisplayHelper.setOnFillableChangeListener(l);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }


    private class ClickGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(CropView.this);
                return true;
            }
            return false;
        }
    }

    protected Matrix getDrawableMatrix() {
        return mDisplayHelper.getDrawMatrix();
    }

    public Matrix getBaseMatrix() {
        return mDisplayHelper.getBaseMatrix();
    }

    public float getScaleFocusX() {
        return mDisplayHelper.getScaleFocusX();
    }

    public float getScaleFocusY() {
        return mDisplayHelper.getScaleFocusY();
    }

    public Matrix getSuppMatrix() {
        return mDisplayHelper.getSuppMatrix();
    }

    protected Rect getShowRect() {
        return mDisplayHelper.getShowRect();
    }

}
