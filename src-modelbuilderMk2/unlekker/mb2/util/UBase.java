package unlekker.mb2.util;

import java.lang.Character.Subset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphics3D;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertex;

public class UBase implements UConst {
  public int options;
  private static String[] optionNames;
  
  
  protected static PGraphics g;
  protected static PGraphics3D g3d;
  protected static int gErrorCnt=0;

  public UBase ptranslate(UVertex v) {
    if(checkGraphicsSet()) {
      g.translate(v.x, v.y,v.z);
    }
    
    return this;
  }

  public void setOptions(int opt) {
    options=opt;
  }

  public void enable(int opt) {
    options=options|opt;
  }

  public boolean enabled(int opt) {
    return (options & opt)==opt;
  }

  public void disable(int opt) {
    options=options  & (~opt);
  }
  
  public String optionStr() {
    if(optionNames==null) {
      optionNames=new String[1000];
      optionNames[COLORVERTEX]="COLORVERTEX";
      optionNames[COLORFACE]="COLORFACE";
      optionNames[NOCOPY]="NOCOPY";
      optionNames[NODUPL]="NODUPL";
    }
    
    StringBuffer buf=new StringBuffer();
    if(enabled(NODUPL)) buf.append(optionNames[NODUPL]).append(TAB);
    if(enabled(NOCOPY)) buf.append(optionNames[NOCOPY]).append(TAB);
    if(enabled(COLORFACE)) buf.append(optionNames[COLORFACE]).append(TAB);
    if(enabled(COLORVERTEX)) buf.append(optionNames[COLORVERTEX]).append(TAB);

    if(buf.length()>0) {
      buf.deleteCharAt(buf.length()-1);
      return "Options: "+buf.toString();
    }
    
    return "Options: None";
  }
  
  public static final int color(int c,float a) {
    return ((int)a<< 24) & c;
  }

  public static final int color(float r, float g, float b) {
    int rr=(int)r,gg=(int)g,bb=(int)b;
    return (0xFF000000)|
        ((rr&0xff)<<16)|((gg&0xff)<<8)|(bb&0xff);
  }

  public static final int color(int r, int g, int b, int a) {
    return (0xFF00000)|((r&0xff)<<16)|((g&0xff)<<8)|(b&0xff);
  }

  public static String hex(int col) {
    String s="",tmp;
    
    int a=(col >> 24) & 0xff;
    if(a<255) {
      tmp=Integer.toHexString(a);
      if(tmp.length()<2) s+="0"+tmp;
      else s+=tmp;
    }
  
    char ZERO='0';
    s+=strPad(Integer.toHexString((col>>16)&0xff),2,ZERO);
    s+=strPad(Integer.toHexString((col>>8)&0xff),2,ZERO);
    s+=strPad(Integer.toHexString((col)&0xff),2,ZERO);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
//    tmp=Integer.toHexString((col>>8)&0xff);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
//    tmp=Integer.toHexString((col)&0xff);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
    
    s=s.toUpperCase();
    return s;
  }

  public static final int color(String hex) {
    int c=0xFFFF0000,alpha=255;
    
    boolean ok=true;
    
    if(hex==null) ok=false; 
    else for(int i=0; ok && i<hex.length(); i++) {
      char ch=hex.charAt(i);
      if(!(
          Character.isAlphabetic(ch) ||
              Character.isDigit(ch)
              )) ok=false;
    }
    if(!ok) {
      log("toColor('"+hex+"') failed.");
      return c;
    }
    
    try {
      if(hex.length()==8) {
        alpha=Integer.parseInt(hex.substring(0,2),16);
//      UUtil.log("hex: "+hex+" alpha: "+alpha);
        hex=hex.substring(2);
      }
      c=(alpha<<24) | Integer.parseInt(hex, 16);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      c=color(255,0,0);
      e.printStackTrace();
    }
    
    return c;
  }

  //////////////////////////////////////////
  // MATH
  // map,lerp,max,constrain code taken from processing.core.PApplet
  
  
  static public final float abs(float n) {
    return (n < 0) ? -n : n;
  }

  static public final int abs(int n) {
    return (n < 0) ? -n : n;
  }

  static public final float sq(float a) {
    return a*a;
  }

  static public final float sqrt(float a) {
    return (float)Math.sqrt(a);
  }

  static public final int max(int a, int b) {
    return (a > b) ? a : b;
  }

