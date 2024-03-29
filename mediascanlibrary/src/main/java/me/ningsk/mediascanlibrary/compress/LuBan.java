package me.ningsk.mediascanlibrary.compress;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.util.Log;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author：luck
 * project：PictureSelector
 * email：893855882@qq.com
 * data：16/12/31
 */

public class LuBan {
    public static final int FIRST_GEAR = 1; // 一档
    public static final int THIRD_GEAR = 3; // 三档
    public static final int CUSTOM_GEAR = 4;// 四档

    private static final String TAG = "LuBan";
    private static String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

    private File mFile;

    private List<File> mFileList;

    private LuBanBuilder mBuilder;

    private LuBan(File cacheDir) {
        mBuilder = new LuBanBuilder(cacheDir);
    }

    public static LuBan compress(Context context, File file) {
        LuBan luban = new LuBan(LuBan.getPhotoCacheDir(context));
        luban.mFile = file;
        luban.mFileList = Collections.singletonList(file);

        return luban;
    }

    public static LuBan compress(Context context, List<File> files) {
        LuBan luban = new LuBan(LuBan.getPhotoCacheDir(context));
        luban.mFileList = files;
        luban.mFile = files.get(0);
        return luban;
    }

    /**
     * 自定义压缩模式 FIRST_GEAR、THIRD_GEAR、CUSTOM_GEAR
     *
     * @param gear
     * @return
     */
    public LuBan putGear(@GEAR int gear) {
        mBuilder.gear = gear;
        return this;
    }

    /**
     * 自定义图片压缩格式
     *
     * @param compressFormat
     * @return
     */
    public LuBan setCompressFormat(Bitmap.CompressFormat compressFormat) {
        mBuilder.compressFormat = compressFormat;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大体积
     *
     * @param size
     * @return
     */
    public LuBan setMaxSize(int size) {
        mBuilder.maxSize = size;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大宽度
     *
     * @param width 最大宽度
     * @return
     */
    public LuBan setMaxWidth(int width) {
        mBuilder.maxWidth = width;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大高度
     *
     * @param height 最大高度
     * @return
     */
    public LuBan setMaxHeight(int height) {
        mBuilder.maxHeight = height;
        return this;
    }

    /**
     * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
     *
     * @param listener 接收回调结果
     */
    public void launch(final OnCompressListener listener) {

        asObservable().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onStart();
            }

            @Override
            public void onNext(File file) {
                listener.onSuccess(file);
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
     *
     * @param listener 接收回调结果
     */
    public void launch(final OnMultiCompressListener listener) {
//        asListObservable().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
//                .doOnRequest(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        listener.onStart();
//                    }
//                })
//                .subscribe(new Action1<List<File>>() {
//                    @Override
//                    public void call(List<File> files) {
//                        listener.onSuccess(files);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        listener.onError(throwable);
//                    }
//                });

        asListObservable().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<File>>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onStart();
            }

            @Override
            public void onNext(List<File> files) {
                listener.onSuccess(files);
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {
            }
        });

    }

    /**
     * 返回File Observable
     *
     * @return
     */
    public Observable<File> asObservable() {
        LuBanCompressor compressor = new LuBanCompressor(mBuilder);
        return compressor.singleAction(mFile);
    }

    /**
     * 返回fileList Observable
     *
     * @return
     */
    public Observable<List<File>> asListObservable() {
        LuBanCompressor compressor = new LuBanCompressor(mBuilder);
        return compressor.multiAction(mFileList);
    }

    // Utils

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store
     * retrieved media and thumbnails.
     *
     * @param context A context.
     * @see #getPhotoCacheDir(Context, String)
     */
    private static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, LuBan.DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store
     * retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getPhotoCacheDir(Context)
     */
    private static File getPhotoCacheDir(Context context, String cacheName) {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    /**
     * 清空LuBan所产生的缓存
     * Clears the cache generated by LuBan
     *
     * @return
     */
    public LuBan clearCache() {
        if (mBuilder.cacheDir.exists()) {
            deleteFile(mBuilder.cacheDir);
        }
        return this;
    }

    /**
     * 清空目标文件或文件夹
     * Empty the target file or folder
     */
    private void deleteFile(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File file : fileOrDirectory.listFiles()) {
                deleteFile(file);
            }
        }
        fileOrDirectory.delete();
    }

    @IntDef({FIRST_GEAR, THIRD_GEAR, CUSTOM_GEAR})
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @Inherited
    @interface GEAR {

    }
}