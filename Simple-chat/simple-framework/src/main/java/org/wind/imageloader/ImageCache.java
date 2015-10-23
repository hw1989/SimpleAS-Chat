package org.wind.imageloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCache {
	private static ImageCache cache = null;
	private LruCache<String, Bitmap> lruCache = null;

	private ImageCache() {
		int size = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
		lruCache = new LruCache<String, Bitmap>(size / 4) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public static ImageCache init() {
		if (cache == null) {
			cache = new ImageCache();
		}
		return cache;
	}

	public LruCache<String, Bitmap> getCache() {
		return lruCache;
	}
}
