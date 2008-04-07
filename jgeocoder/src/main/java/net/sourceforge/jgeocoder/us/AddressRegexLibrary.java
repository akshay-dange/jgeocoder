package net.sourceforge.jgeocoder.us;
import static net.sourceforge.jgeocoder.us.RegexLibrary.ADDR_UNIT;
import static net.sourceforge.jgeocoder.us.RegexLibrary.DIRECTIONS;
import static net.sourceforge.jgeocoder.us.RegexLibrary.ORDINAL_ALL;
import static net.sourceforge.jgeocoder.us.RegexLibrary.STREET_DESIGNATOR;
import static net.sourceforge.jgeocoder.us.RegexLibrary.TXT_NUM_0_9;
import static net.sourceforge.jgeocoder.us.RegexLibrary.TXT_ORDINAL_0_99;
import static net.sourceforge.jgeocoder.us.RegexLibrary.US_STATES;
import static net.sourceforge.jgeocoder.us.Utils.compile;
import net.sourceforge.jgeocoder.us.Utils.NamedGroupPattern;

class AddressRegexLibrary{
  
  private static final String NUMBER =
    "(?:\\p{Alpha})?\\d+(?:[- ][\\p{Alpha}&&[^NSEW]]" +
    "(?!\\s+(?:st|street|ave|aven|avenu|avenue|blvd|boulv|boulevard|boulv|plz|plaza|plza)))?" +
    "|\\d+-?\\d*(?:-?\\p{Alpha})?|"+TXT_NUM_0_9; 
  
  private static final String FRACTION = "\\d+\\/\\d+";
  
  private static final String LINE1A = 
    "(?P<street>"+DIRECTIONS+")\\W+" + 
    "(?P<type>"+STREET_DESIGNATOR+")\\b";
  
  private static final String LINE1B = 
    "(?:(?P<predir>"+DIRECTIONS+")\\W+)?" +
    "(?:" +
      "(?P<street>[^,]+)" +
      "(?:[^\\w,]+(?P<type>"+STREET_DESIGNATOR+")\\b)" +
      "(?:[^\\w,]+(?P<postdir>"+DIRECTIONS+")\\b)?" +
     "|" +
       "(?P<street>[^,]*\\d)" +
       "(?:(?P<postdir>"+DIRECTIONS+")\\b)" +
     "|" +
       "(?P<street>[^,]+?)" +
       "(?:[^\\w,]+(?P<type>"+STREET_DESIGNATOR+")\\b)?" +
       "(?:[^\\w,]+(?P<postdir>"+DIRECTIONS+")\\b)?" +       
    ")";
  
  private static final String LINE1 =
    "(?P<number>(?:" + NUMBER + ")(?:\\W+"+FRACTION+")?)\\W+" + 
    "(?:" + LINE1A + "|" + LINE1B + ")";
  
  //A, 2A, 22, A2, 2-a, 2/a, etc...
  private static final String UNIT_NUMBER = 
    "(?:\\b\\p{Alpha}{1}\\s+|\\p{Alpha}*[-/]?)?" +
    "(?:\\d+|\\b\\p{Alpha}\\b(?=\\s|$))" +
    "(?:[ ]*\\p{Alpha}\\b|-\\w+)?";
  private static final String LINE2A = "\\b(?:"+ADDR_UNIT+")\\W*?(?:"+UNIT_NUMBER+")";
  private static final String LINE2B = "\\b(?:(?:"+TXT_ORDINAL_0_99+"|"+ORDINAL_ALL+")\\W*(?:"+ADDR_UNIT+"))\\b";
  private static final String LINE2 = "(?:(?P<line2>"+LINE2A+"|"+LINE2B+")\\W+)?";
  
  private static final String ZIP = "\\d{5}(?:[- ]\\d{4})?";
  private static final String LASTLINE = 
    "(?:" +
      "(?:(?P<city>[^\\d,]+?)\\W+)?" +  //city                                              
      "(?P<state>"+US_STATES+")\\W*" + //state                                          
    ")?" +
    "(?P<zip>"+ZIP+")?";      //zip
  
  private static final String ADDR_NAME =  "\\W*(?:(?P<name>[^,]+)\\W+)??"; 
  
  private static final String STREET_ADDRESS = 
   ADDR_NAME + LINE1 + "\\W+"+ LINE2 + LASTLINE +"\\W*";
  
  private static final String CORNER = "(?:\\band\\b|\\bat\\b|&|\\@)";

  private static final String INTERSECTION = ADDR_NAME +
  "(?:" + LINE1A + "|" + LINE1B + ")" + "\\W*\\s+" + CORNER + "\\s+" +
  "(?:" + LINE1A + "|" + LINE1B + ")" + "\\W+" + LASTLINE +"\\W*";
  
  public static final NamedGroupPattern P_STREET_ADDRESS = compile(STREET_ADDRESS);
  public static final NamedGroupPattern P_INTERSECTION = compile(INTERSECTION);
  public static final NamedGroupPattern P_CORNER = compile(CORNER);
  

}