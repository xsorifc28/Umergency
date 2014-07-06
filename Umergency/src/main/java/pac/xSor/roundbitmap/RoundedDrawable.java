package pac.xSor.roundbitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class RoundedDrawable extends Drawable
{
    private static final int DIMENSION = 240;
    private int radius;
    private RectF rect;
    Paint paint;

    public RoundedDrawable (Bitmap bitmap, int radius)
    {
        this.radius = radius;
        bitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, true);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        rect = new RectF(0f, 0f, radius, radius);
    }

    @Override
    public void setAlpha(int alpha) {
        if (paint.getAlpha() != alpha) {
            paint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawRoundRect(rect, radius, radius, paint);
    }
}