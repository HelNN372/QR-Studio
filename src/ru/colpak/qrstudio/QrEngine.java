package ru.colpak.qrstudio;
import com.google.zxing.*;import com.google.zxing.common.*;import com.google.zxing.qrcode.*;import com.google.zxing.qrcode.decoder.*;import java.util.*;
public final class QrEngine {
 public static BitMatrix encode(String text)throws WriterException{Map<EncodeHintType,Object> h=new EnumMap<>(EncodeHintType.class);h.put(EncodeHintType.CHARACTER_SET,"UTF-8");h.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);h.put(EncodeHintType.MARGIN,4);return new QRCodeWriter().encode(text,BarcodeFormat.QR_CODE,1024,1024,h);}
 public static String decode(LuminanceSource source)throws NotFoundException,ChecksumException,FormatException{Map<DecodeHintType,Object> h=new EnumMap<>(DecodeHintType.class);h.put(DecodeHintType.TRY_HARDER,true);h.put(DecodeHintType.CHARACTER_SET,"UTF-8");QRCodeReader r=new QRCodeReader();try{return r.decode(new BinaryBitmap(new HybridBinarizer(source)),h).getText();}catch(NotFoundException e){return r.decode(new BinaryBitmap(new HybridBinarizer(source.invert())),h).getText();}}
 public static String webUrl(String text){String t=text.trim();if(t.matches("(?is)^https?://[^\\s]+$")&&androidFreeHost(t))return t;return null;}
 private static boolean androidFreeHost(String t){try{java.net.URI u=new java.net.URI(t);return u.getHost()!=null&&!u.getHost().isEmpty();}catch(Exception e){return false;}}
}
