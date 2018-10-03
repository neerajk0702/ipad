package com.apitechnosoft.ipad.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.RegisterActivity;
import com.apitechnosoft.ipad.mail.Mail;
import com.apitechnosoft.ipad.model.SendMail;

import javax.mail.MessagingException;

import static com.apitechnosoft.ipad.utils.ASTUIUtil.showToast;

public class ContactUsFragment extends MainFragment {
    boolean buttonFlag = false;
    EditText edt_firstname, edt_lastname, edt_mail, edt_subject, edt_message;
    Button btnLogIn;

    String fname;
    String lname;
    String emailid;
    String from;
    String message, message1;

    String subject1;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected void loadView() {
        edt_firstname = this.findViewById(R.id.edt_firstname);
        edt_lastname = this.findViewById(R.id.edt_lastname);
        edt_mail = this.findViewById(R.id.edt_mail);
        edt_subject = this.findViewById(R.id.edt_subject);
        edt_message = this.findViewById(R.id.edt_message);
        btnLogIn = this.findViewById(R.id.btnLogIn);

    }

    @Override
    protected void setClickListeners() {
        btnLogIn.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogIn) {
            if (isValidate()) {
                new SendMail().execute("");

            }
        }
    }

    private class SendMail extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(), "Please wait", "Sending Mail", true, false);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (aVoid) {
                    edt_firstname.setText("");
                    edt_lastname.setText("");
                    edt_mail.setText("");
                    edt_subject.setText("");
                    edt_message.setText("");

                    Toast.makeText(getContext(), "Thanku for Contact us.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Something wrong.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            progressDialog.dismiss();
        }


        protected Boolean doInBackground(String... params) {
            boolean doneflag = false;

            message = "<h3>IPAD User Name <b> " + fname + " " + lname + " " + from + " </b><br> Contact's You <br> <br><br> " + message1 + "<br><br>";
            Mail m = new Mail("admin@rxdmedia.com", "Cowboys777!");
            String[] toArr = {"admin@rxdmedia.com"};//{"neerajk0702@gmail.com", "89neerajsingh@gmail.com"};
            m.setTo(toArr);
            m.setFrom("admin@rxdmedia.com");
            m.setSubject(subject1);
            m.setBody(message);
            try {
                if (m.send()) {
                    doneflag = true;
                } else {
                    doneflag = false;
                }
            } catch (Exception e) {
                //Log.e("MailApp", "Could not send email", e);
            }


            return doneflag;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        fname = edt_firstname.getText().toString();
        lname = edt_lastname.getText().toString();
        emailid = edt_mail.getText().toString();
        subject1 = edt_subject.getText().toString();
        message1 = edt_message.getText().toString();
        from = emailid;
        if (fname.length() == 0) {
            showToast("Please enter first name");
            return false;
        } else if (lname.length() == 0) {
            showToast("Please enter last name");
            return false;
        } else if (from.length() == 0) {
            showToast("Please enter Email Id");
            return false;
        } else if (!from.matches(emailRegex)) {
            showToast("Please enter valid Email ID");
            return false;
        } else if (subject1.length() == 0) {
            showToast("Please enter Subject");
            return false;
        } else if (message1.length() == 0) {
            showToast("Please enter Message");
            return false;
        }
        return true;
    }

}
