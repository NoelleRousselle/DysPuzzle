package de.uni.oldenburg.dyspuzzle.handler;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.uni.oldenburg.dyspuzzle.dataStructures.EventType;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.dataStructures.TelemetryEvent;

// class to handle the puzzle events
public class PuzzleHandler {

    private LinearLayout mMainLayout;

    private boolean mIgnoreEvents = false;

    private PuzzleInfo puzzleInfo = PuzzleInfo.getInstance();
    private Bitmap[] bitmapSolution = puzzleInfo.getBitmaps();

    private TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();

    public PuzzleHandler(LinearLayout mainLayout){
        this.mMainLayout = mainLayout;
    }

    /**
     * Event handler for the completed drag/drop operation
     * https://developer.android.com/reference/android/view/DragEvent
     */
    public View.OnDragListener onDragDropListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            if(mIgnoreEvents){
                return false;
            }

            try {

                View view = (View) event.getLocalState();
                int viewId = view.getId();

                int[] coordinates = new int[2];
                v.getLocationOnScreen(coordinates);
                float posX = coordinates[0] + event.getX();
                float posY = coordinates[1] + event.getY();

                switch (event.getAction()) {

                    // Sent to a View after ACTION_DRAG_ENTERED while the object is still within
                    // the View object's bounding box, but not within a view that can accept the data.
                    case DragEvent.ACTION_DRAG_LOCATION:

                        saveDragEvent(posX, posY, viewId);
                        break;

                    // Signals to a View that the user has released the object,
                    // and the drag point is within the bounding box of the View
                    // and not within a  view that can accept the data.
                    case DragEvent.ACTION_DROP:

                        if (!isDropPositionValid(event) ){
                            saveDropEvent(event.getX(), event.getY(), false, viewId , false);
                            break;
                        }

                        LinearLayout oldParent = (LinearLayout) view.getParent();
                        LinearLayout newParent = (LinearLayout) v;

                        int childCount = newParent.getChildCount();
                        if(childCount == 0){
                            oldParent.removeAllViews();
                            newParent.removeAllViews();
                            newParent.addView(view);
                            boolean placedCorrect = checkIfPuzzlePieceIsCorrect((ImageView) view, newParent);
                            saveDropEvent(posX, posY, true, viewId, placedCorrect);
                        } else{

                            for(int i = 0; i < childCount+1; i++) {
                                if (newParent.getChildAt(i) != null) {
                                    ImageView imageView = (ImageView) newParent.getChildAt(i);
                                    oldParent.removeAllViews();
                                    newParent.removeAllViews();
                                    oldParent.addView(imageView);
                                    newParent.addView(view);
                                    boolean placedCorrect = checkIfPuzzlePieceIsCorrect((ImageView) view, newParent);
                                    saveSwapEvent(posX, posY, viewId, placedCorrect);
                                }
                            }
                        }
                        checkIfPuzzleIsCompleted();
                        break;
                    default:
                        break;
                }
                return true;

            } catch (Exception e){

                // This will handle exceptions (e.g.: double click)
                // when two events are handled in a short time period
                // TODO: Maybe resolve this with a flag that only one event can be handled
                return true;
            }
        }
    };

    public View.OnDragListener onDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            if(mIgnoreEvents){
                return false;
            }

            try {

                View view = (View) event.getLocalState();
                int viewId = view.getId();

                int[] coordinates = new int[2];
                v.getLocationOnScreen(coordinates);
                float posX = coordinates[0] + event.getX();
                float posY = coordinates[1] + event.getY();

                switch (event.getAction()) {

                    // Sent to a View after ACTION_DRAG_ENTERED while the object is still within
                    // the View object's bounding box, but not within a view that can accept the data.
                    case DragEvent.ACTION_DRAG_LOCATION:

                        saveDragEvent(posX, posY, viewId);
                        break;

                    default:
                        break;
                }
                return true;

            } catch (Exception e){

                // This will handle exceptions (e.g.: double click)
                // when two events are handled in a short time period
                // TODO: Maybe resolve this with a flag that only one event can be handled
                return true;
            }
        }
    };

    public OnTouchListener onTouchListener = new OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if(mIgnoreEvents){
                return false;
            }

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        }

    };

    private void saveEvent(EventType eventType, float x, float y, int viewId, boolean isSnapped, boolean setCorrectly) {

        TelemetryEvent telemetryEvent = new TelemetryEvent(eventType);
        telemetryEvent.setPoint(new Point((int) x, (int) y));
        telemetryEvent.setViewId(viewId);
        telemetryEvent.setSnappedIn(isSnapped);
        telemetryEvent.setSetCorrectly(setCorrectly);
        telemetryHandler.addEvent(telemetryEvent);

    }

    private void saveDropEvent(float x, float y, boolean isSnappedIn, int viewId, boolean setCorrectly) {

        saveEvent(EventType.DROP, x, y, viewId, isSnappedIn, setCorrectly);
    }

    private void saveDragEvent(float x, float y, int viewId) {

        saveEvent(EventType.DRAG, x, y, viewId, false, false);
    }

    private void saveSwapEvent(float x, float y, int viewId, boolean setCorrectly) {

        saveEvent(EventType.SWAP, x, y, viewId, true, setCorrectly);
    }

    private void checkIfPuzzleIsCompleted() {
        int pieces = bitmapSolution.length;
        int[] ids =  new int[pieces];

        for(int i = 0; i < pieces; i++){
            ids[i] = 1001 + i;
        }
        LinearLayout[] layouts = new LinearLayout[pieces];

        Bitmap[] bitmapAnswer = new Bitmap[pieces];
        ImageView[] iV_solution = new ImageView[pieces];

        for(int i = 0; i < pieces; i++) {
            layouts[i] = mMainLayout.findViewById(ids[i]);
            iV_solution[i] = (ImageView) layouts[i].getChildAt(0);

            if(iV_solution[i] != null){
                bitmapAnswer[i] = ((BitmapDrawable) iV_solution[i].getDrawable()).getBitmap();
            }
        }

        if(solutionIsCorrect(bitmapAnswer, bitmapSolution)){

            mIgnoreEvents = true;
            puzzleInfo.setWin(true);
        }
    }

    private boolean checkIfPuzzlePieceIsCorrect(ImageView imageView, LinearLayout parent) {

        // convert imageView to bitmap
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // get id of the parent layout of the imageView
        int id = parent.getId();

        // check if imageView is placed correct
        if((id == 1001 && bitmapSolution[0].sameAs(bitmap))
                || (id == 1002 && bitmapSolution[1].sameAs(bitmap))
                || (id == 1003 && bitmapSolution[2].sameAs(bitmap))
                || (id == 1004 && bitmapSolution[3].sameAs(bitmap))){

            return true;

        } else {

            return false;
        }
    }

    private boolean solutionIsCorrect(Bitmap[] bitmapAnswer, Bitmap[] bitmapSolution){

        if(bitmapAnswer.length != bitmapSolution.length){
            return false;
        }

        for(int i = 0; i < bitmapAnswer.length; i++){
            if(bitmapAnswer[i] == null || !bitmapAnswer[i].sameAs(bitmapSolution[i])){
                return false;
            }
        }

        return true;
    }

    private boolean isDropPositionValid(DragEvent event){

        View view = (View) event.getLocalState();
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();

        float x = event.getX();
        float y = event.getY();

        float mToleranceY = 0.25f;
        float mToleranceX = 0.25f;

        return (!(x / viewWidth < mToleranceX)) &&
                (!(x / viewWidth > 1.0 - mToleranceX)) &&
                (!(y / viewHeight < mToleranceY)) &&
                (!(y / viewHeight > 1.0 - mToleranceY));
    }

}
