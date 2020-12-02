package gb.myhomework.android1;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

    public class ConnectionForIcon implements Constants {

        private static final String TAG = "HW WebBrowser" + ConnectionForData.class.getSimpleName();

        public void fetchImage(final String idIcon, final ImageView iView) {

            String iUrl = generateRequestToServerForIcon(idIcon);

            if ( iUrl == null || iView == null )
                return;

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    final Bitmap image = (Bitmap) message.obj;
                    iView.setImageBitmap(image);
                }
            };

            final Thread thread = new Thread() {
                @Override
                public void run() {
                    final Bitmap image = downloadImage(iUrl);
                    if ( image != null ) {
                        if (Constants.DEBUG) {
                            Log.v(TAG, "Got image by URL: " + iUrl);
                        }
                        final Message message = handler.obtainMessage(1, image);
                        handler.sendMessage(message);
                    }
                }
            };
            iView.setImageResource(R.drawable.load);
            thread.setPriority(3);
            thread.start();
        }

        public static Bitmap downloadImage(String iUrl) {
            Bitmap bitmap = null;
            HttpURLConnection conn = null;
            BufferedInputStream buf_stream = null;
            try {
                if (Constants.DEBUG) {
                    Log.v(TAG, "Starting loading image by URL: " + iUrl);
                }
                conn = (HttpURLConnection) new URL(iUrl).openConnection();
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.connect();
                buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
                bitmap = BitmapFactory.decodeStream(buf_stream);
                buf_stream.close();
                conn.disconnect();
                buf_stream = null;
                conn = null;
            } catch (MalformedURLException ex) {
                Log.e(TAG, "Url parsing was failed: " + iUrl);
            } catch (IOException ex) {
                Log.e(TAG, iUrl + " does not exists");
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "Out of memory!!!");
                return null;
            } finally {
                if ( buf_stream != null )
                    try { buf_stream.close(); } catch (IOException ex) {}
                if ( conn != null )
                    conn.disconnect();
            }
            return bitmap;
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












