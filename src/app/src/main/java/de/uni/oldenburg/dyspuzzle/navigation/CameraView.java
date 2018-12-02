package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.EventType;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.dataStructures.TelemetryEvent;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;

// used library:
// https://camerakit.io/
// https://github.com/CameraKit/camerakit-android
public class CameraView extends AppCompatActivity implements View.OnClickListener {

    private CameraKitView cameraView;

    ImageView iV_takePhoto;
    ImageView iV_photo;
    ImageButton iB_back;
    ImageButton iB_continue;

    boolean tookPhoto;

    TelemetryEvent telemetryEvent;
    TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        // set the navigation in the actionbar
        setNavigation();

        cameraView = findViewById(R.id.camera);
        iV_takePhoto = (ImageView)findViewById(R.id.iV_takePhoto);
        iV_photo = (ImageView)findViewById(R.id.iV_photo);

        cameraView.setVisibility(View.VISIBLE);
        iV_takePhoto.setVisibility(View.VISIBLE);
        iV_photo.setVisibility(View.GONE);

        tookPhoto = false;

        iV_takePhoto.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        cameraView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {

        if(v == iV_takePhoto){
            cameraView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView cameraKitView, byte[] imageBytes) {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    boolean success = savePhotoFile(bitmap);
                    if(!success)
                    {
                        Toast.makeText(CameraView.this, "Bild konnte nicht gespeichert werden!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    cameraView.onStop();
                    saveTookPhotoEvent();
                    showImage(bitmap);
                }
            });
        } else if(v == iB_back && tookPhoto == true){

            // Change activity
            Intent intent = new Intent(this, CameraView.class);
            startActivity(intent);
            this.finish();

        } else if(v == iB_back && tookPhoto == false){

            // Change activity
            Intent intent = new Intent(this, SelectPicture.class);
            startActivity(intent);
            this.finish();

        } else if(v == iB_continue){

            // safe the camera picture
            PuzzleInfo info = PuzzleInfo.getInstance();
            info.setiV_puzzlePicture(iV_photo, "Foto");

            // Change activity
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
            this.finish();
        }


    }

    private File getPhotoFile(){

        String filename = "photo.png";
        File destination = getPrivateAlbumStorageDir(CameraView.this,"");
        return new File(destination, filename);
    }

    private File getPrivateAlbumStorageDir(Context context, String albumName) {

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        return file;
    }

    private boolean savePhotoFile(Bitmap photo) {

        try {

            File photoFile = getPhotoFile();

            FileOutputStream outputStream = new FileOutputStream(photoFile);
            photo.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showImage(Bitmap bitmap) {

        cameraView.setVisibility(View.GONE);
        iV_takePhoto.setVisibility(View.GONE);
        iV_photo.setVisibility(View.VISIBLE);
        iB_continue.setVisibility(View.VISIBLE);

        tookPhoto = true;

        iV_photo.setImageBitmap(bitmap);
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
        iB_back = (ImageButton)view.findViewById(R.id.iB_back);

        // Set visibility of continue button
        iB_continue.setVisibility(View.GONE);

        // set width and height
        iB_back.getLayoutParams().height = i;
        iB_back.getLayoutParams().width = i;
        iB_back.setScaleType(ImageView.ScaleType.FIT_XY);

        iB_continue.getLayoutParams().height = i;
        iB_continue.getLayoutParams().width = i;
        iB_continue.setScaleType(ImageView.ScaleType.FIT_XY);

        // set on click listener
        iB_back.setOnClickListener(this);
        iB_continue.setOnClickListener(this);
    }

    private void saveTookPhotoEvent() {

        telemetryEvent = new TelemetryEvent(EventType.TOOKPHOTO);
        telemetryHandler.addEvent(telemetryEvent);
    }
}
