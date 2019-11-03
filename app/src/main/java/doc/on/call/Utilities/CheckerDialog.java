package doc.on.call.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import doc.on.call.R;

public class CheckerDialog extends Dialog {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public CheckerDialog(final Context context) {
        super(context);
        builder = new AlertDialog.Builder(context);
        // Add the buttons
        builder.setTitle(R.string.alert_checker_title);
        builder.setMessage(R.string.alert_checker_message);
        builder.setPositiveButton(R.string.alert_checker_btn_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button, close the application by exiting all activities
                ((Activity) context).finishAffinity();
            }
        });
        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // User clicked outside of dialog bounds, close the application by exiting all activities
                        ((Activity) context).finishAffinity();
                    }
                });
    }

    public void DisplayDialog() {
        this.dialog = this.builder.create();
        this.dialog.show();
    }



}