package com.comerciosrd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class ComerciosRDCacheUtils {

	public static void writeObject(Context context, String key, Object object) {
		try{
		FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
		} catch(IOException e){
			e.printStackTrace();
		}

	}

	public static Object readObject(Context context, String key) {
	   
		  FileInputStream fis = null;		
		  ObjectInputStream ois = null;
		  Object object = null;
		try {
			fis = context.openFileInput(key);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			object = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			object = null;
		}
		  
	   
      return object;
   }
	
	public static void clearCache(Context context) {
	      try {
	         File dir = context.getCacheDir();
	         if (dir != null && dir.isDirectory()) {
	        	 ComerciosRDCacheUtils.deleteDir(dir);
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

	   public static boolean deleteDir(File dir) {
	      if (dir != null && dir.isDirectory()) {
	         String[] children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
	      }

	      // The directory is now empty so delete it
	      return dir.delete();
	   }
}
