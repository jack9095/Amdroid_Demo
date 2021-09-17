package com.kuanquan.doyincover.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.facebook.common.internal.Closeables;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.TAG_ORIENTATION;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * Created by marko on 14-9-28.
 */
public class ImageUtils {

  public static final int DEFAULT_SIZE = 1000;
  public static final String JPEG_FILE_SUFFIX = ".jpg";
  public static final String ALBUM_NAME = "fish";
  private static final String JPEG_FILE_PREFIX = "IMG_";
  private static final String BASE64_URI_PREFIX = "base64,";
  private static final Pattern BASE64_IMAGE_URI_PATTERN = Pattern.compile("^(?:.*;)?base64,.*");

  private ImageUtils() {

  }

  @SuppressLint("NewApi") public static int getBitmapBytes(Bitmap bitmap) {
    int result;
    if (SDK_INT >= KITKAT) {
      result = bitmap.getAllocationByteCount();
    } else {
      result = bitmap.getByteCount();
    }
    if (result < 0) {
      throw new IllegalStateException("Negative size: " + bitmap);
    }
    return result;
  }


  public static InputStreamFactory createInputStreamFactory(final ContentResolver resolver,
      final Uri uri) {
    final String scheme = uri.getScheme();
    if ("data".equals(scheme)) {
      return new DataInputStreamFactory(resolver, uri);
    }
    return new BaseInputStreamFactory(resolver, uri);
  }

  public static Bitmap createBitmapByScale(Bitmap bitmap, float scale) {
    if (bitmap == null) {
      return null;
    }
    int w, h;
    w = bitmap.getWidth();
    h = bitmap.getHeight();
    if (w < 300 && h < 300) {
      return bitmap;
    }
    Matrix matrix = new Matrix();
    matrix.postScale((scale / w), (scale / h));
    return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
  }

  @Deprecated public static Bitmap getBitmapFromUri(Context context, Uri uri) {
    Bitmap bitmap = null;
    InputStream in = null;
    try {
      in = context.getContentResolver().openInputStream(uri);
      bitmap = BitmapFactory.decodeStream(in);
    } catch (FileNotFoundException e) {
      Log.e(String.valueOf(e), "get bitmap from uri");
    } finally {
      closeSilently(in);
    }
    return bitmap;
  }

