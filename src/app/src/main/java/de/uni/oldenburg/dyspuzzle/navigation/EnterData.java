package de.uni.oldenburg.dyspuzzle.navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.Person;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;

public class EnterData extends AppCompatActivity implements View.OnClickListener {

    ImageButton iB_continue;
    EditText tB_age;
    CheckBox cB_male;
    CheckBox cB_female;
    CheckBox cB_lefthanded;
    CheckBox cB_righthanded;
    CheckBox cB_no;
    CheckBox cB_yes;
    ImageView iV_info;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        setNavigation();

        // Get items form the layout
        tB_age = (EditText)findViewById(R.id.tB_age);
        cB_male = (CheckBox)findViewById(R.id.cB_male);
        cB_female = (CheckBox)findViewById(R.id.cB_female);
        cB_lefthanded = (CheckBox)findViewById(R.id.cB_lefthanded);
        cB_righthanded = (CheckBox)findViewById(R.id.cB_righthanded);
        cB_no = (CheckBox)findViewById(R.id.cB_no);
        cB_yes = (CheckBox)findViewById(R.id.cB_yes);
        iV_info = (ImageView)findViewById(R.id.iV_info);

        // Set all items on click listener
        cB_male.setOnClickListener(this);
        cB_female.setOnClickListener(this);
        cB_lefthanded.setOnClickListener(this);
        cB_righthanded.setOnClickListener(this);
        cB_no.setOnClickListener(this);
        cB_yes.setOnClickListener(this);
        iV_info.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == iB_continue){

            // Check if all fields are filled
            if((!cB_male.isChecked() && !cB_female.isChecked())
                    || (!cB_lefthanded.isChecked() && !cB_righthanded.isChecked())
                    || (!cB_no.isChecked() && !cB_yes.isChecked())
                    || tB_age.getText().toString().matches("")){

                Toast.makeText(this, "Bitte füllen Sie alle Felder aus!", Toast.LENGTH_SHORT).show();

            } else{

                // Safe person infos
                person = Person.getInstance();
                person.setAge((Integer.parseInt(String.valueOf(tB_age.getText()))));

                if(cB_male.isChecked()){
                    person.setGender("male");
                } else{
                    person.setGender("female");
                }

                if(cB_lefthanded.isChecked()){
                    person.setPreferredHand("lefthanded");
                } else{
                    person.setPreferredHand("righthanded");
                }

                if(cB_no.isChecked()){
                    person.setDislexia(false);
                } else{
                    person.setDislexia(true);
                }

                // Safe the person instance in a file
                safePersonInfosInAFile();

                // Change activity
                Intent intent = new Intent(this, DifficultyDegree.class);
                intent.putExtra("activity","EnterData");
                startActivity(intent);
                this.finish();
            }

        }else if(v == cB_male){

            cB_male.setChecked(true);
            cB_female.setChecked(false);

        }else if(v == cB_female){

            cB_female.setChecked(true);
            cB_male.setChecked(false);

        }else if(v == cB_lefthanded && cB_lefthanded.isChecked()){

            cB_lefthanded.setChecked(true);
            cB_righthanded.setChecked(false);

        }else if(v == cB_righthanded && cB_righthanded.isChecked()){

            cB_righthanded.setChecked(true);
            cB_lefthanded.setChecked(false);

        }else if(v == cB_no && cB_no.isChecked()){

            cB_no.setChecked(true);
            cB_yes.setChecked(false);

        }else if(v == cB_yes && cB_yes.isChecked()) {

            cB_yes.setChecked(true);
            cB_no.setChecked(false);

        }else if(v == iV_info) {
            showDysInformation();
        }

    }

    public void safePersonInfosInAFile(){
        try {
            Gson gson = new Gson();

            // New personData file
            FileOutputStream fileOutputStream = openFileOutput("personData",MODE_PRIVATE);

            // Serialize person instance to a json object
            String personString = gson.toJson(person);

            // Write the json object in the file
            fileOutputStream.write(personString.getBytes());
            fileOutputStream.close();

            Toast.makeText(this, "Daten gespeichert!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDysInformation(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Lese-/Rechtschreibstörung");
        alertDialog.setMessage("Hinweise auf eine Lese-/Rechtschreibstörung sind z.B. " +
                "Schwierigkeiten Gelesenes zu verstehen oder gelesene Worte wieder zu " +
                "erkennen bzw. vorzulesen");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void setNavigation(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // get actionbar layout file
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout_back);
        View view = getSupportActionBar().getCustomView();

        ContextInfo contextInfo = new ContextInfo(this);
        int i = (int) (contextInfo.getActionBarHeight() * 0.8);

        // get ImageButton by id from the actionbar layout
        iB_continue = (ImageButton)view.findViewById(R.id.iB_continue);

        // set width and height
        iB_continue.getLayoutParams().height = i;
        iB_continue.getLayoutParams().width = i;
        iB_continue.setScaleType(ImageView.ScaleType.FIT_XY);

        // set on click listener
        iB_continue.setOnClickListener(this);

    }
}
