import ru.colpak.qrstudio.QrEngine;import com.google.zxing.*;import com.google.zxing.common.*;
public class BarcodeTest {
 public static void main(String[] a)throws Exception{
 BarcodeFormat[] formats={BarcodeFormat.EAN_13,BarcodeFormat.EAN_8,BarcodeFormat.CODE_128};String[] inputs={"590123412345","9638507","Colpak-128 / 2026"};String[] expected={"5901234123457","96385074","Colpak-128 / 2026"};int count=0;
 for(int i=0;i<formats.length;i++)for(int invert=0;invert<2;invert++)for(int turn=0;turn<4;turn++){
 BitMatrix m=QrEngine.encode(inputs[i],formats[i]);for(int r=0;r<turn;r++)m.rotate90();int w=m.getWidth(),h=m.getHeight();int[] p=new int[w*h];byte[] yuv=new byte[w*h*3/2];for(int y=0;y<h;y++)for(int x=0;x<w;x++){boolean black=m.get(x,y)^(invert==1);p[y*w+x]=black?0xff000000:0xffffffff;yuv[y*w+x]=(byte)(black?0:255);}
 for(LuminanceSource source:new LuminanceSource[]{new RGBLuminanceSource(w,h,p),new PlanarYUVLuminanceSource(yuv,w,h,0,0,w,h,false)}){Result read=QrEngine.decodeResult(source);if(!expected[i].equals(read.getText())||read.getBarcodeFormat()!=formats[i])throw new AssertionError(read.toString());count++;}}
 String[] invalid={"123","5901234123458","абвгд"};for(int i=0;i<invalid.length;i++){boolean rejected=false;try{QrEngine.encode(invalid[i],i==2?BarcodeFormat.CODE_128:BarcodeFormat.EAN_13);}catch(IllegalArgumentException e){rejected=true;}if(!rejected)throw new AssertionError("Invalid input accepted");}
 System.out.println("PASS: "+count+" barcode roundtrips (EAN-13, EAN-8, Code 128; four rotations; inverted; photo and camera buffers), validation and automatic checksums");
 }
}
