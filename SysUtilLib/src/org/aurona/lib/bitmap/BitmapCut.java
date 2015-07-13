package org.aurona.lib.bitmap;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class BitmapCut {
	public static List<Bitmap> getNineGridImage(Bitmap bmp) {    
        return createImagePieces(bmp,3,3,0,0);  
    } 
	
	public static List<Bitmap> getNineGridImage(Bitmap bmp,int gridWidth) {    
        return createImagePieces(bmp,3,3,gridWidth,gridWidth);  
    } 
	
    public static List<Bitmap> createImagePieces(Bitmap bitmap, int xPiece, int yPiece) {    
        return createImagePieces(bitmap,xPiece,yPiece,0,0);  
    }  
    
    public static List<Bitmap> createImagePieces(Bitmap bitmap, int xPiece, int yPiece, int xGrid, int yGrid) {  
  	  
        List<Bitmap> pieces = new ArrayList<Bitmap>(xPiece * yPiece);  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        int pieceWidth = (width - xGrid*(xPiece -1)) / xPiece;  
        int pieceHeight = (height - yGrid*(yPiece -1)) / yPiece;  
        for (int i = 0; i < yPiece; i++) {  
            for (int j = 0; j < xPiece; j++) {  
                //ImagePiece piece = new ImagePiece();  
                //piece.index = j + i * xPiece;  
                int xValue = j * (pieceWidth + xGrid);  
                int yValue = i * (pieceHeight + yGrid);  
                Bitmap piece = Bitmap.createBitmap(bitmap, xValue, yValue,  
                        pieceWidth, pieceHeight);  
                pieces.add(piece);  
            }  
        }  
  
        return pieces;  
    } 
	
}
