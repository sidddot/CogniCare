package com.example.testingalz;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddActivityDialog extends DialogFragment {

    private EditText activityEditText;
    private TimePicker timePicker;
    private Button setAlarmButton;
    private OnActivityAddedListener listener;

    public interface OnActivityAddedListener {
        void onActivityAdded(String activity, int hour, int minute);
    }

    public void setListener(OnActivityAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Activity");

        final View view = getActivity().getLayoutInflater().inflate(R.layout.activity_dialog, null);
        builder.setView(view);

        activityEditText = view.findViewById(R.id.activityEditText);
        timePicker = view.findViewById(R.id.timePicker);
        setAlarmButton = view.findViewById(R.id.setAlarmButton);

        setAlarmButton.setOnClickListener(v -> {
            String activity = activityEditText.getText().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            if (listener != null) {
                listener.onActivityAdded(activity, hour, minute);
                dismiss();
            }
        });

        return builder.create();
    }
}