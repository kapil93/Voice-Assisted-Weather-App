package kapil.voiceassistedweatherapp.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import kapil.voiceassistedweatherapp.R;

/**
 * Created by kapil on 08-01-2017.
 */
public class VoiceListeningView extends View {
    private float radius;
    private float circleAction;

    private Paint paint;
    private ValueAnimator radiusAnimator;
    private int animationDuration;

    public VoiceListeningView(Context context) {
        super(context);
        init();
    }

    public VoiceListeningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoiceListeningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radius = pxToDp(30);
        circleAction = 20;       // px

        animationDuration = 800;        // milliseconds

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paint.setAlpha(99);

        radiusAnimator = new ValueAnimator();
        radiusAnimator.setFloatValues(radius, radius + circleAction, radius);
        radiusAnimator.setDuration(animationDuration);
        radiusAnimator.setRepeatMode(ValueAnimator.RESTART);
        radiusAnimator.setRepeatCount(ValueAnimator.INFINITE);
        radiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                radius = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radiusAnimator.isRunning()) {
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint);
        }
    }

    public float pxToDp(float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    public void startAnim() {
        if (!radiusAnimator.isRunning()) {
            radiusAnimator.start();
        }
    }

    public void pauseAnim() {
        if (radiusAnimator.isRunning()) {
            radiusAnimator.pause();
        }
    }

    public void endAnim() {
        if (radiusAnimator.isRunning()) {
            radiusAnimator.end();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        switch (visibility) {
            case VISIBLE:
                startAnim();
                break;
            case INVISIBLE:
                pauseAnim();
                break;
            case GONE:
                endAnim();
                break;
        }
    }
}