  static public final float max(float a, float b) {
    return (a > b) ? a : b;
  }

  static public final int min(int a, int b) {
    return (a < b) ? a : b;
  }

  static public final float min(float a, float b) {
    return (a < b) ? a : b;
  }

  
  static public final float map(float value,
      float ostart, float ostop) {
        return ostart + (ostop - ostart) * (value);
  }
  
  static public final float map(float value,
      float istart, float istop,
      float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

  static public final int constrain(int amt, int low, int high) {
    return (amt < low) ? low : ((amt > high) ? high : amt);
  }

  static public final float constrain(float amt, float low, float high) {
    return (amt < low) ? low : ((amt > high) ? high : amt);
  }

  static public final float lerp(float start, float stop, float amt) {
    return start + (stop-start) * amt;
  }

  
  // extended versions
  
  static public final float mod(float a, float b) { // code from David Bollinger
    return (a%b+b)%b; 
  }

  static public final float max(ArrayList<Float> val) {
    float theMax=Float.MIN_VALUE;
    
    for(float v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }

  static public final float min(ArrayList<Float> val) {
    float theMin=Float.MAX_VALUE;
    
    for(float v:val) theMin=(v>theMin ? v : theMin);
    return theMin;
  }

  static public final float max(float val[]) {
    float theMax=val[0];
    for(float v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }

  static public final int max(int val[]) {
    int theMax=val[0];
    for(int v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }


  static public final double mapDbl(double value,
      double istart, double istop,
      double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }


  //////////////////////////////////////////
  // PARSING VALUES

  public static int parseInt(String s) {
    if(s==null) return Integer.MIN_VALUE;
    return Integer.parseInt(s.trim());
  }

  public static float parseFloat(String s) {
    if(s==null) return Float.NaN;
    return Float.parseFloat(s.trim());
  }

  public static float[] parseFloat(String s[]) {
    if(s==null) return null;
    
    float f[]=new float[s.length];
    int id=0;
    for(String ss:s) f[id++]=parseFloat(ss);
    
    return f;
  }

  
  //////////////////////////////////////////
  // RANDOM VALUES

  /**
   * Static copy of unlekker.util.Rnd for easy random number generation.
   */
  public static URnd rnd=new URnd(System.currentTimeMillis());
  
  public boolean rndProbGt(float prob) {
    return rnd.prob(prob>100 ? 100 : prob);
  }

  public static boolean rndBool() {
    return rnd.bool();
  }

  public static float rnd() {
    return rnd.random(1);
  }

  public static float rnd(float max) {
    return rnd.random(max);
  }

  public static float rndSign() {
    return (rndBool() ? -1 : 1);
  }

  public static float rnd(float min, float max) {
    return rnd.random(min,max);
  }

  public static float rndSigned(float v) {
    return rnd(v)*rndSign();
  }
   public static float rndSigned(float min, float max) {
     float val=rnd.random(min,max);
      return rndBool() ? val : -val;
    }

  public static int rndInt(int max) {
    return rnd.integer(max);
  }

  public static int rndInt(int min, int max) {
    return rnd.integer(min,max);
  }

  public static int rndIntSigned(float min, float max) {
    int val=rnd.integer(min,max);
     return rndBool() ? val : -val;
   }
  
  
  //////////////////////////////////////////
  // SET / GET PGRAPHICS 
  
  public static boolean checkGraphicsSet() {
    if(g==null) {
      if(gErrorCnt%100==0) logErr("ModelbuilderMk2: No PGraphics set. Use UGeo.setGraphics(PApplet).");
      gErrorCnt++;
    }
    return true;
  }
  
  public static PGraphics3D getGraphics() {
    return g3d;
  }

  public static void setGraphics(PApplet papplet) {
    setGraphics(papplet.g);
  }

  public static void setGraphics(PGraphics gg) {
    UBase.g=gg;
    UBase.g3d=(PGraphics3D)gg;
  }

  //////////////////////////////////////////
  // LOGGING
  
  public static void log(String s) {
    System.out.println(s);
  }

  public static void log(int i) {
    System.out.println(i);
  }

  public static void log(float f) {
    System.out.println(f);
  }

  public static void logErr(String s) {
    System.err.println(s);
  }

  public static void logDivider() {
    System.out.println(LOGDIVIDER);
  }

  public static void logDivider(String s) {
    System.out.println(LOGDIVIDER+' '+s);
  }

  
  //////////////////////////////////////////
  // FILE TOOLS
  
  public static String nextFile(String path,String pre) {
    return nextFilename(path, pre,null);
  }

  public static String nextFilename(String path,String pre,String ext) {
    return UFile.nextFile(path, pre, ext);
  }
  
  //////////////////////////////////////////
  // NUMBER FORMATTING
  
  private static NumberFormat formatFloat, formatInt;
  private static char numberChar[]=new char[] {'0', '1', '2', '3', '4', '5',
      '6', '7', '8', '9', '-', '.'};

  static public void nfInitFormats() {
    formatFloat=NumberFormat.getInstance();
    formatFloat.setGroupingUsed(false);

    formatInt=NumberFormat.getInstance();
    formatInt.setGroupingUsed(false);
  }

  /**
   * Format floating point number for printing
   * 
   * @param num
   *          Number to format
   * @param lead
   *          Minimum number of leading digits
   * @param digits
   *          Number of decimal digits to show
   * @return Formatted number string
   */
  static public String nf(float num, int lead, int decimal) {
    if (formatFloat==null) nfInitFormats();
    formatFloat.setMinimumIntegerDigits(lead);
    formatFloat.setMaximumFractionDigits(decimal);
    formatFloat.setMinimumFractionDigits(decimal);

    return formatFloat.format(num).replace(",", ".");
  }

  static public String nf(double num, int lead, int decimal) {
    return nf((float)num,lead,decimal);
  }

  /**
   * Format floating point number for printing with maximum 3 decimal points.
   * 
   * @param num
   *          Number to format
   * @return Formatted number string
   */
  static public String nf(float num) {
    return nf(num,0,3);
  }

  static public String nf(float num,int prec) {
    return nf(num,1,prec);
  }

  static public String nf(double num) {
    return nf((float)num);
  }

  /**
   * Format integer number for printing, padding with zeros if number has fewer
   * digits than desired.
   * 
   * @param num
   *          Number to format
   * @param digits
   *          Minimum number of digits to show
   * @return Formatted number string
   */
  static public String nf(int num, int digits) {
    if (formatInt==null) nfInitFormats();
    formatInt.setMinimumIntegerDigits(digits);
    return formatInt.format(num);
  }

  public static String strPad(String s,int len,char c) {
    len-=s.length();
    while(len>0) {
      s+=c;
      len--;
    }
    
    return s;
  }
  

  public static <T> String str(ArrayList<T> o) {
    return str(o,NEWLN,null);
  }

  public static <T> String str(ArrayList<T> o, char delim,String enclosure) {
    StringBuffer buf=strBufGet();
    if(o==null) buf.append("null");
    else {
      int id=0;
      for(T oo:o) {
        if(buf.length()>0) buf.append(delim); 
//        buf.append(id++).append(' ');
        buf.append(oo.toString());
      }
    }
    
    if(enclosure!=null) {
      buf.insert(0, enclosure.charAt(0));
      buf.append(enclosure.charAt(1));
    }
    
    return strBufDispose(buf);
  }
  
  
  //////////////////////////////////////////
  // STRING BUFFER POOL
  
  protected static ArrayList<StringBuffer> strBufFree,strBufBusy;

  protected static String strBufDispose(StringBuffer buf) {
    if(strBufBusy!=null) {
      strBufBusy.remove(buf);
      strBufFree.add(buf);
    }
      
    return buf.toString();
  }

  
  protected static StringBuffer strBufGet() {
    try {
      StringBuffer buf;
      
      if(strBufBusy==null) {
        strBufBusy=new ArrayList<StringBuffer>();
        strBufFree=new ArrayList<StringBuffer>();
      }
      
      if(strBufFree.size()>1) {
        buf=strBufFree.remove(0);
        buf.setLength(0);
      }
      else buf=new StringBuffer();
      
      strBufBusy.add(buf);
      
      return buf;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return new StringBuffer();
  }
  
  //////////////////////////////////////////
  // STRING MANIPULATION
  
  public String strTrim(String s,int newlen) {
    return s.substring(0,newlen);
  }

  public String strStripContainer(String str) {
    String s=str.trim();
    int len=s.length();
    char ch1=s.charAt(0);
    char ch2=s.charAt(len-1);
    
    if((ch1=='[' && ch2==']') ||
        (ch1=='<' && ch2=='>')) {
      s=s.substring(1,len-1);
      return s;
    }
    
    return str;
  }

}