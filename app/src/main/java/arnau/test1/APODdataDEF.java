package arnau.test1;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;


public class APODdataDEF extends APODdata {

    private APODdata parsedAPOD;
    private Bitmap urlBitmap;

    public static APODdataDEF newDefinitiveAPOD(APODdata parsedAPOD,
                                                @Nullable Bitmap generatedBitmap) {
        APODdataDEF apod = new APODdataDEF();
        apod.parsedAPOD = parsedAPOD;
        apod.urlBitmap = generatedBitmap;
        return apod;
    }

    public APODdata getParsedAPOD() {
        return parsedAPOD;
    }

    public Bitmap getUrlBitmap() {
        return urlBitmap;
    }

}
