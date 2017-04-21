package kapil.voiceassistedweatherapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * This is a custom view for visual feedback during the SpeechRecognizer is in Listening Mode.
 */
public class VoiceListeningView extends View {
    private float radius;

    private Paint paint;
    private ValueAnimator radiusAnimator;

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
        radius = DpToPx(30);                  // dp
        float circleAction = DpToPx(10);      // dp

        int animationDuration = 800;          // milliseconds

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

    public float DpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void startAnim() {
        if (!radiusAnimator.isRunning()) {
            radiusAnimator.start();
        }
    }

    public void endAnim() {
        if (radiusAnimator.isRunning()) {
            radiusAnimator.end();
        }
    }
}