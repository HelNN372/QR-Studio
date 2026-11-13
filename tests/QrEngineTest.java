import ru.colpak.qrstudio.QrEngine;import com.google.zxing.*;import com.google.zxing.common.*;
public class QrEngineTest {
 public static void main(String[] args)throws Exception{
 String[] samples={"Привет! Это QR-код 🇩🇪", "https://example.org/path?q=test&x=1", "WIFI:T:WPA;S:Моя сеть;P:12345678;;", "plain text\nsecond line", "a".repeat(1200)};
 int checks=0;
 for(String value:samples){BitMatrix m=QrEngine.encode(value);int w=m.getWidth();for(int rotation=0;rotation<4;rotation++){int[] p=new int[w*w];for(int y=0;y<w;y++)for(int x=0;x<w;x++)p[y*w+x]=m.get(x,y)?0xff000000:0xffffffff;String read=QrEngine.decode(new RGBLuminanceSource(w,w,p));if(!value.equals(read))throw new AssertionError("Roundtrip failed");m.rotate90();checks++;}}
 if(QrEngine.webUrl("javascript:alert(1)")!=null||QrEngine.webUrl("intent://foo")!=null||QrEngine.webUrl("https://")!=null)throw new AssertionError("Unsafe URL");
 if(!"https://example.org/a".equals(QrEngine.webUrl(" https://example.org/a ")))throw new AssertionError("URL missing");
 System.out.println("PASS: "+checks+" QR roundtrips (UTF-8, emoji, Wi-Fi, multiline, long text; four rotations), URL checks");
 }
}
