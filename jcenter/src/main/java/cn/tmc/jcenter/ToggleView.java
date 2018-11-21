package cn.tmc.jcenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Email: 76534779@qq.com
 * created by nbb on 2018/11/18 13
 * version: 1.0
 * description
 */
public class ToggleView extends View {

    private Bitmap mBackground;
    private Bitmap mButton;
    private Paint mPaint;
    private boolean mSwitchState = false;
    private boolean mIsSliding = false;

    public ToggleView(Context context) {
        super(context);
        initPaints();
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取配置的自定义属性
        if(attrs!=null) {
            String namespace = "http://schemas.android.com/apk/res-auto";
            int switch_background = attrs.getAttributeResourceValue(namespace, "switch_background", R.drawable.switch_background);
            int slide_button = attrs.getAttributeResourceValue(namespace, "slide_button", R.drawable.slide_button);
            boolean switch_state = attrs.getAttributeBooleanValue(namespace, "switch_state", false);
            setSwitchBackgroundResource(switch_background);
            setSlideButtonResource(slide_button);
            setSwitchState(switch_state);
        }
        initPaints();
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBackground.getWidth(), mBackground.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBackground, 0, 0, mPaint);
        //根据状态绘制图片
        if(mIsSliding){
            //让滑动时触摸滑块的中心点
            float dx=startX-mButton.getWidth()/2;
            int offsetX = mBackground.getWidth() - mButton.getWidth();
            if(dx<0){
                dx=0;
            }else if(dx>offsetX){
                dx=offsetX;
            }
            canvas.drawBitmap(mButton,dx,0,mPaint);
        }else {
            if (mSwitchState) {
                int dx = mBackground.getWidth() - mButton.getWidth();
                canvas.drawBitmap(mButton, dx, 0, mPaint);
            } else {
                canvas.drawBitmap(mButton, 0, 0, mPaint);
            }
        }
    }

    float startX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsSliding=true;
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                mIsSliding=false;
                startX = event.getX();
                int bgCenter = mBackground.getWidth() / 2;
                boolean state = startX > bgCenter;
                if(state!=mSwitchState && onSwitchStateChangeListener!=null){
                    if(state){
                        onSwitchStateChangeListener.onSwitchOn();
                    }else {
                        onSwitchStateChangeListener.onSwitchOff();
                    }
                }
                mSwitchState=state;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    /**
     * @param bgResId bg图片
     * @return void
     */
    public void setSwitchBackgroundResource(int bgResId) {
        mBackground = BitmapFactory.decodeResource(getResources(), bgResId);
    }

    /**
     * @param btnResid btn图片
     * @return void
     */
    public void setSlideButtonResource(int btnResid) {
        mButton = BitmapFactory.decodeResource(getResources(), btnResid);
    }

    public void setSwitchState(boolean switchState) {
        this.mSwitchState = switchState;
    }

    //开关状态变化回调
    public OnSwitchStateChangeListener onSwitchStateChangeListener;
    public interface OnSwitchStateChangeListener{
        void onSwitchOn();
        void onSwitchOff();
    }
    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener){
        this.onSwitchStateChangeListener=onSwitchStateChangeListener;
    }
}
