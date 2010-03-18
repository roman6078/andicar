/*
 *  AndiCar - a car management software for Android powered devices.
 *
 *  Copyright (C) 2010 Miklos Keresztes (miklos.keresztes@gmail.com)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.andicar.persistence;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import org.andicar.activity.R;
import org.andicar.utils.StaticValues;

/**
 *
 * @author miki
 */
public class FileUtils {
    private AlertDialog.Builder exceptionAlertBuilder;
    private AlertDialog exceptionAlert;
    private Resources mRes;

    public String lastError = null;

    public int onCreate(Context ctx){
        try{
            lastError = null;
            mRes = ctx.getResources();
            File file = new File("/sdcard");
            if(!file.exists() || !file.isDirectory()){
                lastError = "SDCARD not found.";
                exceptionAlertBuilder = new AlertDialog.Builder(ctx);
                exceptionAlertBuilder.setCancelable( false );
                exceptionAlertBuilder.setPositiveButton( mRes.getString(R.string.GEN_OK), null );
                exceptionAlertBuilder.setMessage(mRes.getString(R.string.ERR_020));
                exceptionAlert = exceptionAlertBuilder.create();
                exceptionAlert.show();
                return R.string.ERR_020;
            }
            file = new File(StaticValues.reportFolder);
            if(!file.exists()){
                if(!file.mkdirs()){
                    lastError = "Report folder " +  StaticValues.reportFolder + " cannot be created.";
                    exceptionAlertBuilder = new AlertDialog.Builder(ctx);
                    exceptionAlertBuilder.setCancelable( false );
                    exceptionAlertBuilder.setPositiveButton( mRes.getString(R.string.GEN_OK), null );
                    exceptionAlertBuilder.setMessage(mRes.getString(R.string.ERR_021));
                    exceptionAlert = exceptionAlertBuilder.create();
                    exceptionAlert.show();
                    return R.string.ERR_021;
                }
            }
            file = new File(StaticValues.backupFolder);
            if(!file.exists()){
                if(!file.mkdirs()){
                    lastError = "Backup folder " +  StaticValues.backupFolder + " cannot be created.";
                    exceptionAlertBuilder = new AlertDialog.Builder(ctx);
                    exceptionAlertBuilder.setCancelable( false );
                    exceptionAlertBuilder.setPositiveButton( mRes.getString(R.string.GEN_OK), null );
                    exceptionAlertBuilder.setMessage(mRes.getString(R.string.ERR_024));
                    exceptionAlert = exceptionAlertBuilder.create();
                    exceptionAlert.show();
                    return R.string.ERR_024;
                }
            }
        }
        catch(SecurityException e){
            exceptionAlertBuilder = new AlertDialog.Builder(ctx);
            exceptionAlertBuilder.setCancelable( false );
            exceptionAlertBuilder.setPositiveButton( mRes.getString(R.string.GEN_OK), null );
            exceptionAlertBuilder.setMessage(e.getMessage());
            exceptionAlert = exceptionAlertBuilder.create();
            exceptionAlert.show();
            return -2;
        }
        return -1;
    }

    public int writeToFile(String content, String fileName){
        try
        {
            lastError = null;
            File file = new File(StaticValues.reportFolder + fileName);
            if(!file.createNewFile())
                return R.string.ERR_022;
            FileWriter fw = new FileWriter(file);
            fw.append(content);
            fw.flush();
            fw.close();
        }
        catch (IOException e)
        {
            lastError = e.getMessage();
            return R.string.ERR_023;
        }
        return -1;
    }

    public boolean deleteFile(String pathToFile){
        File file = new File(pathToFile);
        return file.delete();
    }

    public boolean copyFile(Context ctx, String fromFilePath, String toFilePath, boolean overwriteExisting){
        try{
            File fromFile = new File(fromFilePath);
            File toFile = new File(toFilePath);
            if(overwriteExisting && toFile.exists())
                toFile.delete();
            return copyFile(ctx, fromFile, toFile);
        }
        catch(SecurityException e){
            lastError = e.getMessage();
            return false;
        }
    }

    public boolean copyFile(Context ctx, File source, File dest){
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();

            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

            out.write(buf);
            
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            return true;
        } 
        catch(IOException e){
            lastError = e.getMessage();
            exceptionAlertBuilder = new AlertDialog.Builder(ctx);
            exceptionAlertBuilder.setCancelable( false );
            exceptionAlertBuilder.setPositiveButton( mRes.getString(R.string.GEN_OK), null );
            exceptionAlertBuilder.setMessage(e.getMessage());
            exceptionAlert = exceptionAlertBuilder.create();
            exceptionAlert.show();
            return false;
        }
    }

    public static ArrayList<String> getBkFileNames(){
        ArrayList<String> myData = new ArrayList<String>();
        File bkDir = new File(StaticValues.backupFolder);
        if(!bkDir.exists() || !bkDir.isDirectory()){
            return null;
        }
        String[] bkFiles = bkDir.list();
        if(bkFiles.length == 0){
            return null;
        }
        for (int i = 0; i < bkFiles.length; i++) {
            myData.add(bkFiles[i]);
        }
        return myData;
    }

}