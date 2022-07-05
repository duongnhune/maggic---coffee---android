package com.example.magiccoffee_v2.gui.dataLocal;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class ImageInternalStorage {
//    imageDir
    public static boolean checkImageExists(Context context, String name){
        try{
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath = new File(directory, name);
            if(mypath.exists()) return true;
            return false;
        }
        catch (Exception ex){
            return false;
        }
    }


    public static void saveImage(Context context, Bitmap bitmap, String name){
              FileOutputStream fileOutputStream;
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath = new File(directory, name);

            fileOutputStream = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImage(Context context, ImageView imageView, String name){
        if(ImageInternalStorage.checkImageExists(context, name)){
            imageView.setImageBitmap(ImageInternalStorage.loadImageBitmap(context,name));
        }
        else{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference islandRef = storageRef.child("Images/"+name);

            final long ONE_MEGABYTE = 1024 * 1024 *5;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    ImageInternalStorage.saveImage(context, bitmap, name);
                    imageView.setImageBitmap(ImageInternalStorage.loadImageBitmap(context,name));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageView.setImageBitmap(ImageInternalStorage.loadImageBitmap(context,"no_image.jpg"));
                }
            });
        }
    }

    public static Bitmap loadImageBitmap(Context context,String name){
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try{
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath = new File(directory, name);

            fileInputStream = new FileInputStream(mypath);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
