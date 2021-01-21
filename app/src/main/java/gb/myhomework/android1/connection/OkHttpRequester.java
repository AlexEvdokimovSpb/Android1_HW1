package gb.myhomework.android1.connection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.os.Handler;
import java.io.IOException;

public class OkHttpRequester {
    private OnResponseCompleted listener;

    public OkHttpRequester(OnResponseCompleted listener) {
        this.listener = listener;
    }

    public void run(String url) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            final Handler handler = new Handler();
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String answer = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCompleted(answer);
                    }
                });
            }
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    public interface OnResponseCompleted {
        void onCompleted(String content);
    }
}
