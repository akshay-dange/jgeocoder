package net.sourceforge.jgeocoder.us;

import static net.sourceforge.jgeocoder.AddressComponent.CITY;
import static net.sourceforge.jgeocoder.AddressComponent.LINE2;
import static net.sourceforge.jgeocoder.AddressComponent.NAME;
import static net.sourceforge.jgeocoder.AddressComponent.NUMBER;
import static net.sourceforge.jgeocoder.AddressComponent.POSTDIR;
import static net.sourceforge.jgeocoder.AddressComponent.POSTDIR2;
import static net.sourceforge.jgeocoder.AddressComponent.PREDIR;
import static net.sourceforge.jgeocoder.AddressComponent.PREDIR2;
import static net.sourceforge.jgeocoder.AddressComponent.STATE;
import static net.sourceforge.jgeocoder.AddressComponent.STREET;
import static net.sourceforge.jgeocoder.AddressComponent.STREET2;
import static net.sourceforge.jgeocoder.AddressComponent.TYPE;
import static net.sourceforge.jgeocoder.AddressComponent.TYPE2;
import static net.sourceforge.jgeocoder.AddressComponent.ZIP;
import static net.sourceforge.jgeocoder.us.AddressRegexLibrary.LINE2A_GROUPED;
import static net.sourceforge.jgeocoder.us.Data.getDIRECTIONAL_MAP;
import static net.sourceforge.jgeocoder.us.Data.getNUMBER_MAP;
import static net.sourceforge.jgeocoder.us.Data.getSTATE_CODE_MAP;
import static net.sourceforge.jgeocoder.us.Data.getSTREET_TYPE_MAP;
import static net.sourceforge.jgeocoder.us.Data.getUNIT_MAP;
import static net.sourceforge.jgeocoder.us.RegexLibrary.TXT_NUM_0_99;
import static net.sourceforge.jgeocoder.us.Utils.nvl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jgeocoder.AddressComponent;

import org.apache.commons.lang.StringUtils;
public class AddressStandardizer{
  /**
   * Turn input map into one line of format
   * 
   * {name, num predir street type postdir, line2, city, state, zip}
   * 
   * @param parsedAddr
   * @return
   */
  public static String toSingleLine(Map<AddressComponent, String> parsedAddr){
    StringBuilder sb = new StringBuilder();
    appendIfNotNull(sb, parsedAddr.get(NAME), ", ");
    appendIfNotNull(sb, parsedAddr.get(NUMBER), " ");
    appendIfNotNull(sb, parsedAddr.get(PREDIR), " ");
    appendIfNotNull(sb, parsedAddr.get(STREET), " ");
    if(parsedAddr.get(STREET2) != null){
      appendIfNotNull(sb, parsedAddr.get(TYPE2), " ");
      appendIfNotNull(sb, parsedAddr.get(POSTDIR2), " ");
      sb.append("& ");
      appendIfNotNull(sb, parsedAddr.get(PREDIR2), " ");
      appendIfNotNull(sb, parsedAddr.get(STREET2), " ");
    }
    appendIfNotNull(sb, parsedAddr.get(TYPE), " ");
    appendIfNotNull(sb, parsedAddr.get(POSTDIR), " ");
    if(StringUtils.isNotBlank(sb.toString())){
      sb.append(", ");
    }
    appendIfNotNull(sb, parsedAddr.get(LINE2), ", ");
    appendIfNotNull(sb, parsedAddr.get(CITY), ", ");
    appendIfNotNull(sb, parsedAddr.get(STATE), " ");
    appendIfNotNull(sb, parsedAddr.get(ZIP), " ");
    return sb.toString().replaceAll(" ,", ",");
  }
  
  private static void appendIfNotNull(StringBuilder sb, String s, String suffix){
    if(s != null){
      sb.append(s).append(suffix);
    }
  }
  
  /**
   * Normalize the input parsedAddr map into a standardize format
   * 
   * @param parsedAddr
   * @return normalized address in a map
   */
  public static Map<AddressComponent, String>  normalizeParsedAddress(Map<AddressComponent, String> parsedAddr){
    Map<AddressComponent, String> ret = new HashMap<AddressComponent, String>();
    //just take the digits from the number component
    for(Map.Entry<AddressComponent, String> e : parsedAddr.entrySet()){
      String v = StringUtils.upperCase(e.getValue());
      switch (e.getKey()) {
        case PREDIR: ret.put(AddressComponent.PREDIR, normalizeDir(v)); break;
        case POSTDIR: ret.put(AddressComponent.POSTDIR, normalizeDir(v)); break;
        case TYPE: ret.put(AddressComponent.TYPE, normalizeStreetType(v)); break;
        case PREDIR2: ret.put(AddressComponent.PREDIR2, normalizeDir(v)); break;
        case POSTDIR2: ret.put(AddressComponent.POSTDIR2, normalizeDir(v)); break;
        case TYPE2: ret.put(AddressComponent.TYPE2, normalizeStreetType(v)); break;
        case NUMBER: ret.put(AddressComponent.NUMBER, normalizeNum(v)); break;
        case STATE: ret.put(AddressComponent.STATE, normalizeState(v)); break;
        case ZIP: ret.put(AddressComponent.ZIP, normalizeZip(v)); break;
        case LINE2: ret.put(AddressComponent.LINE2, normalizeLine2(v)); break;                    
        default: ret.put(e.getKey(), v); break;
      }
    }
    return ret;
  }
  //oh man... what had i got myself into...
  //XXX this class is tightly coupled with the regex library classes
  private static final Pattern TXT_NUM = Pattern.compile("^\\W*("+TXT_NUM_0_99+")\\W*");
  private static final Pattern DIGIT = Pattern.compile("(.*?\\d+)\\W*(.+)?");
  private static String normalizeNum(String num){
    if(num == null) return null;
    Matcher m = TXT_NUM.matcher(num);
    String ret = null;
    if(m.matches()){
      ret = m.group(1);
      if(ret.contains("-") || ret.contains(" ")){//it's a 2 part number
        String[] pair = ret.split("[ -]");
        String pre = getNUMBER_MAP().get(pair[0]).substring(0, 1);
        ret = pre+getNUMBER_MAP().get(pair[1]);
      }else{
        ret = getNUMBER_MAP().get(ret);
      }
    }else{
      m = DIGIT.matcher(num);
      if(m.matches()){
        ret = m.group(2) == null? m.group(1): m.group(1)+"-"+m.group(2);
      }
    }
    return nvl(ret, num) ;
  }

  private static String normalizeDir(String dir){
    if(dir == null) return null;
    dir = dir.replace(" ", "");
    return dir.length() > 2 ? getDIRECTIONAL_MAP().get(dir): dir;
  }
  
  private static String normalizeStreetType(String type){
    return nvl(getSTREET_TYPE_MAP().get(type), type);
  }
  
  private static String normalizeState(String state){
    return nvl(getSTATE_CODE_MAP().get(state), state);
  }
  private static final Pattern LINE2A = Pattern.compile("\\W*(?:"+LINE2A_GROUPED+")\\W*");
  private static String normalizeLine2(String line2){
    if(line2 == null) return null;
    Matcher m = LINE2A.matcher(line2);
    if(m.matches()){
      for(Map.Entry<String, String> e : getUNIT_MAP().entrySet()){
        if(line2.startsWith(e.getKey()+" ")){
          line2 = line2.replaceFirst(e.getKey()+" ", e.getValue()+" ");
          break;
        }
      }
    }
    return line2;
  }
  
  private static String normalizeZip(String zip){
    return StringUtils.length(zip) > 5 ? zip.substring(0, 5) : zip;
  }
  

}