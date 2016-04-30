package br.ufms.vagner.agenda.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import java.io.File;

import br.ufms.vagner.agenda.R;

public class BitmapUtils {

    public BitmapUtils() {

    }

    public static Bitmap getBitmapFromImgString(String img, Context context) {
        if (img == null || img.equals("") || !new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getApplicationContext().getPackageName() + "/images/" + img).exists()) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.user);
        } else {
            return BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getApplicationContext().getPackageName() + "/images/" + img).getAbsolutePath(), new BitmapFactory.Options());
        }
    }

    private static int getResourceId(String pVariableName, String pResourcename, String pPackageName, Context context) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
