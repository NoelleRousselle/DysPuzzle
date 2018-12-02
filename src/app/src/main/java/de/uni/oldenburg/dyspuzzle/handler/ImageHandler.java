package de.uni.oldenburg.dyspuzzle.handler;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.constraint.solver.widgets.Rectangle;
import android.widget.ImageView;

import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Bitmap.createScaledBitmap;

    // class to manipulated images
    public class ImageHandler {

        public ImageHandler() {

        }

        // cuts the bitmap in to pieces depending on the number of rows and columns
        public Bitmap[] createBitmaps(Bitmap image, int rows, int columns) {

            int chunks = rows * columns;

            // calculate size of rows and columns
            int chunkWidth = image.getWidth() / columns;
            int chunkHeight = image.getHeight() / rows;
            int count = 0;

            // Image array to hold image chunks
            Bitmap images[] = new Bitmap[chunks];
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {

                    // Initialize the image array with image chunks
                    images[count] = createBitmap(image,chunkWidth * y,chunkHeight * x, chunkWidth, chunkHeight,null,false);
                    count++;
                }
            }

            return images;
        }

        // cuts the original image to the correct size
        // the rectangle object gives information about about the size of a puzzle field
        public Bitmap cutBitmap(Bitmap bitmap, int rows, int columns, Rectangle rectangle) {

            // factor, which indicates how often the size of the puzzle field fits
            // into the original picture
            float factorX = bitmap.getWidth() /  (float) (rectangle.width * columns);
            float factorY = bitmap.getHeight() / (float) (rectangle.height * rows);

            // checks if the puzzle field fits completely in the original picture
            if(factorX > 1.0 && factorY > 1.0){

                float factor = Math.min(factorX, factorY);

                //cuts the image according to height and width
                if(factor == factorY){

                    float width = (bitmap.getWidth() / factor);
                    float offsetX =  ((width - (rectangle.width * columns)) / 2  * factor);
                    width = bitmap.getWidth() - 2 * offsetX;
                    return createBitmap(bitmap, (int) offsetX, 0, (int) width, bitmap.getHeight(),null, false);

                } else{

                    float height = (bitmap.getHeight() / factor);
                    float offsetY = ((height - (rectangle.height * rows)) / 2  * factor);
                    height = bitmap.getHeight() - 2 * offsetY;
                    return createBitmap(bitmap, 0, (int) offsetY, bitmap.getWidth(), (int) height , null,false);

                }

            } else {

                // if the original picture is too small:
                // call this method again with with an artificially enlarged image
                bitmap = createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 1.1), (int) (bitmap.getHeight() *1.1) ,false);
                return cutBitmap(bitmap, rows, columns, rectangle);

            }
        }

        // this method can be used later if the puzzle pieces are to be rotated
        // rotates the image to the given angle
        public ImageView turnImage(ImageView image, int angle){
            int pivotX = (image.getWidth() / 2);
            int pivotY = (image.getHeight() / 2);
            Matrix matrix = new Matrix();
            image.setScaleType(ImageView.ScaleType.MATRIX);
            matrix.postRotate((float) angle, pivotX, pivotY);
            image.setImageMatrix(matrix);
            return image;
        }
    }