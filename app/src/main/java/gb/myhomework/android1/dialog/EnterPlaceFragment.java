package gb.myhomework.android1.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import gb.myhomework.android1.Constants;
import gb.myhomework.android1.R;

public class EnterPlaceFragment extends DialogFragment {
    public static final String TAG = "HW "+ EnterPlaceFragment.class.getSimpleName();
    private TextInputEditText editText;
    private String newPlace;
    private OnDialogListener onDialogListener;
    private OnDialogListener activity;

    public static EnterPlaceFragment newInstance() {
        return new EnterPlaceFragment();
    }

    public void setOnDialogListener(OnDialogListener onDialogListener){
        this.onDialogListener = onDialogListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_city, container, false);
        view.findViewById(R.id.button_confirm_entry).setOnClickListener(listener);
        editText = (TextInputEditText) view.findViewById(R.id.entry_place2);
        setCancelable(false);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            newPlace = editText.getText().toString();
            onDialogListener = (OnDialogListener) getParentFragment();
            activity = (OnDialogListener)getActivity();

            if (onDialogListener instanceof OnDialogListener) {
                onDialogListener.onGetString(newPlace);
            }
            if (activity instanceof OnDialogListener) {
                activity.onGetString(newPlace);
            }
            Toast.makeText(getContext(), String.format("Введено: %s", editText
                                .getText().toString()), Toast.LENGTH_SHORT)
                                .show();
            dismiss();
            if (Constants.DEBUG) {
                Log.v(TAG, "EnterPlaceFragment dismiss");
                Log.v(TAG, "with "+ newPlace);
            }
        }
    };
}



