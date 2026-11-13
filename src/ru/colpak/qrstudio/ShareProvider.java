package ru.colpak.qrstudio;
import android.content.*;import android.database.*;import android.net.Uri;import android.os.*;import java.io.*;
public class ShareProvider extends ContentProvider {
 public boolean onCreate(){return true;}
 public String getType(Uri u){return "image/png";}
 public Cursor query(Uri u,String[] p,String s,String[] a,String sort){MatrixCursor c=new MatrixCursor(new String[]{"_display_name","_size"});c.addRow(new Object[]{"QR-Studio.png",new File(getContext().getCacheDir(),"qr.png").length()});return c;}
 public ParcelFileDescriptor openFile(Uri u,String mode)throws FileNotFoundException{if(!"/qr.png".equals(u.getPath())||!"r".equals(mode))throw new FileNotFoundException();return ParcelFileDescriptor.open(new File(getContext().getCacheDir(),"qr.png"),ParcelFileDescriptor.MODE_READ_ONLY);}
 public Uri insert(Uri u,ContentValues v){throw new UnsupportedOperationException();}public int delete(Uri u,String s,String[] a){throw new UnsupportedOperationException();}public int update(Uri u,ContentValues v,String s,String[] a){throw new UnsupportedOperationException();}
}
