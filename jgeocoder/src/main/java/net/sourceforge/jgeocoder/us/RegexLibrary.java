package net.sourceforge.jgeocoder.us;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Some common regex, they are for address parsing, not for validating English, some 
 * common spelling mistakes are intentionally included 
 * 
 * @author jliang
 *
 */
class RegexLibrary{
  
  ///////////////NUMBERS///////////////
  
  public static String TXT_NUM_0_9 = 
    "zero|one|two|three|four|five|six|seven|eight|nine";
  private static String TXT_NUM_10_19 = 
    "ten|eleven|twelve|thirteen|fourteen|fifteen|sixteen|seventeen|eighteen|nineteen";
  private static String TXT_NUM_DECADES = 
    "twenty|thirty|fourty|forty|fifty|sixty|seventy|eighty|ninety|ninty";
  private static String TXT_NUM_20_99 = 
    "(?:"+TXT_NUM_DECADES+")(?:[- ](?:"+TXT_NUM_0_9+"))?";
  private static final String ORDINAL_0_9 = 
    "0[-]?th|1[-]?st|2[-]?nd|3[-]?rd|[0[4-9]][-]?th|1[0-9][-]?th";
  
  private static final String TXT_ORDINAL_1_9 = 
    "first|second|third|fourth|forth|fifth|sixth|seventh|eighth|ninth|nineth";  
  private static final String TXT_ORDINAL_10_19 = 
    "tenth|eleventh|twelfth|twelveth|twelvth|thirteenth|fourteenth|fifteenth|sixteenth|seventeenth|enghteenth|nineteenth";
  private static final String TXT_ORDINAL_DECADES = 
    "twentieth|thirtieth|fourtieth|fortieth|fiftieth|sixitieth|seventieth|eightieth|ninetieth|nintieth";
  private static final String TXT_ORDINAL_20_99 =
    "(?:"+TXT_ORDINAL_DECADES+")"+
  "|" +
    "(?:"+TXT_NUM_DECADES+")[- ](?:"+TXT_ORDINAL_1_9+")";

  public static String TXT_NUM_0_99 = 
      "(?:"+TXT_NUM_0_9+")" +
    "|" +
      "(?:"+TXT_NUM_10_19+")" +
    "|" +
      "(?:"+TXT_NUM_20_99+")";
    
  //XXX not necessary valid english grammar, but it's okay
  public static final String ORDINAL_ALL = "(?:[0-9]*(?:"+ORDINAL_0_9+"))";

  public static final String TXT_ORDINAL_0_99 =
    "zeroth" +
  "|" +
    "(?:"+TXT_ORDINAL_1_9+")" +
  "|" +
    "(?:"+TXT_ORDINAL_10_19+")" +
  "|" +
    "(?:"+TXT_ORDINAL_20_99+")";  
  
  /////////////NUMBERS///////////////

  /////////////US ADDRESSES///////////////
  
  public static final String STREET_DESIGNATOR = UsAddressesData.getStreetDesignatorRegex();
  public static final String US_STATES = UsAddressesData.getStateRegex();
  public static final String DIRECTIONS = UsAddressesData.getDirectionRegex();
  public static final String ADDR_UNIT = UsAddressesData.getUnitDesignatorRegex();
  
  @SuppressWarnings("unchecked")
  private static class UsAddressesData{    
    public static String getDirectionRegex(){
      String abbrv = "N|N[ ]?E|E|S[ ]?E|S|S[ ]?W|W|N[ ]?W";
      return join("|", DIRECTIONAL_MAP.keySet())+"|"+abbrv;
    }
    
    public static String getStateRegex(){
      return join("|", STATE_CODE_MAP.values(), STATE_CODE_MAP.keySet());
    }
    
    public static String getStreetDesignatorRegex(){
      return join("|", STREET_TYPE_MAP.values(), STREET_TYPE_MAP.keySet());
    }
    
    public static String getUnitDesignatorRegex(){
      return join("|", UNIT_MAP.values(), UNIT_MAP.keySet());
    }
    
