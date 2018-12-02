package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;

public class SelectPicture extends AppCompatActivity implements View.OnClickListener {

    ImageView iV_camera;
    ImageButton iB_back;
    ImageButton iB_continue;

    ImageView iV_topLeft;
    ImageView iV_topRight;
    ImageView iV_bottomLeft;
    ImageView iV_bottomRight;

    PuzzleInfo puzzleInfo = PuzzleInfo.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);

        // Get items from the layout
        iV_camera = (ImageView)findViewById(R.id.iV_camera);
        iV_topLeft = (ImageView)findViewById(R.id.iV_topLeft);
        iV_topRight = (ImageView)findViewById(R.id.iV_topRight);
        iV_bottomLeft = (ImageView)findViewById(R.id.iV_bottomLeft);
        iV_bottomRight = (ImageView)findViewById(R.id.iV_bottomRight);

        // Set items on click listener
        iV_camera.setOnClickListener(this);
        iV_topLeft.setOnClickListener(this);
        iV_topRight.setOnClickListener(this);
        iV_bottomLeft.setOnClickListener(this);
        iV_bottomRight.setOnClickListener(this);

        setNavigation();

        // Disable the button/imageView if the user has no camera
        if(!hasCamera())
            iV_camera.setEnabled(false);
    }

    //Check if the user has a camera
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    public void onClick(View v) {

        if(v == iV_camera) {
            // Change activity
            Intent intent = new Intent(this, CameraView.class);
            startActivity(intent);
            this.finish();

        }else if(v == iB_back){
            // Change activity
            Intent intent = new Intent(this, StartScreen.class);
            startActivity(intent);
            this.finish();

        }else if(v == iB_continue){
            // Change activity
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
            this.finish();

        } else if(v == iV_topLeft){
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            puzzleInfo.setiV_puzzlePicture((ImageView) v, "Bild 1");
            iV_topRight.setBackgroundResource(0);
            iV_bottomLeft.setBackgroundResource(0);
            iV_bottomRight.setBackgroundResource(0);

        } else if(v == iV_topRight){
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            puzzleInfo.setiV_puzzlePicture((ImageView) v, "Bild 2");
            iV_topLeft.setBackgroundResource(0);
            iV_bottomLeft.setBackgroundResource(0);
            iV_bottomRight.setBackgroundResource(0);

        } else if(v == iV_bottomLeft){
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            puzzleInfo.setiV_puzzlePicture((ImageView) v, "Bild 3");
            iV_topLeft.setBackgroundResource(0);
            iV_topRight.setBackgroundResource(0);
            iV_bottomRight.setBackgroundResource(0);

        } else if( v == iV_bottomRight){
            v.setBackgroundResource(R.drawable.shapes);
            // Set visibillity of continue button
            iB_continue.setVisibility(View.VISIBLE);
            puzzleInfo.setiV_puzzlePicture((ImageView) v, "Bild 4");
            iV_topLeft.setBackgroundResource(0);
            iV_topRight.setBackgroundResource(0);
            iV_bottomLeft.setBackgroundResource(0);

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
