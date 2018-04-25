package arnau.test1;

import android.graphics.Bitmap;


public class APODdataDEF extends APODdata {

    private APODdata parsedAPOD;
    private Bitmap urlBitmap;

    public APODdata getParsedAPOD() {
        return parsedAPOD;
    }

    public static APODdataDEF newDefinitiveAPOD(APODdata parsedAPOD, Bitmap generatedBitmap) {
        APODdataDEF apod = new APODdataDEF();
        apod.parsedAPOD = parsedAPOD;
        apod.urlBitmap = generatedBitmap;
        return apod;
    }

    public Bitmap getUrlBitmap() {
        return urlBitmap;
    }

}
