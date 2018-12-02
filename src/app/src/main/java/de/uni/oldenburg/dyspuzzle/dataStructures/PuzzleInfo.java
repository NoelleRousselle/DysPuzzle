package de.uni.oldenburg.dyspuzzle.dataStructures;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.solver.widgets.Rectangle;
import android.text.format.Time;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Random;

import de.uni.oldenburg.dyspuzzle.handler.ImageHandler;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;

// singleton class to safe selected image and degree of difficulty
// implements the observer pattern
public class PuzzleInfo extends Observable {

    private ImageView iV_puzzlePicture;
    private String puzzlePictureId;
    private int degreeOfDifficulty = 1; // 1 = 2x2, 2 = 3x3, 3 = 4x4
    private Bitmap[] bitmaps;
    private Bitmap[] shuffledBitmaps;

    TelemetryEvent telemetryEvent;
    TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();

    // this attribute indicates if the puzzle is finished
    // observed by the game class
    private boolean win;

    private ImageHandler imageHandler = new ImageHandler();

    @SuppressLint("StaticFieldLeak")
    private static PuzzleInfo puzzleInfo = null;

    // a private constructor so no instances can be made outside this class
    private PuzzleInfo() {}

    //Everytime an instance is needed, call this function
    //synchronized to make the call thread-safe
    public static synchronized PuzzleInfo getInstance() {

        if(puzzleInfo == null)
            puzzleInfo = new PuzzleInfo();

        return puzzleInfo;
    }

    // generates a bitmap array with shuffled puzzle pieces
    // the rectangle object gives information about about the size of a puzzle field
    public void generatePuzzleContent(Rectangle rectangle){

        // convert imageView to Bitmap
        Bitmap bitmap =((BitmapDrawable)iV_puzzlePicture.getDrawable()).getBitmap();

        //get row and columns
        int rows = degreeOfDifficulty+1;
        int columns = degreeOfDifficulty+1;

        // cut the image to the correct size
        bitmap = imageHandler.cutBitmap(bitmap, rows, columns, rectangle);

        // create a bitmap array with all puzzle pieces
        bitmaps = imageHandler.createBitmaps(bitmap, rows, columns);

        // randomize entries of bitmaps to get shuffled puzzle pieces
        shuffledBitmaps = shuffleArray(bitmaps);

    }

    // shuffle the array
    private static Bitmap[] shuffleArray(Bitmap[] oldBitmaps){

        Bitmap[] newBitmaps = new Bitmap[oldBitmaps.length];

        // copy of the old bitmaps
        for(int i = 0; i < oldBitmaps.length; i++){
            newBitmaps[i] = oldBitmaps[i].copy(oldBitmaps[i].getConfig(), true);
        }

        Time time = new Time();
        time.setToNow();
        Random rnd = new Random(time.toMillis(false));
        for (int i = newBitmaps.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            Bitmap a = newBitmaps[index];
            newBitmaps[index] = newBitmaps[i];
            newBitmaps[i] = a;
        }

        return newBitmaps;
    }

    public Bitmap[] getShuffledBitmaps(){
        return shuffledBitmaps;
    }

    public ImageView getiV_puzzlePicture() {
        return iV_puzzlePicture;
    }

    public String getPuzzlePictureId() {
        return puzzlePictureId;
    }

    public void setPuzzlePictureId(String puzzlePictureId) {
        this.puzzlePictureId = puzzlePictureId;
    }

    public boolean getWin() {
        return win;
    }

    public void setiV_puzzlePicture(ImageView iV_puzzlePicture, String id) {
        this.iV_puzzlePicture = iV_puzzlePicture;
        this.puzzlePictureId = id;
        saveSelectPictureEvent();

    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int getDegreeOfDifficulty() {
        return degreeOfDifficulty;
    }

    public void setDegreeOfDifficulty(int degreeOfDifficulty) {
        this.degreeOfDifficulty = degreeOfDifficulty;
        saveSelectDifficultyDegreeEvent();
    }

    public void setWin(boolean win) {
        this.win = win;

        //notify observers of the value of the win attribute has changed
        setChanged();
        notifyObservers();
    }

    public boolean isWin() {
        return win;
    }

    private void saveSelectPictureEvent() {

        telemetryEvent = new TelemetryEvent(EventType.SELECTIMAGE);
        telemetryEvent.setInfo("Ausgewähltes Bild: " + puzzlePictureId);
        telemetryHandler.addEvent(telemetryEvent);
    }

    private void saveSelectDifficultyDegreeEvent() {

        telemetryEvent = new TelemetryEvent(EventType.SELECTDIFFICULTY);
        telemetryEvent.setDifficultyDegree(degreeOfDifficulty);
        telemetryHandler.addEvent(telemetryEvent);
    }
}


