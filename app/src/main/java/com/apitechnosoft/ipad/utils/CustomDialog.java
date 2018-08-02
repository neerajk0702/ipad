package com.apitechnosoft.ipad.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;


/**
 * /** * @author AST Inc.  2/11/2016.
 */
public class CustomDialog extends Dialog {
    Context context;
   /* EditText etActualHotelExp, etHotelExp;
    LinearLayout llActualHotelExp, llHotelExp;*/

    public CustomDialog(Context context) {
        super(context);
        context = this.context;
    }

    public void createDialog(final Context activityContext, String title, String body, String buttonText, String transitType) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(activityContext);
        // Include dialog.xml file
        dialog.setContentView(R.layout.custom_dialog);
        // Set dialog title
        dialog.setTitle(title);

        /*etActualHotelExp = (EditText)dialog.findViewById(R.id.etActualHotelExp);
        etHotelExp = (EditText)dialog.findViewById(R.id.etHotelExp);

        llActualHotelExp = (LinearLayout)dialog.findViewById(R.id.llActualHotelExp);
        llHotelExp = (LinearLayout)dialog.findViewById(R.id.llHotelExp);*/

        TextView tvBody = (TextView) dialog.findViewById(R.id.tvBody);
        tvBody.setText(body);

        if(!transitType.equals("4")){
            /*llActualHotelExp.setVisibility(View.GONE);
            llHotelExp.setVisibility(View.GONE);*/
            tvBody.setVisibility(View.VISIBLE);
            //llActualHotelExp.setVisibility(View.GONE);
        }else{

        }

        // set values for custom dialog components - text, image and button

        Button button = (Button) dialog.findViewById(R.id.button);
        button.setText(buttonText);

        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                /*Intent intentReload = new Intent(activityContext, TransitActivity.class);
                activityContext.startActivity(intentReload);*/
                dialog.dismiss();
            }
        });
    }
}
