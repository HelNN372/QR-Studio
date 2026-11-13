package ru.colpak.qrstudio;
import com.google.zxing.*;import com.google.zxing.common.*;import com.google.zxing.qrcode.decoder.*;import java.util.*;
public final class QrEngine {
 public static BitMatrix encode(String text)throws WriterException{return encode(text,BarcodeFormat.QR_CODE);}
 public static BitMatrix encode(String text,BarcodeFormat format)throws WriterException{
  Map<EncodeHintType,Object> h=new EnumMap<>(EncodeHintType.class);
  boolean qr=format==BarcodeFormat.QR_CODE;
  if(qr){h.put(EncodeHintType.CHARACTER_SET,"UTF-8");h.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);h.put(EncodeHintType.MARGIN,4);}
  else {h.put(EncodeHintType.MARGIN,40);text=text.trim();}
  if(format==BarcodeFormat.EAN_13&&!text.matches("[0-9]{12,13}"))throw new IllegalArgumentException("EAN-13: введи 12 цифр или 13 с верной контрольной цифрой.");
  if(format==BarcodeFormat.EAN_8&&!text.matches("[0-9]{7,8}"))throw new IllegalArgumentException("EAN-8: введи 7 цифр или 8 с верной контрольной цифрой.");
  if(format==BarcodeFormat.CODE_128&&!text.matches("[\\x20-\\x7E]{1,80}"))throw new IllegalArgumentException("Code 128: от 1 до 80 латинских букв, цифр или знаков ASCII. Для русского текста выбери QR.");
  try{return new MultiFormatWriter().encode(text,format,qr?1024:1200,qr?1024:360,h);}catch(IllegalArgumentException e){if(format==BarcodeFormat.EAN_13||format==BarcodeFormat.EAN_8)throw new IllegalArgumentException("Неверная контрольная цифра. Введи первые "+(format==BarcodeFormat.EAN_13?12:7)+" цифр — последняя рассчитается автоматически.");throw e;}
 }
 public static Result decodeResult(LuminanceSource source)throws NotFoundException{
  Map<DecodeHintType,Object> h=new EnumMap<>(DecodeHintType.class);h.put(DecodeHintType.TRY_HARDER,true);h.put(DecodeHintType.CHARACTER_SET,"UTF-8");
  h.put(DecodeHintType.POSSIBLE_FORMATS,Arrays.asList(BarcodeFormat.QR_CODE,BarcodeFormat.EAN_13,BarcodeFormat.EAN_8,BarcodeFormat.UPC_A,BarcodeFormat.UPC_E,BarcodeFormat.CODE_128,BarcodeFormat.CODE_39,BarcodeFormat.CODE_93,BarcodeFormat.ITF,BarcodeFormat.CODABAR,BarcodeFormat.DATA_MATRIX,BarcodeFormat.AZTEC,BarcodeFormat.PDF_417));
  MultiFormatReader reader=new MultiFormatReader();LuminanceSource image=new Rotatable(source.getMatrix(),source.getWidth(),source.getHeight());
  try{return reader.decode(new BinaryBitmap(new HybridBinarizer(image)),h);}catch(NotFoundException e){reader.reset();return reader.decode(new BinaryBitmap(new HybridBinarizer(image.invert())),h);}
 }
 public static String decode(LuminanceSource source)throws NotFoundException{return decodeResult(source).getText();}
 static final class Rotatable extends LuminanceSource{
  final byte[] data;Rotatable(byte[] data,int w,int h){super(w,h);this.data=data;}
  public byte[] getRow(int y,byte[] row){int w=getWidth();if(row==null||row.length<w)row=new byte[w];System.arraycopy(data,y*w,row,0,w);return row;}
  public byte[] getMatrix(){return data;}public boolean isRotateSupported(){return true;}
  public LuminanceSource rotateCounterClockwise(){int w=getWidth(),h=getHeight();byte[] out=new byte[w*h];for(int y=0;y<h;y++)for(int x=0;x<w;x++)out[(w-1-x)*h+y]=data[y*w+x];return new Rotatable(out,h,w);}
 }
 public static String webUrl(String text){String t=text.trim();if(t.matches("(?is)^https?://[^\\s]+$")&&androidFreeHost(t))return t;return null;}
 private static boolean androidFreeHost(String t){try{java.net.URI u=new java.net.URI(t);return u.getHost()!=null&&!u.getHost().isEmpty();}catch(Exception e){return false;}}
}
