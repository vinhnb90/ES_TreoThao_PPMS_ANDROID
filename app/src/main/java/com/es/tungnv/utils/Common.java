package com.es.tungnv.utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by TUNGNV on 2/29/2016.
 */
public class Common {

    private static String PASSWORD_SECRET = "esolutions@123456";

    public static final String CODE_VERSION = "CPC";

    private static String version = "1.0";

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        Common.version = version;
    }

    private static char[] SPECIAL_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò',
            'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê',
            'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ', 'đ',
            'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ',
            'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ',
            'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế',
            'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị',
            'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ',
            'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ',
            'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự', };

    private static char[] REPLACEMENTS = {'A', 'A', 'A', 'A', 'E', 'E', 'E',
            'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a', 'a',
            'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A',
            'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I',
            'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', };

    public static float getMin(float x1, float x2) {
        if(x1 > x2)
            return x1;
        return x2;
    }
    
    public static String toUrlFriendly(String s) {
        int maxLength = Math.min(s.length(), 236);
        char[] buffer = new char[maxLength];
        int n = 0;
        for (int i = 0; i < maxLength; i++) {
            char ch = s.charAt(i);
            buffer[n] = removeAccent(ch);
            // skip not printable characters
            if (buffer[n] > 31) {
                n++;
            }
        }
        // skip trailing slashes
        while (n > 0 && buffer[n - 1] == '/') {
            n--;
        }
        return String.valueOf(buffer, 0, n);
    }

    public static char removeAccent(char ch) {

        int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
        if (index >= 0) {
            ch = REPLACEMENTS[index];
        }
        return ch;
    }

    public static String removeAccent(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static String GetIMEI(Context context) {
        try {
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String IMEI = tManager.getDeviceId();
            if (IMEI == null) {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                IMEI = (String) get.invoke(c, "ro.serialno");
            }

            return IMEI;
        } catch (Exception e) {
            return null;
        }
    }

    public static void showAlertDialogGreen(Context context, String title, int title_color, String content, int content_color, String button, int button_color){
        try{
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.invoice_dialog_thongbao);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);

            TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
            TextView tvBody = (TextView) dialog.findViewById(R.id.tvBody);
            LinearLayout lnButton = (LinearLayout) dialog.findViewById(R.id.lnButton);

            tvBody.setTextColor(content_color);
            tvTitle.setText(title);
            tvTitle.setTextColor(title_color);
            tvBody.setText(content);

            TextView tvClose = new TextView(context);
            tvClose.setText(button);
            tvClose.setTextColor(button_color);
            tvClose.setTypeface(null, Typeface.BOLD);
            tvClose.setPadding(10, 10, 20, 10);
            tvClose.setTextSize(20);
            tvClose.setGravity(Gravity.RIGHT);
            lnButton.setGravity(Gravity.RIGHT);
            lnButton.addView(tvClose, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            tvClose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);

            dialog.show();
        } catch(Exception ex) {

        }
    }

    public static String formatMoney(String money) {
        try {
            StringBuilder s = new StringBuilder();
            String ngan_cach_phan_nghin = " ";
            String ngan_cach_phan_thap_phan = ".";
            if (money.contains(".")) {
                String[] a = money.replace(".", ",").split(",");
                String tp0 = a[0].toString();
                String tp1 = a[1].toString();
                char [] c_money = tp0.toCharArray();
                int count_charactor = c_money.length % 3;
                if(count_charactor == 0)
                    count_charactor = 3;
                int count = 0;
                for (int i = 0; i < c_money.length; i++) {
                    count++;
                    if(count <= count_charactor){
                        s.append(c_money[i]);
                    } else {
                        s.append(ngan_cach_phan_nghin);
                        i--;
                        count = 0;
                        count_charactor = 3;
                    }
                }
                try{
                    if(Double.parseDouble(tp1) != 0){
                        s.append(ngan_cach_phan_thap_phan);
                        if (tp1.length() == 2) {
                            s.append(tp1);
                        } else if (tp1.length() == 1) {
                            s.append(tp1);
                            s.append("0");
                        } else if (tp1.length() >= 3) {
                            s.append(tp1.substring(0, 3));
                        }
                    }
                } catch(Exception ex) {
/**/
                }
            } else {
                char [] c_money = money.toCharArray();
                int count_charactor = c_money.length % 3;
                if(count_charactor == 0)
                    count_charactor = 3;
                int count = 0;
                for (int i = 0; i < c_money.length; i++) {
                    count++;
                    if(count <= count_charactor){
                        s.append(c_money[i]);
                    } else {
                        s.append(ngan_cach_phan_nghin);
                        i--;
                        count = 0;
                        count_charactor = 3;
                    }
                }
            }
            return s.toString();
        } catch (Exception ex) {
            ex.toString();
            return new DecimalFormat("##,##0.00").format(Double.parseDouble(money)).replace(",", " ");
        }
    }

    public static String formatFloatNumber(String number){
        try{
            if(number.contains(".")){
                if(Integer.parseInt(number.replace(".",",").split(",")[1].toString()) == 0){
                    return number.replace(".",",").split(",")[0].toString();
                }
            }
            return number;
        } catch (Exception ex) {
            ex.toString();
            return "0";
        }
    }

    public static boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

