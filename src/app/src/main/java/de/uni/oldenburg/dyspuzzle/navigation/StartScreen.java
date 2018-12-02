package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.Device;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;

public class StartScreen extends AppCompatActivity implements View.OnClickListener {

    ImageView iV_changeData;
    ImageView iV_difficultyDegree;
    ImageView iV_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // get actionbar layout file
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);


        // Get items form the layout
        iV_changeData = (ImageView)findViewById(R.id.iV_changeData);
        iV_difficultyDegree = (ImageView)findViewById(R.id.iV_difficultyDegree);
        iV_play = (ImageView) findViewById(R.id.iV_play);

        // Set all items on click listener
        iV_changeData.setOnClickListener(this);
        iV_difficultyDegree.setOnClickListener(this);
        iV_play.setOnClickListener(this);

        PuzzleInfo puzzleInfo = PuzzleInfo.getInstance();
        int degreeOfDifficulty = puzzleInfo.getDegreeOfDifficulty();

        // set resource of the difficultyDegree button
        if(degreeOfDifficulty == 1){
            iV_difficultyDegree.setImageResource(R.drawable.difficulty2x2);
        } else if(degreeOfDifficulty == 2){
            iV_difficultyDegree.setImageResource(R.drawable.difficulty3x3);
        } else if(degreeOfDifficulty == 3){
            iV_difficultyDegree.setImageResource(R.drawable.difficulty4x4);
        }

        setTitle("DysPuzzle: ");
    }

    @Override
    public void onClick(View v) {

        if(v == iV_changeData){

            // Change activity
            Intent intent = new Intent(this,ChangeData.class);
            startActivity(intent);
            this.finish();

        } else if(v == iV_play){
            saveRootLayoutInformation();

            // Change activity
            Intent intent = new Intent(this,SelectPicture.class);
            startActivity(intent);
            this.finish();

        } else if(v == iV_difficultyDegree){

            // Change activity
            Intent intent = new Intent(this,DifficultyDegree.class);
            intent.putExtra("activity","StartScreen");
            startActivity(intent);
            this.finish();
        }
    }

    private void saveRootLayoutInformation(){

        // get offset/origin of the root layout
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.rootLayout);
        int[] coordinates = new int[2];
        mainLayout.getLocationOnScreen(coordinates);

        // save offset/origin of the root layout
        TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();
        telemetryHandler.setRootLayoutOrigin(coordinates);
        Device device = Device.getInstance();
        device.setmRootLayoutOrigin(coordinates);

    }
}
