package com.example.wardrobex.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\u0018\u0010\f\u001a\u0004\u0018\u00010\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/example/wardrobex/utils/ImageUtils;", "", "()V", "MAX_DIMENSION", "", "deleteImage", "", "path", "", "resizeBitmap", "Landroid/graphics/Bitmap;", "bitmap", "saveImageToInternalStorage", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "app_debug"})
public final class ImageUtils {
    private static final int MAX_DIMENSION = 1024;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.wardrobex.utils.ImageUtils INSTANCE = null;
    
    private ImageUtils() {
        super();
    }
    
    /**
     * Copies a URI (from gallery/camera) into app's internal storage.
     * Returns the absolute file path, or null on failure.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String saveImageToInternalStorage(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    /**
     * Scales down a bitmap to [MAX_DIMENSION] on its longest side, preserving aspect ratio.
     */
    private final android.graphics.Bitmap resizeBitmap(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    /**
     * Deletes a clothing image from internal storage.
     */
    public final void deleteImage(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
    }
}