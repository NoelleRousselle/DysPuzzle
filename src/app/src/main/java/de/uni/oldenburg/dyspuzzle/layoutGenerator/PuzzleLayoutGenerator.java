package de.uni.oldenburg.dyspuzzle.layoutGenerator;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.PuzzleInfo;
import de.uni.oldenburg.dyspuzzle.handler.PuzzleHandler;

public class PuzzleLayoutGenerator {

    private static Context mContext = null;

    private int counter = 0;
    private int startIdImages = 2000;
    private int startIdResults = 1000;

    private ContextInfo mContextInfo;
    private PuzzleHandler mPuzzleHandler;

    private PuzzleInfo mPuzzleInfo = PuzzleInfo.getInstance();
    private Bitmap[] mShuffledBitmaps;

    public PuzzleLayoutGenerator(Context context){
        mContext = context;
        mContextInfo = new ContextInfo(mContext);
    }

    // Initializing the main layout for the puzzle
    public void initializeLayout(LinearLayout mainLayout, LayoutInfo layoutInfo){

        mainLayout.removeAllViews();
        mainLayout.setWeightSum(getWeightSum(layoutInfo));
        mainLayout.setOrientation(getMainLayoutOrientation(layoutInfo.getOrientation()));

        // Calculate the size of one puzzle piece
        Rectangle rectangle = calculatePuzzlePiece(layoutInfo);
        mPuzzleInfo.generatePuzzleContent(rectangle);
        mShuffledBitmaps = mPuzzleInfo.getShuffledBitmaps();

        // Handler for onTouch and Drag/Drop
        mPuzzleHandler = new PuzzleHandler(mainLayout);
    }

    private Rectangle calculatePuzzlePiece(LayoutInfo layoutInfo){

        int mainOrientation = layoutInfo.getOrientation();
        int screenWidth = mContextInfo.getScreenWidth(mainOrientation);
        int screenHeight = mContextInfo.getScreenHeight(mainOrientation);

        float mainWeightSum = getWeightSum(layoutInfo);

        for (LayoutDetail detail : layoutInfo.getDetails()){

            if (detail.getValues() == null){
                continue;
            }

            for (LayoutCell cell : detail.getValues()){

                if(cell.getType() != CellType.START){
                    continue;
                }

                float weight = detail.getWeight();
                float detailWeightSum = getDetailWeightSum(detail);

                float heightFactor = cell.getWeight() / detailWeightSum;
                float widthFactor = weight / mainWeightSum;
                int width = (int)(screenWidth * widthFactor);
                int height = (int) (screenHeight * heightFactor);
                Rectangle rectangle = new Rectangle();
                rectangle.setBounds(0,0,width +1, height +1);
                return rectangle;
            }
        }

        return new Rectangle();
    }

    public void generateLayout(LinearLayout mainLayout, LayoutInfo layoutInfo){

        int mainOrientation = layoutInfo.getOrientation();
        float mainWeightSum = getWeightSum(layoutInfo);

        for (LayoutDetail detail : layoutInfo.getDetails()){
            LinearLayout colLayout = generateColumn(mainOrientation, mainWeightSum, detail);
            mainLayout.addView(colLayout);
        }

        mainLayout.setOnDragListener(mPuzzleHandler.onDragListener);

    }


