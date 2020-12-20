package gb.myhomework.android1.connection;

import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import gb.myhomework.android1.Constants;

public class ConnectionAndSetIcon {

    private static final String TAG = "HW WebBrowser" + ConnectionForData.class.getSimpleName();

    public void fetchImage(final String idIcon, final ImageView iView) {
        String iUrl = generateRequestToServerForIcon(idIcon);
        Picasso.get().load(iUrl).into(iView);
    }

    private String generateRequestToServerForIcon (String idIcon){
        String request;
        String startRequest = "https://openweathermap.org/img/wn/";
        String endRequest = "@2x.png";
        request = startRequest + idIcon + endRequest;
        if (Constants.DEBUG) {
            Log.v(TAG, "ICON request= " + request);
        }
        return request;
    }
}












