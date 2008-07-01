package net.sourceforge.jgeocoder.us;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jgeocoder.CommonUtils;

import org.apache.commons.lang.StringUtils;

class AliasResolver{
  private static final Map<String, Map<String, String>> CITY_ALIAS_MAP = new HashMap<String, Map<String,String>>();
  static{
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("city-alias.txt")));
      String line = null;
      while((line=br.readLine())!= null){
        String[] items = line.split("\\s*=\\s*");
        String[] cs = items[0].split("<b>")[1].split("\\s*,\\s*");
        String city = cs[0], state = cs[1];
        String[] alias = items[1].split("[|]");
        Map<String, String> aliasMap = CITY_ALIAS_MAP.get(state);
        if(aliasMap == null){
          aliasMap = new HashMap<String, String>();
          CITY_ALIAS_MAP.put(state, aliasMap);
        }
        for(String a : alias){
          aliasMap.put(a.split("\\s*,\\s*")[0].replaceAll("\\s+", "").intern(), city.intern());
        }
      }      
    } catch (IOException e) {
      throw new Error("Unable to initalize City Alias Resolver", e);
    }
  }
  
  /**
   * @param city
   * @param state
   * @return the real city if the input {@code city} is an recognized alias, otherwise returns the 
   * original input
   */
  public static String resolveCityAlias(String city, String state){
    if(StringUtils.isBlank(city) || StringUtils.isBlank(state)){
      return city;
    }
    Map<String, String> aliasMap = CITY_ALIAS_MAP.get(state);
    if(aliasMap == null) return city;
    String realCity = aliasMap.get(city.replaceAll("\\s+", ""));
    return CommonUtils.nvl(realCity, city);
  }
  
}