    /**
     * Generates a column for the main layout
     * @param mainOrientation       portrait or landscape
     * @param detail                information for the column/row, contains the weight
     *                              and the list of rows/cloumns for the second level
     * @return                      the generated linearlayout
     */
    private LinearLayout generateColumn(int mainOrientation, float mainWeightSum, LayoutDetail detail){

        int screenWidth = mContextInfo.getScreenWidth(mainOrientation);
        int screenHeight = mContextInfo.getScreenHeight(mainOrientation);

        // Generate layout for the column
        int detailOrientation = getLayoutOrientation(mainOrientation);
        float weight = detail.getWeight();
        float detailWeightSum = getDetailWeightSum(detail);

        LinearLayout colLayout = generateColLinearLayout(mainWeightSum, weight, detailOrientation);

        // If the generated column has no row information we return the column
        if (detail.getValues() == null){
            return colLayout;
        }

        for (LayoutCell cell : detail.getValues()){

            LinearLayout rowLayout = generateRowLinearLayout(detailWeightSum, cell.getWeight(), weight/mainWeightSum,  detailOrientation);

            if (cell.getType() == CellType.START){
                ImageView imageView = new ImageView(mContext);
                setIdToImageView(imageView);
                imageView.setImageBitmap(mShuffledBitmaps[counter]);

                float heightFactor = cell.getWeight() / detailWeightSum;
                float widthFactor = weight / mainWeightSum;
                int width = (int)(screenWidth * widthFactor);
                int height = (int) (screenHeight * heightFactor);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setAdjustViewBounds(true);
                rowLayout.addView(imageView);
                rowLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border));

                rowLayout.setOnDragListener(mPuzzleHandler.onDragDropListener);
                imageView.setOnTouchListener(mPuzzleHandler.onTouchListener);

                counter++;
            }

            if(cell.getType() == CellType.DESTINATION){
                rowLayout.setOnDragListener(mPuzzleHandler.onDragDropListener);
                rowLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border));
                int id = rowLayout.getId();
                if (id == View.NO_ID) {
                    id = ++startIdResults;
                    rowLayout.setId(id);
                }

            }
            colLayout.addView(rowLayout);
        }

        return colLayout;
    }

    private void setIdToImageView(ImageView imageView) {

        int id = imageView.getId();
        if (id == View.NO_ID) {
            id = ++startIdImages;
            imageView.setId(id);
        }
    }

    private LinearLayout generateColLinearLayout(float mainWeightSum, float weight, int orientation){

        int screenWidth = mContextInfo.getScreenWidth(orientation);
        int screenHeight = mContextInfo.getScreenHeight(orientation);
        float widthFactor = weight / mainWeightSum;
        LayoutParams params = new LayoutParams((int)(screenWidth * widthFactor), screenHeight);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(orientation);
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setLayoutParams(params);

        return layout;
    }

    private LinearLayout generateRowLinearLayout(float mainWeightSum, float rowWeight, float widthFactor, int orientation){

        int screenWidth = mContextInfo.getScreenWidth(orientation);
        int screenHeight = mContextInfo.getScreenHeight(orientation);
        float heightFactor = rowWeight / mainWeightSum;
        int width = (int) (screenWidth * widthFactor);
        LayoutParams params = new LayoutParams(width, (int) (screenHeight * heightFactor));

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(orientation);
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setLayoutParams(params);

        return layout;
    }

    private int getMainLayoutOrientation(int orientation){

        return orientation == Configuration.ORIENTATION_PORTRAIT
                ? LinearLayout.VERTICAL
                : LinearLayout.HORIZONTAL;
    }

    private int getLayoutOrientation(int orientation){

        return orientation == Configuration.ORIENTATION_PORTRAIT
                ? LinearLayout.HORIZONTAL
                : LinearLayout.VERTICAL;
    }

    private float getWeightSum(LayoutInfo layoutInfo){

        if(layoutInfo == null || layoutInfo.getDetails() == null){
            return 0;
        }

        float sum = 0;

        for (int i = 0; i < layoutInfo.getDetails().length; i++) {
            sum += layoutInfo.getDetails()[i].getWeight();
        }
        return sum;
    }

    private float getDetailWeightSum(LayoutDetail layoutDetail){

        if(layoutDetail == null || layoutDetail.getValues() == null){
            return 0;
        }

        float sum = 0;

        for (int i = 0; i < layoutDetail.getValues().length; i++) {
            sum += layoutDetail.getValues()[i].getWeight();
        }
        return sum;
    }

}