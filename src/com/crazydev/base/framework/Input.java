package com.crazydev.base.framework;

import java.util.List;

import com.crazydev.math.Vector2D;

public interface Input {
    public static class KeyEvent {
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;
        
        public static final int VOLUME_UP = android.view.KeyEvent.KEYCODE_VOLUME_UP;
        public static final int VOLUME_DOWN = android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
        public static final int VOLUME_MUTE = android.view.KeyEvent.KEYCODE_VOLUME_MUTE;
        
        public int type;
        public int keyCode;
        public char keyChar;

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (type == KEY_DOWN)
                builder.append("key down, ");
            else
                builder.append("key up, ");
            builder.append(keyCode);
            builder.append(",");
            builder.append(keyChar);
            return builder.toString();
        }
    }

    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;

        public int type;
        public int x, y;
        public int pointer;
        
        public Vector2D touchPosition = new Vector2D(); 

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (type == TOUCH_DOWN)
                builder.append("touch down, ");
            else if (type == TOUCH_DRAGGED)
                builder.append("touch dragged, ");
            else
                builder.append("touch up, ");
            builder.append(pointer);
            builder.append(",");
            builder.append(x);
            builder.append(",");
            builder.append(y);
            return builder.toString();
        }
    }

    public boolean isKeyPressed(int keyCode);

    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public float getAccelX();

    public float getAccelY();

    public float getAccelZ();

    public List<KeyEvent> getKeyEvents();

    public List<TouchEvent> getTouchEvents();
}