package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;

public class PrivacyStatement extends AppCompatActivity implements View.OnClickListener {

    TextView tV_agb;
    ImageButton iB_continue;
    CheckBox cB_acceptAgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_agb);

        setNavigation();

        // Get Items from the layout
        tV_agb = (TextView)findViewById(R.id.tV_agb);
        cB_acceptAgb = (CheckBox)findViewById(R.id.cB_acceptAgb);

        // Add a scrollbar to the texfield
        tV_agb.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    public void onClick(View v) {

        // Check if the checkbox for accepting the AGB's is enabled
        if (v == iB_continue && cB_acceptAgb.isChecked()) {

            // change activity
            Intent intent = new Intent(this, EnterData.class);
            startActivity(intent);
            this.finish();

        }else if(v == iB_continue){

            // if the data privacy statement has not been accepted,
            // the user will be notified of this
            Toast.makeText(this, "Bitte akzeptiere unsere Datenschutzerklärung!",
                    Toast.LENGTH_SHORT).show();
        }
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
