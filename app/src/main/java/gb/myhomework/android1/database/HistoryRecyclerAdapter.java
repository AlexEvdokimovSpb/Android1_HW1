package gb.myhomework.android1.database;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import gb.myhomework.android1.R;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{

    private Activity activity;
    private WeatherSource dataSource;
    private int count;

    public HistoryRecyclerAdapter(WeatherSource dataSource, Activity activity){
        this.dataSource = dataSource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<ResponseTheWeather> responseTheWeathers = dataSource.getResponseTheWeathers();
        ResponseTheWeather responseTheWeather = responseTheWeathers.get(position);
        holder.historyPlace.setText(responseTheWeather.place);
        holder.historyTemperature.setText(String.valueOf(responseTheWeather.temperature));
        holder.historyFeelsTemperature.setText(String.valueOf(responseTheWeather.feelsTemperature));
        holder.historyDateAndTime.setText(responseTheWeather.dateAndTime);
    }

    @Override
    public int getItemCount() {
        return (int)dataSource.getCountResponseTheWeathers();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView historyPlace;
        TextView historyTemperature;
        TextView historyFeelsTemperature;
        TextView historyDateAndTime;
        View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            historyPlace = cardView.findViewById(R.id.text_place_history);
            historyTemperature = cardView.findViewById(R.id.text_temperature_history);
            historyFeelsTemperature = cardView.findViewById(R.id.text_feelsTemperature_history);
            historyDateAndTime = cardView.findViewById(R.id.text_dateAndTime_history);
        }
    }
}
