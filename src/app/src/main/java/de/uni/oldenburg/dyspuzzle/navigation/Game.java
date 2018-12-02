package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.EventType;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.dataStructures.TelemetryEvent;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.CellType;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.LayoutCell;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.LayoutDetail;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.LayoutInfo;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.PuzzleLayoutGenerator;

public class Game extends AppCompatActivity implements View.OnClickListener, Observer {

    private LinearLayout mainLayout;
    private PuzzleLayoutGenerator puzzleGenerator;
    private PuzzleInfo  puzzleInfo = PuzzleInfo.getInstance();

    ImageButton iB_back;
    ImageButton iB_continue;

    TelemetryEvent telemetryEvent;
    TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();

    int orientation;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // save the time when the game starts
        saveGameStartEvent();

        // generate and show layouts for the puzzle field
        showLayout(getLayout());

        // add this class as a observer for the win attribute,
        // indicates if the puzzle is finished
        // if the value of this attribute changes, the activity is exited.
        puzzleInfo.addObserver(this);

        // set the navigation in the actionbar
        setNavigation();
    }

    private void saveGameStartEvent() {

        telemetryEvent = new TelemetryEvent(EventType.STARTGAME);
        telemetryHandler.addEvent(telemetryEvent);

    }

    @Override
    // leaves the activity when the back button is pressed
    public void onClick(View v) {

        if (v == iB_back) {
            // Change activity
            Intent intent = new Intent(this, SelectPicture.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    // generate and show layouts for the puzzle field
    private void showLayout(LayoutInfo info) {

        puzzleGenerator = new PuzzleLayoutGenerator(getApplicationContext());
        mainLayout = (LinearLayout) findViewById(R.id.rootLayout);
        puzzleGenerator.initializeLayout(mainLayout, info);
        puzzleGenerator.generateLayout(mainLayout, info);
    }

    private LayoutInfo getLayout(){

        LayoutInfo info = new LayoutInfo();
        orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            info.setOrientation(Configuration.ORIENTATION_LANDSCAPE);

            LayoutCell start = new LayoutCell(CellType.START, 6f);
            LayoutCell destination = new LayoutCell(CellType.DESTINATION, 6.01f);
            LayoutCell spacerOne = new LayoutCell(CellType.SPACER, 1f);
            LayoutCell spacerOneFive = new LayoutCell(CellType.SPACER, 1.5f);
            LayoutCell spacerTwo = new LayoutCell(CellType.SPACER, 2f);
            LayoutCell spacerTwoFive = new LayoutCell(CellType.SPACER, 2.5f);
            LayoutCell spacerThree = new LayoutCell(CellType.SPACER, 3f);
            LayoutCell spacerFour = new LayoutCell(CellType.SPACER, 4f);
            LayoutCell spacerEight = new LayoutCell(CellType.SPACER, 8f);

            if(puzzleInfo.getDegreeOfDifficulty() == 1){

                // Puzzle Layout for 2x2 Landscape
                info.setDetails(new LayoutDetail[]
                        {
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOneFive, destination, destination, spacerOneFive}),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOneFive, destination, destination, spacerOneFive}),
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(1f, null)
                        });
            } else if(puzzleInfo.getDegreeOfDifficulty() == 2) {

                // Puzzle Layout for 3x3 Landscape
                info.setDetails(new LayoutDetail[]
                        {
                                new LayoutDetail(2f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerThree, start, spacerFour, start, spacerThree}),
                                new LayoutDetail(2f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwo, destination, destination, destination, spacerTwo}),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwo, destination, destination, destination, spacerTwo}),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwo, destination, destination, destination, spacerTwo}),
                                new LayoutDetail(2f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerThree, start, spacerFour, start, spacerThree}),
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerEight, start, spacerOne}),
                                new LayoutDetail(2f, null)

                        });
            } else{

                // Puzzle Layout for 4x4 Landscape
                info.setDetails(new LayoutDetail[]
                        {
                                new LayoutDetail(2f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(2f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwoFive, destination, destination, destination, destination, spacerTwoFive}),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwoFive, destination, destination, destination, destination, spacerTwoFive}),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwoFive, destination, destination, destination, destination, spacerTwoFive}),
                                new LayoutDetail(10f, new LayoutCell[]{spacerTwoFive, destination, destination, destination, destination, spacerTwoFive}),
                                new LayoutDetail(2f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(1f, null),
                                new LayoutDetail(10f, new LayoutCell[]{spacerOne, start, spacerOne, start, spacerOne, start, spacerOne, start, spacerOne}),
                                new LayoutDetail(2f, null)

                        });
            }
            return info;
        }

        return null;
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

        // get ImageButton by id from the actionbar layout
        iB_back = (ImageButton)view.findViewById(R.id.iB_back);

        // set width and height
        iB_back.getLayoutParams().height = i;
        iB_back.getLayoutParams().width = i;
        iB_back.setScaleType(ImageView.ScaleType.FIT_XY);

        // set on click listener
        iB_back.setOnClickListener(this);
    }

    @Override
    // checks if the value of the observed attribute changes
    public void update(Observable o, Object arg) {
        boolean win = puzzleInfo.getWin();
        if(win){
            // if the value of the attribute is true, the puzzle is completed
            // the timer is called to leave this activity in one second
            iB_back.setClickable(false);
            callActivityTimer();
        }
    }

    // leave this activity in one second
    public synchronized void callActivityTimer(){

        // change the activity to the win activity
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Game.this, Win.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }
}