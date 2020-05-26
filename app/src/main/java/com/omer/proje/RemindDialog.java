package com.omer.proje;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class RemindDialog extends AppCompatDialogFragment  {
    private ReminderDialogListener listener;
    private EditText time;
    private Switch notification;
    private Switch vibrate;
    private Switch alarm;
    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.activity_reminder_settings,null);

        builder.setView(view)
                .setTitle("Hatırlatıcı")
                .setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int t=Integer.parseInt(time.getText().toString());
                        boolean n=notification.isChecked();
                        boolean v=vibrate.isChecked();
                        boolean a=alarm.isChecked();
                        reminder r=new reminder(t,n,v,a);
                        listener.applyRemind(r);

                    }
                });
            time=view.findViewById(R.id.remindTime);
            notification=view.findViewById(R.id.notification);
            vibrate=view.findViewById(R.id.vibrate);
            alarm=view.findViewById(R.id.alarm);
            return builder.create();
        }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ReminderDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement ReminderDialogListener");
        }
    }


    public interface ReminderDialogListener {
        void applyRemind(reminder r);
    }
}
