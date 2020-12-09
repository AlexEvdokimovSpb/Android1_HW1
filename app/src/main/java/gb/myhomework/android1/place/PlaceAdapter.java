package gb.myhomework.android1.place;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gb.myhomework.android1.Constants;
import gb.myhomework.android1.R;

public class PlaceAdapter extends RecyclerView.Adapter <PlaceAdapter.ViewHolder> {

    public static final String TAG = "HW "+ PlaceAdapter.class.getSimpleName();

    private CityDataSource dataSource;
    public PlaceAdapter(CityDataSource dataSource){
        this.dataSource = dataSource;
        if (Constants.DEBUG) {
            Log.v(TAG, "dataSource=" + dataSource);
        }
    }

    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        if (Constants.DEBUG) {
            Log.v(TAG, "Place ViewHolder create");
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        City city = dataSource.getCity(position);
        viewHolder.setData(city.getDescription(), city.getPicture());
        if (Constants.DEBUG) {
            Log.v(TAG, "onBindViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        if (Constants.DEBUG) {
            Log.v(TAG, "getItemCount= "+dataSource.size());
        }
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }

        public void setData(String description, int picture){
            getImageView().setImageResource(picture);
            getTextView().setText(description);
        }
        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
    }
}
