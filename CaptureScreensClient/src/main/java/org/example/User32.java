package org.example;

import net.java.dev.jna.Library;
import net.java.dev.Native;
import net.java.dev.platform.win32.User32;
public interface User32 extends Library {
    Main.User32 INSTANCE = (Main.User32) Native.loadLibrary("user32", Main.User32.class);
    boolean GetAsyncKeyState(int vKey);
}