    private static String join(String separator, Collection<String>...collections){
      Set<String> union = new HashSet<String>();
      for(Collection<String> c : collections){
        union.addAll(c);      
      }
      String[] set = new String[union.size()];
      List<String> lst = Arrays.asList(union.toArray(set));
      Collections.sort(lst, new Comparator<String>(){
        public int compare(String o1, String o2) {
          return new Integer(o2.length()).compareTo(o1.length());
        }
      });
      return StringUtils.join(lst, separator);
    }
    
    private static final Map<String, String> DIRECTIONAL_MAP = new HashMap<String, String>();
    private static final Map<String, String> STREET_TYPE_MAP = new HashMap<String, String>();
    private static final Map<String, String> STATE_CODE_MAP = new HashMap<String, String>();
    private static final Map<String, String> UNIT_MAP = new HashMap<String, String>();
    static{
      UNIT_MAP.put("APT","APARTMENT");
      UNIT_MAP.put("APMT","APARTMENT");
      UNIT_MAP.put("BSMT","BASEMENT");
      UNIT_MAP.put("BLDG","BUILDING");
      UNIT_MAP.put("DEPT","DEPARTMENT");
      UNIT_MAP.put("FL","FLOOR");
      UNIT_MAP.put("F","FLOOR");
      UNIT_MAP.put("FRNT","FRONG");
      UNIT_MAP.put("HNGR","HANGAR");
      UNIT_MAP.put("LBBY","LOBBY");
      UNIT_MAP.put("LBY","LOBBY");
      UNIT_MAP.put("LOT","LOT");
      UNIT_MAP.put("LT","LOT");
      UNIT_MAP.put("LOWR","LOWER");
      UNIT_MAP.put("NUM","NUMBER");
      UNIT_MAP.put("#","NUMBER");
      UNIT_MAP.put("NO","NUMBER");
      UNIT_MAP.put("OFC","OFFICE");
      UNIT_MAP.put("OFIC","OFFICE");
      UNIT_MAP.put("OFFC","OFFICE");
      UNIT_MAP.put("PIER","PIER");
      UNIT_MAP.put("PH","PENTHOUSE");
      UNIT_MAP.put("PBOX","PO BOX");
      UNIT_MAP.put("PB","PO BOX");
      UNIT_MAP.put("PBOX","POBOX");
      UNIT_MAP.put("PB","POBOX");
      UNIT_MAP.put("POBX","PO BOX");
      UNIT_MAP.put("POBX","POBOX");
      UNIT_MAP.put("BX","BOX");
      UNIT_MAP.put("REAR","REAR");
      UNIT_MAP.put("RM","ROOM");
      UNIT_MAP.put("SIDE","SIDE");
      UNIT_MAP.put("SLIP","SLIP");
      UNIT_MAP.put("SPC","SPACE");
      UNIT_MAP.put("STOP","STOP");
      UNIT_MAP.put("STE","SUITE");
      UNIT_MAP.put("SUIT","SUITE");
      UNIT_MAP.put("TRLR","TRAILER");
      UNIT_MAP.put("UNT","UNIT");
      UNIT_MAP.put("UPPR","UPPER");

      DIRECTIONAL_MAP.put("north", "N");
      DIRECTIONAL_MAP.put("northeast", "NE");
      DIRECTIONAL_MAP.put("east", "E");
      DIRECTIONAL_MAP.put("southeast", "SE");
      DIRECTIONAL_MAP.put("south", "S");
      DIRECTIONAL_MAP.put("southwest", "SW");
      DIRECTIONAL_MAP.put("west", "W");
      DIRECTIONAL_MAP.put("northwest", "NW");

      STREET_TYPE_MAP.put("allee", "aly");
      STREET_TYPE_MAP.put("alley", "aly");
      STREET_TYPE_MAP.put("ally", "aly");
      STREET_TYPE_MAP.put("anex", "anx");
      STREET_TYPE_MAP.put("annex", "anx");
      STREET_TYPE_MAP.put("annx", "anx");
      STREET_TYPE_MAP.put("arcade", "arc");
      STREET_TYPE_MAP.put("av", "ave");
      STREET_TYPE_MAP.put("aven", "ave");
      STREET_TYPE_MAP.put("avenu", "ave");
      STREET_TYPE_MAP.put("avenue", "ave");
      STREET_TYPE_MAP.put("avn", "ave");
      STREET_TYPE_MAP.put("avnue", "ave");
      STREET_TYPE_MAP.put("bayoo", "byu");
      STREET_TYPE_MAP.put("bayou", "byu");
      STREET_TYPE_MAP.put("beach", "bch");
      STREET_TYPE_MAP.put("bend", "bnd");
      STREET_TYPE_MAP.put("bluf", "blf");
      STREET_TYPE_MAP.put("bluff", "blf");
      STREET_TYPE_MAP.put("bluffs", "blfs");
      STREET_TYPE_MAP.put("bot", "btm");
      STREET_TYPE_MAP.put("bottm", "btm");
      STREET_TYPE_MAP.put("bottom", "btm");
      STREET_TYPE_MAP.put("boul", "blvd");
      STREET_TYPE_MAP.put("boulevard", "blvd");
      STREET_TYPE_MAP.put("boulv", "blvd");
      STREET_TYPE_MAP.put("branch", "br");
      STREET_TYPE_MAP.put("brdge", "brg");
      STREET_TYPE_MAP.put("bridge", "brg");
      STREET_TYPE_MAP.put("brnch", "br");
      STREET_TYPE_MAP.put("brook", "brk");
      STREET_TYPE_MAP.put("brooks", "brks");
      STREET_TYPE_MAP.put("burg", "bg");
      STREET_TYPE_MAP.put("burgs", "bgs");
      STREET_TYPE_MAP.put("bypa", "byp");
      STREET_TYPE_MAP.put("bypas", "byp");
      STREET_TYPE_MAP.put("bypass", "byp");
      STREET_TYPE_MAP.put("byps", "byp");
      STREET_TYPE_MAP.put("camp", "cp");
      STREET_TYPE_MAP.put("canyn", "cyn");
      STREET_TYPE_MAP.put("canyon", "cyn");
      STREET_TYPE_MAP.put("cape", "cpe");
      STREET_TYPE_MAP.put("causeway", "cswy");
      STREET_TYPE_MAP.put("causway", "cswy");
      STREET_TYPE_MAP.put("cen", "ctr");
      STREET_TYPE_MAP.put("cent", "ctr");
      STREET_TYPE_MAP.put("center", "ctr");
      STREET_TYPE_MAP.put("centers", "ctrs");
      STREET_TYPE_MAP.put("centr", "ctr");
      STREET_TYPE_MAP.put("centre", "ctr");
      STREET_TYPE_MAP.put("circ", "cir");
      STREET_TYPE_MAP.put("circl", "cir");
      STREET_TYPE_MAP.put("circle", "cir");
      STREET_TYPE_MAP.put("circles", "cirs");
      STREET_TYPE_MAP.put("ck", "crk");
      STREET_TYPE_MAP.put("cliff", "clf");
      STREET_TYPE_MAP.put("cliffs", "clfs");
      STREET_TYPE_MAP.put("club", "clb");
      STREET_TYPE_MAP.put("cmp", "cp");
      STREET_TYPE_MAP.put("cnter", "ctr");
      STREET_TYPE_MAP.put("cntr", "ctr");
      STREET_TYPE_MAP.put("cnyn", "cyn");
      STREET_TYPE_MAP.put("common", "cmn");
      STREET_TYPE_MAP.put("corner", "cor");
      STREET_TYPE_MAP.put("corners", "cors");
      STREET_TYPE_MAP.put("course", "crse");
      STREET_TYPE_MAP.put("court", "ct");
      STREET_TYPE_MAP.put("courts", "cts");
      STREET_TYPE_MAP.put("cove", "cv");
      STREET_TYPE_MAP.put("coves", "cvs");
      STREET_TYPE_MAP.put("cr", "crk");
      STREET_TYPE_MAP.put("crcl", "cir");
      STREET_TYPE_MAP.put("crcle", "cir");
      STREET_TYPE_MAP.put("crecent", "cres");
      STREET_TYPE_MAP.put("creek", "crk");
      STREET_TYPE_MAP.put("crescent", "cres");
      STREET_TYPE_MAP.put("cresent", "cres");
      STREET_TYPE_MAP.put("crest", "crst");
      STREET_TYPE_MAP.put("crossing", "xing");
      STREET_TYPE_MAP.put("crossroad", "xrd");
      STREET_TYPE_MAP.put("crscnt", "cres");
      STREET_TYPE_MAP.put("crsent", "cres");
      STREET_TYPE_MAP.put("crsnt", "cres");
      STREET_TYPE_MAP.put("crssing", "xing");
      STREET_TYPE_MAP.put("crssng", "xing");
      STREET_TYPE_MAP.put("crt", "ct");
      STREET_TYPE_MAP.put("curve", "curv");
      STREET_TYPE_MAP.put("dale", "dl");
      STREET_TYPE_MAP.put("dam", "dm");
      STREET_TYPE_MAP.put("div", "dv");
      STREET_TYPE_MAP.put("divide", "dv");
      STREET_TYPE_MAP.put("driv", "dr");
      STREET_TYPE_MAP.put("drive", "dr");
      STREET_TYPE_MAP.put("drives", "drs");
      STREET_TYPE_MAP.put("drv", "dr");
      STREET_TYPE_MAP.put("dvd", "dv");
      STREET_TYPE_MAP.put("estate", "est");
      STREET_TYPE_MAP.put("estates", "ests");
      STREET_TYPE_MAP.put("exp", "expy");
      STREET_TYPE_MAP.put("expr", "expy");
      STREET_TYPE_MAP.put("express", "expy");
      STREET_TYPE_MAP.put("expressway", "expy");
      STREET_TYPE_MAP.put("expw", "expy");
      STREET_TYPE_MAP.put("extension", "ext");
      STREET_TYPE_MAP.put("extensions", "exts");
      STREET_TYPE_MAP.put("extn", "ext");
      STREET_TYPE_MAP.put("extnsn", "ext");
      STREET_TYPE_MAP.put("falls", "fls");
      STREET_TYPE_MAP.put("ferry", "fry");
      STREET_TYPE_MAP.put("field", "fld");
      STREET_TYPE_MAP.put("fields", "flds");
      STREET_TYPE_MAP.put("flat", "flt");
      STREET_TYPE_MAP.put("flats", "flts");
      STREET_TYPE_MAP.put("ford", "frd");
      STREET_TYPE_MAP.put("fords", "frds");
      STREET_TYPE_MAP.put("forest", "frst");
      STREET_TYPE_MAP.put("forests", "frst");
      STREET_TYPE_MAP.put("forg", "frg");
      STREET_TYPE_MAP.put("forge", "frg");
      STREET_TYPE_MAP.put("forges", "frgs");
      STREET_TYPE_MAP.put("fork", "frk");
      STREET_TYPE_MAP.put("forks", "frks");
      STREET_TYPE_MAP.put("fort", "ft");
      STREET_TYPE_MAP.put("freeway", "fwy");
      STREET_TYPE_MAP.put("freewy", "fwy");
      STREET_TYPE_MAP.put("frry", "fry");
      STREET_TYPE_MAP.put("frt", "ft");
      STREET_TYPE_MAP.put("frway", "fwy");
      STREET_TYPE_MAP.put("frwy", "fwy");
      STREET_TYPE_MAP.put("garden", "gdn");
      STREET_TYPE_MAP.put("gardens", "gdns");
      STREET_TYPE_MAP.put("gardn", "gdn");
      STREET_TYPE_MAP.put("gateway", "gtwy");
      STREET_TYPE_MAP.put("gatewy", "gtwy");
      STREET_TYPE_MAP.put("gatway", "gtwy");
      STREET_TYPE_MAP.put("glen", "gln");
      STREET_TYPE_MAP.put("glens", "glns");
      STREET_TYPE_MAP.put("grden", "gdn");
      STREET_TYPE_MAP.put("grdn", "gdn");
      STREET_TYPE_MAP.put("grdns", "gdns");
      STREET_TYPE_MAP.put("green", "grn");
      STREET_TYPE_MAP.put("greens", "grns");
      STREET_TYPE_MAP.put("grov", "grv");
      STREET_TYPE_MAP.put("grove", "grv");
      STREET_TYPE_MAP.put("groves", "grvs");
      STREET_TYPE_MAP.put("gtway", "gtwy");
      STREET_TYPE_MAP.put("harb", "hbr");
      STREET_TYPE_MAP.put("harbor", "hbr");
      STREET_TYPE_MAP.put("harbors", "hbrs");
      STREET_TYPE_MAP.put("harbr", "hbr");
      STREET_TYPE_MAP.put("haven", "hvn");
      STREET_TYPE_MAP.put("havn", "hvn");
      STREET_TYPE_MAP.put("height", "hts");
      STREET_TYPE_MAP.put("heights", "hts");
      STREET_TYPE_MAP.put("hgts", "hts");
      STREET_TYPE_MAP.put("highway", "hwy");
      STREET_TYPE_MAP.put("highwy", "hwy");
      STREET_TYPE_MAP.put("hill", "hl");
      STREET_TYPE_MAP.put("hills", "hls");
      STREET_TYPE_MAP.put("hiway", "hwy");
      STREET_TYPE_MAP.put("hiwy", "hwy");
      STREET_TYPE_MAP.put("hllw", "holw");
      STREET_TYPE_MAP.put("hollow", "holw");
      STREET_TYPE_MAP.put("hollows", "holw");
      STREET_TYPE_MAP.put("holws", "holw");
      STREET_TYPE_MAP.put("hrbor", "hbr");
      STREET_TYPE_MAP.put("ht", "hts");
      STREET_TYPE_MAP.put("hway", "hwy");
      STREET_TYPE_MAP.put("inlet", "inlt");
      STREET_TYPE_MAP.put("island", "is");
      STREET_TYPE_MAP.put("islands", "iss");
      STREET_TYPE_MAP.put("isles", "isle");
      STREET_TYPE_MAP.put("islnd", "is");
      STREET_TYPE_MAP.put("islnds", "iss");
      STREET_TYPE_MAP.put("jction", "jct");
      STREET_TYPE_MAP.put("jctn", "jct");
      STREET_TYPE_MAP.put("jctns", "jcts");
      STREET_TYPE_MAP.put("junction", "jct");
      STREET_TYPE_MAP.put("junctions", "jcts");
      STREET_TYPE_MAP.put("junctn", "jct");
      STREET_TYPE_MAP.put("juncton", "jct");
      STREET_TYPE_MAP.put("key", "ky");
      STREET_TYPE_MAP.put("keys", "kys");
      STREET_TYPE_MAP.put("knol", "knl");
      STREET_TYPE_MAP.put("knoll", "knl");
      STREET_TYPE_MAP.put("knolls", "knls");
      STREET_TYPE_MAP.put("la", "ln");
      STREET_TYPE_MAP.put("lake", "lk");
      STREET_TYPE_MAP.put("lakes", "lks");
      STREET_TYPE_MAP.put("landing", "lndg");
      STREET_TYPE_MAP.put("lane", "ln");
      STREET_TYPE_MAP.put("lanes", "ln");
      STREET_TYPE_MAP.put("ldge", "ldg");
      STREET_TYPE_MAP.put("light", "lgt");
      STREET_TYPE_MAP.put("lights", "lgts");
      STREET_TYPE_MAP.put("lndng", "lndg");
      STREET_TYPE_MAP.put("loaf", "lf");
      STREET_TYPE_MAP.put("lock", "lck");
      STREET_TYPE_MAP.put("locks", "lcks");
      STREET_TYPE_MAP.put("lodg", "ldg");
      STREET_TYPE_MAP.put("lodge", "ldg");
      STREET_TYPE_MAP.put("loops", "loop");
      STREET_TYPE_MAP.put("manor", "mnr");
      STREET_TYPE_MAP.put("manors", "mnrs");
      STREET_TYPE_MAP.put("meadow", "mdw");
      STREET_TYPE_MAP.put("meadows", "mdws");
      STREET_TYPE_MAP.put("medows", "mdws");
      STREET_TYPE_MAP.put("mill", "ml");
      STREET_TYPE_MAP.put("mills", "mls");
      STREET_TYPE_MAP.put("mission", "msn");
      STREET_TYPE_MAP.put("missn", "msn");
      STREET_TYPE_MAP.put("mnt", "mt");
      STREET_TYPE_MAP.put("mntain", "mtn");
      STREET_TYPE_MAP.put("mntn", "mtn");
      STREET_TYPE_MAP.put("mntns", "mtns");
      STREET_TYPE_MAP.put("motorway", "mtwy");
      STREET_TYPE_MAP.put("mount", "mt");
      STREET_TYPE_MAP.put("mountain", "mtn");
      STREET_TYPE_MAP.put("mountains", "mtns");
      STREET_TYPE_MAP.put("mountin", "mtn");
      STREET_TYPE_MAP.put("mssn", "msn");
      STREET_TYPE_MAP.put("mtin", "mtn");
      STREET_TYPE_MAP.put("neck", "nck");
      STREET_TYPE_MAP.put("orchard", "orch");
      STREET_TYPE_MAP.put("orchrd", "orch");
      STREET_TYPE_MAP.put("overpass", "opas");
      STREET_TYPE_MAP.put("ovl", "oval");
      STREET_TYPE_MAP.put("parks", "park");
      STREET_TYPE_MAP.put("parkway", "pkwy");
      STREET_TYPE_MAP.put("parkways", "pkwy");
      STREET_TYPE_MAP.put("parkwy", "pkwy");
      STREET_TYPE_MAP.put("passage", "psge");
      STREET_TYPE_MAP.put("paths", "path");
      STREET_TYPE_MAP.put("pikes", "pike");
      STREET_TYPE_MAP.put("pine", "pne");
      STREET_TYPE_MAP.put("pines", "pnes");
      STREET_TYPE_MAP.put("pk", "park");
      STREET_TYPE_MAP.put("pkway", "pkwy");
      STREET_TYPE_MAP.put("pkwys", "pkwy");
      STREET_TYPE_MAP.put("pky", "pkwy");
      STREET_TYPE_MAP.put("place", "pl");
      STREET_TYPE_MAP.put("plain", "pln");
      STREET_TYPE_MAP.put("plaines", "plns");
      STREET_TYPE_MAP.put("plains", "plns");
      STREET_TYPE_MAP.put("plaza", "plz");
      STREET_TYPE_MAP.put("plza", "plz");
      STREET_TYPE_MAP.put("point", "pt");
      STREET_TYPE_MAP.put("points", "pts");
      STREET_TYPE_MAP.put("port", "prt");
      STREET_TYPE_MAP.put("ports", "prts");
      STREET_TYPE_MAP.put("prairie", "pr");
      STREET_TYPE_MAP.put("prarie", "pr");
      STREET_TYPE_MAP.put("prk", "park");
      STREET_TYPE_MAP.put("prr", "pr");
      STREET_TYPE_MAP.put("rad", "radl");
      STREET_TYPE_MAP.put("radial", "radl");
      STREET_TYPE_MAP.put("radiel", "radl");
      STREET_TYPE_MAP.put("ranch", "rnch");
      STREET_TYPE_MAP.put("ranches", "rnch");
      STREET_TYPE_MAP.put("rapid", "rpd");
      STREET_TYPE_MAP.put("rapids", "rpds");
      STREET_TYPE_MAP.put("rdge", "rdg");
      STREET_TYPE_MAP.put("rest", "rst");
      STREET_TYPE_MAP.put("ridge", "rdg");
      STREET_TYPE_MAP.put("ridges", "rdgs");
      STREET_TYPE_MAP.put("river", "riv");
      STREET_TYPE_MAP.put("rivr", "riv");
      STREET_TYPE_MAP.put("rnchs", "rnch");
      STREET_TYPE_MAP.put("road", "rd");
      STREET_TYPE_MAP.put("roads", "rds");
      STREET_TYPE_MAP.put("route", "rte");
      STREET_TYPE_MAP.put("rvr", "riv");
      STREET_TYPE_MAP.put("shoal", "shl");
      STREET_TYPE_MAP.put("shoals", "shls");
      STREET_TYPE_MAP.put("shoar", "shr");
      STREET_TYPE_MAP.put("shoars", "shrs");
      STREET_TYPE_MAP.put("shore", "shr");
      STREET_TYPE_MAP.put("shores", "shrs");
      STREET_TYPE_MAP.put("skyway", "skwy");
      STREET_TYPE_MAP.put("spng", "spg");
      STREET_TYPE_MAP.put("spngs", "spgs");
      STREET_TYPE_MAP.put("spring", "spg");
      STREET_TYPE_MAP.put("springs", "spgs");
      STREET_TYPE_MAP.put("sprng", "spg");
      STREET_TYPE_MAP.put("sprngs", "spgs");
      STREET_TYPE_MAP.put("spurs", "spur");
      STREET_TYPE_MAP.put("sqr", "sq");
      STREET_TYPE_MAP.put("sqre", "sq");
      STREET_TYPE_MAP.put("sqrs", "sqs");
      STREET_TYPE_MAP.put("squ", "sq");
      STREET_TYPE_MAP.put("square", "sq");
      STREET_TYPE_MAP.put("squares", "sqs");
      STREET_TYPE_MAP.put("station", "sta");
      STREET_TYPE_MAP.put("statn", "sta");
      STREET_TYPE_MAP.put("stn", "sta");
      STREET_TYPE_MAP.put("str", "st");
      STREET_TYPE_MAP.put("strav", "stra");
      STREET_TYPE_MAP.put("strave", "stra");
      STREET_TYPE_MAP.put("straven", "stra");
      STREET_TYPE_MAP.put("stravenue", "stra");
      STREET_TYPE_MAP.put("stravn", "stra");
      STREET_TYPE_MAP.put("stream", "strm");
      STREET_TYPE_MAP.put("street", "st");
      STREET_TYPE_MAP.put("streets", "sts");
      STREET_TYPE_MAP.put("streme", "strm");
      STREET_TYPE_MAP.put("strt", "st");
      STREET_TYPE_MAP.put("strvn", "stra");
      STREET_TYPE_MAP.put("strvnue", "stra");
      STREET_TYPE_MAP.put("sumit", "smt");
      STREET_TYPE_MAP.put("sumitt", "smt");
      STREET_TYPE_MAP.put("summit", "smt");
      STREET_TYPE_MAP.put("terr", "ter");
      STREET_TYPE_MAP.put("terrace", "ter");
      STREET_TYPE_MAP.put("throughway", "trwy");
      STREET_TYPE_MAP.put("tpk", "tpke");
      STREET_TYPE_MAP.put("tr", "trl");
      STREET_TYPE_MAP.put("trace", "trce");
      STREET_TYPE_MAP.put("traces", "trce");
      STREET_TYPE_MAP.put("track", "trak");
      STREET_TYPE_MAP.put("tracks", "trak");
      STREET_TYPE_MAP.put("trafficway", "trfy");
      STREET_TYPE_MAP.put("trail", "trl");
      STREET_TYPE_MAP.put("trails", "trl");
      STREET_TYPE_MAP.put("trk", "trak");
      STREET_TYPE_MAP.put("trks", "trak");
      STREET_TYPE_MAP.put("trls", "trl");
      STREET_TYPE_MAP.put("trnpk", "tpke");
      STREET_TYPE_MAP.put("trpk", "tpke");
      STREET_TYPE_MAP.put("tunel", "tunl");
      STREET_TYPE_MAP.put("tunls", "tunl");
      STREET_TYPE_MAP.put("tunnel", "tunl");
      STREET_TYPE_MAP.put("tunnels", "tunl");
      STREET_TYPE_MAP.put("tunnl", "tunl");
      STREET_TYPE_MAP.put("turnpike", "tpke");
      STREET_TYPE_MAP.put("turnpk", "tpke");
      STREET_TYPE_MAP.put("underpass", "upas");
      STREET_TYPE_MAP.put("union", "un");
      STREET_TYPE_MAP.put("unions", "uns");
      STREET_TYPE_MAP.put("valley", "vly");
      STREET_TYPE_MAP.put("valleys", "vlys");
      STREET_TYPE_MAP.put("vally", "vly");
      STREET_TYPE_MAP.put("vdct", "via");
      STREET_TYPE_MAP.put("viadct", "via");
      STREET_TYPE_MAP.put("viaduct", "via");
      STREET_TYPE_MAP.put("view", "vw");
      STREET_TYPE_MAP.put("views", "vws");
      STREET_TYPE_MAP.put("vill", "vlg");
      STREET_TYPE_MAP.put("villag", "vlg");
      STREET_TYPE_MAP.put("village", "vlg");
      STREET_TYPE_MAP.put("villages", "vlgs");
      STREET_TYPE_MAP.put("ville", "vl");
      STREET_TYPE_MAP.put("villg", "vlg");
      STREET_TYPE_MAP.put("villiage", "vlg");
      STREET_TYPE_MAP.put("vist", "vis");
      STREET_TYPE_MAP.put("vista", "vis");
      STREET_TYPE_MAP.put("vlly", "vly");
      STREET_TYPE_MAP.put("vst", "vis");
      STREET_TYPE_MAP.put("vsta", "vis");
      STREET_TYPE_MAP.put("walks", "walk");
      STREET_TYPE_MAP.put("well", "wl");
      STREET_TYPE_MAP.put("wells", "wls");
      STREET_TYPE_MAP.put("wy", "way");
      
      STATE_CODE_MAP.put("alabama", "AL");
      STATE_CODE_MAP.put("alaska", "AK");
      STATE_CODE_MAP.put("american samoa", "AS");
      STATE_CODE_MAP.put("arizona", "AZ");
      STATE_CODE_MAP.put("arkansas", "AR");
      STATE_CODE_MAP.put("california", "CA");
      STATE_CODE_MAP.put("colorado", "CO");
      STATE_CODE_MAP.put("connecticut", "CT");
      STATE_CODE_MAP.put("delaware", "DE");
      STATE_CODE_MAP.put("district of columbia", "DC");
      STATE_CODE_MAP.put("federated states of micronesia", "FM");
      STATE_CODE_MAP.put("florida", "FL");
      STATE_CODE_MAP.put("georgia", "GA");
      STATE_CODE_MAP.put("guam", "GU");
      STATE_CODE_MAP.put("hawaii", "HI");
      STATE_CODE_MAP.put("idaho", "ID");
      STATE_CODE_MAP.put("illinois", "IL");
      STATE_CODE_MAP.put("indiana", "IN");
      STATE_CODE_MAP.put("iowa", "IA");
      STATE_CODE_MAP.put("kansas", "KS");
      STATE_CODE_MAP.put("kentucky", "KY");
      STATE_CODE_MAP.put("louisiana", "LA");
      STATE_CODE_MAP.put("maine", "ME");
      STATE_CODE_MAP.put("marshall islands", "MH");
      STATE_CODE_MAP.put("maryland", "MD");
      STATE_CODE_MAP.put("massachusetts", "MA");
      STATE_CODE_MAP.put("michigan", "MI");
      STATE_CODE_MAP.put("minnesota", "MN");
      STATE_CODE_MAP.put("mississippi", "MS");
      STATE_CODE_MAP.put("missouri", "MO");
      STATE_CODE_MAP.put("montana", "MT");
      STATE_CODE_MAP.put("nebraska", "NE");
      STATE_CODE_MAP.put("nevada", "NV");
      STATE_CODE_MAP.put("new hampshire", "NH");
      STATE_CODE_MAP.put("new jersey", "NJ");
      STATE_CODE_MAP.put("new mexico", "NM");
      STATE_CODE_MAP.put("new york", "NY");
      STATE_CODE_MAP.put("north carolina", "NC");
      STATE_CODE_MAP.put("north dakota", "ND");
      STATE_CODE_MAP.put("northern mariana islands", "MP");
      STATE_CODE_MAP.put("ohio", "OH");
      STATE_CODE_MAP.put("oklahoma", "OK");
      STATE_CODE_MAP.put("oregon", "OR");
      STATE_CODE_MAP.put("palau", "PW");
      STATE_CODE_MAP.put("pennsylvania", "PA");
      STATE_CODE_MAP.put("puerto rico", "PR");
      STATE_CODE_MAP.put("rhode island", "RI");
      STATE_CODE_MAP.put("south carolina", "SC");
      STATE_CODE_MAP.put("south dakota", "SD");
      STATE_CODE_MAP.put("tennessee", "TN");
      STATE_CODE_MAP.put("texas", "TX");
      STATE_CODE_MAP.put("utah", "UT");
      STATE_CODE_MAP.put("vermont", "VT");
      STATE_CODE_MAP.put("virgin islands", "VI");
      STATE_CODE_MAP.put("virginia", "VA");
      STATE_CODE_MAP.put("washington", "WA");
      STATE_CODE_MAP.put("west virginia", "WV");
      STATE_CODE_MAP.put("wisconsin", "WI");
      STATE_CODE_MAP.put("wyoming", "WY");  
    }  
  }
  
}