  public static void closeSilently(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ignored) {
      }
    }
  }

  @Deprecated public static void copyFormBitmapToFile(Bitmap bitmap, String path) {
    File parentFile = (new File(path)).getParentFile();
    if (!parentFile.exists()) {
      parentFile.mkdirs();
    }

    if (bitmap != null) {
      File myCaptureFile = new File(path);
      try {
        if (myCaptureFile.exists()) {
          myCaptureFile.delete();
        }
        myCaptureFile.createNewFile();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      FileOutputStream fOut = null;
      try {
        fOut = new FileOutputStream(myCaptureFile);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      if (fOut != null) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut);
      }
      try {
        assert fOut != null;
        fOut.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        fOut.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static File convertBitmapToFile(Bitmap bitmap, File file) {
    if (bitmap == null) {
      return null;
    }
    File bitmapFile;
    if (file != null) {
      bitmapFile = file;
    } else {
      try {
        bitmapFile = createImageFile();
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
    byte[] bitmapdata = bos.toByteArray();

    // write the bytes in file
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(bitmapFile);
      fos.write(bitmapdata);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fos != null) {
        try {
          fos.flush();
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bitmapFile;
  }

  public static File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
    File albumF = getAlbumDir();
    return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
  }

  public static File getAlbumDir() {
    File storageDir = null;

    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

      storageDir = getAlbumStorageDir(getAlbumName());

      if (storageDir != null) {
        if (!storageDir.mkdirs()) {
          if (!storageDir.exists()) {
            return null;
          }
        }
      }
    }

    return storageDir;
  }

  private static File getAlbumStorageDir(String albumName) {
    return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        albumName);
  }

  private static String getAlbumName() {
    return ALBUM_NAME;
  }

  public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filename, options);
    // int[] reqSize = calReqSize(options.outWidth, options.outHeight,
    // reqWidth, reqHeight);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(filename, options);
  }

  /**
   * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
   * object when decoding bitmaps using the decode* methods from
   * {@link BitmapFactory}. This implementation calculates the closest
   * inSampleSize that will result in the final decoded bitmap having a width
   * and height equal to or larger than the requested width and height. This
   * implementation does not ensure a power of 2 is returned for inSampleSize
   * which can be faster when decoding but results in a larger bitmap which
   * isn't as useful for caching purposes.
   *
   * @param options An options object with out* params already populated (run
   * through a decode* method with inJustDecodeBounds==true
   * @param reqWidth The requested width of the resulting bitmap
   * @param reqHeight The requested height of the resulting bitmap
   * @return The value to be used for inSampleSize
   */
  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
      int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      // Calculate ratios of height and width to requested height and
      // width
      final int heightRatio = Math.round((float) height / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);

      // Choose the smallest ratio as inSampleSize value, this will
      // guarantee a final image
      // with both dimensions larger than or equal to the requested height
      // and width.
      inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;

      // This offers some additional logic in case the image has a strange
      // aspect ratio. For example, a panorama may have a much larger
      // width than height. In these cases the total pixels might still
      // end up being too large to fit comfortably in memory, so we should
      // be more aggressive with sample down the image (=larger
      // inSampleSize).

      final float totalPixels = width * height;

      // Anything more than 2x the requested pixels we'll sample down
      // further
      final float totalReqPixelsCap = reqWidth * reqHeight * 2;

      while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        inSampleSize++;
      }
    }
    return inSampleSize;
  }

  public static int getFileExifRotation(String filename) throws IOException {
    ExifInterface exifInterface = new ExifInterface(filename);
    int orientation = exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL);
    switch (orientation) {
      case ORIENTATION_ROTATE_90:
        return 90;
      case ORIENTATION_ROTATE_180:
        return 180;
      case ORIENTATION_ROTATE_270:
        return 270;
      default:
        return 0;
    }
  }


  /**
   * Utility class for when an InputStream needs to be read multiple times. For example, one pass
   * may load EXIF orientation, and the second pass may do the actual Bitmap decode.
   */
  public interface InputStreamFactory {

    /**
     * Create a new InputStream. The caller of this method must be able to read the input
     * stream starting from the beginning.
     */
    InputStream createInputStream() throws FileNotFoundException;
  }

  private static class BaseInputStreamFactory implements InputStreamFactory {
    protected final ContentResolver mResolver;
    protected final Uri mUri;

    BaseInputStreamFactory(final ContentResolver resolver, final Uri uri) {
      mResolver = resolver;
      mUri = uri;
    }

    @Override public InputStream createInputStream() throws FileNotFoundException {
      return mResolver.openInputStream(mUri);
    }
  }

  private static class DataInputStreamFactory extends BaseInputStreamFactory {
    private byte[] mData;

    DataInputStreamFactory(final ContentResolver resolver, final Uri uri) {
      super(resolver, uri);
    }

    @Override public InputStream createInputStream() throws FileNotFoundException {
      if (mData == null) {
        mData = parseDataUri(mUri);
        if (mData == null) {
          return super.createInputStream();
        }
      }
      return new ByteArrayInputStream(mData);
    }

    private byte[] parseDataUri(final Uri uri) {
      final String ssp = uri.getSchemeSpecificPart();
      try {
        if (ssp.startsWith(BASE64_URI_PREFIX)) {
          final String base64 = ssp.substring(BASE64_URI_PREFIX.length());
          return Base64.decode(base64, Base64.URL_SAFE);
        } else if (BASE64_IMAGE_URI_PATTERN.matcher(ssp).matches()) {
          final String base64 =
              ssp.substring(ssp.indexOf(BASE64_URI_PREFIX) + BASE64_URI_PREFIX.length());
          return Base64.decode(base64, Base64.DEFAULT);
        } else {
          return null;
        }
      } catch (IllegalArgumentException ex) {
        Log.e(String.valueOf(ex), "Illegal data URI");
        return null;
      }
    }
  }

  /**
   * 图片质量有损压缩
   *
   * @param originBytes， 原始图片字节大小
   * @param maxCompressSize，最终压缩大小不超过 maxCompressSize
   * @return byte[] 最终压缩图片大小
   */
  public static byte[] compressImage(byte[] originBytes, int maxCompressSize) {
    if (originBytes.length > maxCompressSize) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(originBytes, 0, originBytes.length);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      int quality = 99;
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
      while (outputStream.toByteArray().length > maxCompressSize) {
        outputStream.reset();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        quality--;
      }
      bitmap.recycle();
      byte[] result = outputStream.toByteArray();
      try {
        Closeables.close(outputStream, true);
      } catch (IOException e) {
        Log.e(String.valueOf(e), "compress image output stream close exception");
      }
      return result;
    }
    return originBytes;
  }


  /**
   * 图片按比例大小压缩方法
   *
   * @param image （根据Bitmap图片压缩）
   * @return
   */
  public static Bitmap compressScale(Bitmap image,float width,float height) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

    // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
    if (baos.toByteArray().length / 1024 > 1024) {
      baos.reset();// 重置baos即清空baos
      image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
    }
    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
    newOpts.inJustDecodeBounds = true;
    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
    newOpts.inJustDecodeBounds = false;
    int w = newOpts.outWidth;
    int h = newOpts.outHeight;
    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    int be = 1;// be=1表示不缩放
    if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
      be = (int) (newOpts.outWidth / width);
    } else if (w < h && h > height) { // 如果高度高的话根据高度固定大小缩放
      be = (int) (newOpts.outHeight / height);
    }
    if (be <= 0)
      be = 1;
    newOpts.inSampleSize = be; // 设置缩放比例
    // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565

    // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    isBm = new ByteArrayInputStream(baos.toByteArray());
    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

    return bitmap;// 压缩好比例大小后再进行质量压缩

    //return bitmap;
  }
}
