package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;

public class DifficultyDegree extends AppCompatActivity implements View.OnClickListener{

    ImageView iV_easy;
    ImageView iV_middle;
    ImageView iV_hard;

    ImageButton iB_back;
    ImageButton iB_continue;

    PuzzleInfo puzzleInfo = PuzzleInfo.getInstance();
    ActionBar actionBar;
    int degreeOfDiffifculty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_degree);

        actionBar = getSupportActionBar();

        // Get items from the layout
        iV_easy = (ImageView)findViewById(R.id.iV_easy);
        iV_middle = (ImageView)findViewById(R.id.iV_middle);
        iV_hard = (ImageView)findViewById(R.id.iV_hard);

        // Set items on click listener
        iV_easy.setOnClickListener(this);
        iV_middle.setOnClickListener(this);
        iV_hard.setOnClickListener(this);

        setNavigation();
    }

    @Override
    public void onClick(View v) {

        if (v == iB_back){
            // Change activity
            Intent intent = getIntent();
            String activity = intent.getStringExtra("activity");

            if(activity.matches("StartScreen")){

                intent = new Intent(this, StartScreen.class);
                startActivity(intent);
                this.finish();

            } else{

                intent = new Intent(this, EnterData.class);
                startActivity(intent);
                this.finish();
            }


        }else if(v == iB_continue) {
            puzzleInfo.setDegreeOfDifficulty(degreeOfDiffifculty);
            // Change activity
            Intent intent = new Intent(this, StartScreen.class);
            startActivity(intent);
            this.finish();

        } else if (v == iV_easy) {
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            degreeOfDiffifculty = 1;
            iV_middle.setBackgroundResource(0);
            iV_hard.setBackgroundResource(0);

        } else if (v == iV_middle) {
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            degreeOfDiffifculty = 2;
            iV_easy.setBackgroundResource(0);
            iV_hard.setBackgroundResource(0);

        } else if (v == iV_hard) {
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            degreeOfDiffifculty = 3;
            iV_easy.setBackgroundResource(0);
            iV_middle.setBackgroundResource(0);

        }

    }

    private void setNavigation(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // get actionbar layout file
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout_back_continue);
        View view = getSupportActionBar().getCustomView();

        ContextInfo contextInfo = new ContextInfo(this);
        int i = (int) (contextInfo.getActionBarHeight() * 0.8);

        // get ImageButton by id from the actionbar layout
        iB_continue = (ImageButton)view.findViewById(R.id.iB_continue);

        // Set visibility of continue button
        iB_continue.setVisibility(View.GONE);

        // set width and height
        iB_continue.getLayoutParams().height = i;
        iB_continue.getLayoutParams().width = i;
        iB_continue.setScaleType(ImageView.ScaleType.FIT_XY);

        // set on click listener
        iB_continue.setOnClickListener(this);

        // get ImageButton by id from the actionbar layout
        iB_back = (ImageButton)view.findViewById(R.id.iB_back);

        // set width and height
        iB_back.getLayoutParams().height = i;
        iB_back.getLayoutParams().width = i;
        iB_back.setScaleType(ImageView.ScaleType.FIT_XY);

        // set on click listener
        iB_back.setOnClickListener(this);
    }
}
