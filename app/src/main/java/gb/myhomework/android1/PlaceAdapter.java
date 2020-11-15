package gb.myhomework.android1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceAdapter extends RecyclerView.Adapter <PlaceAdapter.ViewHolder> implements Constants {

    public static final String TAG = "HW "+ PlaceAdapter.class.getSimpleName();

    private String[] dataSource;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public PlaceAdapter(String[] dataSource){
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        if (Constants.DEBUG) {
            Log.v(TAG, "ViewHolder create");
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (Constants.DEBUG) {
            Log.v(TAG, "onBindViewHolder");
        }

        holder.setData(dataSource[position]);
    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public void setData(String text) {
            textView.setText(text);
        }
    }
}