//    public static Bitmap cropImage(Bitmap img, Bitmap templateImage, int width, int height) {
//        // Merge two images together.
//        Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas combineImg = new Canvas(bm);
//        combineImg.drawBitmap(img, 0f, 0f, null);
//        combineImg.drawBitmap(templateImage, 0f, 0f, null);
//
//        // Create new blank ARGB bitmap.
//        Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//
//        // Get the coordinates for the middle of combineImg.
//        int hMid = bm.getHeight() / 2;
//        int wMid = bm.getWidth() / 2;
//        int hfMid = finalBm.getHeight() / 2;
//        int wfMid = finalBm.getWidth() / 2;
//
//        int y2 = hfMid;
//        int x2 = wfMid;
//
//        // Top half of the template.
//        for (int y = hMid; y >= 0; y--) {
//            boolean template = false;
//            // Check Upper-left section of combineImg.
//            for (int x = wMid; x >= 0; x--) {
//                if (x2 < 0) {
//                    break;
//                }
//
//                int px = bm.getPixel(x, y);
//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
//                    template = true;
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else if (template) {
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else {
//                    finalBm.setPixel(x2, y2, px);
//                }
//                x2--;
//            }
//
//            // Check upper-right section of combineImage.
//            x2 = wfMid;
//            template = false;
//            for (int x = wMid; x < bm.getWidth(); x++) {
//                if (x2 >= finalBm.getWidth()) {
//                    break;
//                }
//
//                int px = bm.getPixel(x, y);
//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
//                    template = true;
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else if (template) {
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else {
//                    finalBm.setPixel(x2, y2, px);
//                }
//                x2++;
//            }
//
//            // Once we reach the top-most part on the template line, set pixel value transparent
//            // from that point on.
//            int px = bm.getPixel(wMid, y);
//            if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
//                for (int y3 = y2; y3 >= 0; y3--) {
//                    for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
//                        finalBm.setPixel(x3, y3, Color.TRANSPARENT);
//                    }
//                }
//                break;
//            }
//            x2 = wfMid;
//            y2--;
//        }
//
//        x2 = wfMid;
//        y2 = hfMid;
//        // Bottom half of the template.
//        for (int y = hMid; y <= bm.getHeight(); y++) {
//            boolean template = false;
//            // Check bottom-left section of combineImage.
//            for (int x = wMid; x >= 0; x--) {
//                if (x2 < 0) {
//                    break;
//                }
//
//                int px = bm.getPixel(x, y);
//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
//                    template = true;
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else if (template) {
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else {
//                    finalBm.setPixel(x2, y2, px);
//                }
//                x2--;
//            }
//
//            // Check bottom-right section of combineImage.
//            x2 = wfMid;
//            template = false;
//            for (int x = wMid; x < bm.getWidth(); x++) {
//                if (x2 >= finalBm.getWidth()) {
//                    break;
//                }
//
//                int px = bm.getPixel(x, y);
//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
//                    template = true;
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else if (template) {
//                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
//                } else {
//                    finalBm.setPixel(x2, y2, px);
//                }
//                x2++;
//            }
//
//            // Once we reach the bottom-most part on the template line, set pixel value transparent
//            // from that point on.
//            int px = bm.getPixel(wMid, y);
//            if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
//                for (int y3 = y2; y3 < finalBm.getHeight(); y3++) {
//                    for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
//                        finalBm.setPixel(x3, y3, Color.TRANSPARENT);
//                    }
//                }
//                break;
//            }
//
//            x2 = wfMid;
//            y2++;
//        }
//        return finalBm;
//    }

    public static byte[] encodeTobase64Byte(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // Query bitmap without allocating memory
        options.inJustDecodeBounds = true;
        // decode file from path
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        // decode according to configuration or according best match
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            // if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
            // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        // if value is greater than 1,sub sample the original image
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static byte[] encodeTobyte(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static void SaveImage(String path, String name, Bitmap finalBitmap) {
        File file = new File (path, name);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void ConvertByteToFile(byte[] bloc, String filePath) {
        File photo = new File(filePath);

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());

            fos.write(bloc);
            fos.close();
        } catch (java.io.IOException e) {
            // String s = e.getMessage();
        }
    }

    public static byte[] FileToByteArray(String path) throws IOException {
        File f = new File(path);
        byte[] data = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(data);
        fis.close();
        return data;
    }

    public static String ConvertArray2String(String[] array){
        if(array == null || array.length == 0){
            return null;
        }
        String str = "";
        for(String item : array){
            str += item+",";
        }
        str = str.substring(0, str.lastIndexOf(","));
        return str;
    }

    public static List<String> GetSameItems(String[] arr1, String[] arr2){
        List<String> list = new ArrayList<String>();
        String[] arr_temp = arr2;
        for(String item_arr_1 : arr1){
            for(String item_arr_2 : arr_temp){
                String item1 = item_arr_1.toLowerCase(Locale.ENGLISH).trim();
                String item2 = item_arr_2.toLowerCase(Locale.ENGLISH).trim();
                if(item1.equals(item2)){
                    list.add(item_arr_1);
                    break;
                }
            }
        }

        return list;
    }

    public static List<String> ConvertArrayString2List(String[] array){
        List<String> list = new ArrayList<String>();
        for(String item : array){
            list.add(item);
        }
        return list;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static float MmToPixels(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value,
                context.getResources().getDisplayMetrics());
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Bitmap blurBitmap(Context context, Bitmap bitmap){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    public static void splitText(String str, StringBuilder s1, StringBuilder s2, float length_text){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        String[] sArr = str.split(" ");
        for (int i = 0; i < sArr.length; i++){
            Rect rect = new Rect();
            paint.getTextBounds(s1.toString(), 0, s1.toString().length(), rect);
            if(rect.width() < length_text){
                s1.append(sArr[i]).append(" ");
            } else {
                s2.append(sArr[i]).append(" ");
            }
        }
    }

    public static StringBuilder[] splitMultiText(String str, float lengthLine, float lengthText) {
        try {
            Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeCap(Paint.Cap.ROUND);
            paintText.setColor(EsspCommon.color);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(EsspCommon.width);
            paintText.setTextSize(EsspCommon.textSize);

            int soDongChan = (int) (lengthText / lengthLine);
            int soDongLe = lengthText % lengthLine > 0 ? 1 : 0;
            int soDong = soDongChan + soDongLe;

            int countSSB = 0;
            StringBuilder[] s = new StringBuilder[soDong + 1];
            for (int i = 0; i < s.length; i++) {
                s[i] = new StringBuilder();
            }
            String[] sArr = str.split(" ");
            for (int i = 0; i < sArr.length; i++) {
                Rect rect = new Rect();
                paintText.getTextBounds(s[countSSB].toString(), 0, s[countSSB].toString().length(), rect);
                if (rect.width() < lengthLine) {
                    s[countSSB].append(sArr[i]).append(" ");
                } else {
                    countSSB++;
                    s[countSSB].append(sArr[i]).append(" ");
                }
            }
            return s;
        } catch(Exception ex) {
            StringBuilder[] s = new StringBuilder[0];
            s[0] = new StringBuilder(str);
            return s;
        }
    }

    public static StringBuilder[] splitMultiTextTimes(String str, float lengthLine, float lengthText, Paint paintText) {
        int soDongChan = (int)(lengthText/lengthLine);
        int soDongLe = lengthText%lengthLine>0?1:0;
        int soDong = soDongChan + soDongLe;

        int countSSB = 0;
        StringBuilder[] s = new StringBuilder[soDong];
        for (int i = 0; i < s.length; i++){
            s[i] = new StringBuilder();
        }
        String[] sArr = str.split(" ");
        for (int i = 0; i < sArr.length; i++) {
            Rect rect = new Rect();
            paintText.getTextBounds(s[countSSB].toString(), 0, s[countSSB].toString().length(), rect);
            if (rect.width() < lengthLine) {
                s[countSSB].append(sArr[i]).append(" ");
            } else {
                countSSB++;
                s[countSSB].append(sArr[i]).append(" ");
            }
        }
        return s;
    }

    public static double roundTwoDecimals(double d) {
        return Math.round(d*100.0)/100.0;
    }

    public static String encryptPassword(String clearText) {
        try {
            byte[] encData_byte = new byte[clearText.length()];
            encData_byte = clearText.getBytes();
            String encodedData = Base64.encodeToString(encData_byte, Base64.DEFAULT);
            return encodedData;
        } catch (Exception e) {
            return "";
        }
    }

    public static String decryptPassword(String base64) {
        try {
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            return new String(data, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

}
