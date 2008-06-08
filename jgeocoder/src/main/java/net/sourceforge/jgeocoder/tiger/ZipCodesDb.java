package net.sourceforge.jgeocoder.tiger;
import java.io.File;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;
@Entity
class CityStateGeo{
  @PrimaryKey
  private Location _location;
  private float _lat;
  private float _lon;
  public Location getLocation() {
    return _location;
  }
  public void setLocation(Location location) {
    _location = location;
  }
  public float getLat() {
    return _lat;
  }
  public void setLat(float lat) {
    _lat = lat;
  }
  public float getLon() {
    return _lon;
  }
  public void setLon(float lon) {
    _lon = lon;
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}

@Entity
class ZipCode{
  @PrimaryKey
  private String _zip;
  @SecondaryKey(relate=Relationship.MANY_TO_ONE)
  private Location _location;
  @SecondaryKey(relate=Relationship.MANY_TO_ONE)
  private String _county;
  private float _lat;
  private float _lon;
  private String _zipClass;
  public String getZip() {
    return _zip;
  }
  public void setZip(String zip) {
    _zip = zip;
  }
  public Location getLocation() {
    return _location;
  }
  public void setLocation(Location location) {
    _location = location;
  }
  public String getCounty() {
    return _county;
  }
  public void setCounty(String county) {
    _county = county;
  }
  public float getLat() {
    return _lat;
  }
  public void setLat(float lat) {
    _lat = lat;
  }
  public float getLon() {
    return _lon;
  }
  public void setLon(float lon) {
    _lon = lon;
  }
  public String getZipClass() {
    return _zipClass;
  }
  public void setZipClass(String zipClass) {
    _zipClass = zipClass;
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
  
}
@Persistent
class Location{ 
  @KeyField(1)
  private String _city;
  @KeyField(2)
  private String _state;

  public String getCity() {
    return _city;
  }
  public void setCity(String city) {
    _city = city;
  }
  public String getState() {
    return _state;
  }
  public void setState(String state) {
    _state = state;
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}

class ZipCodesDb{
  private Environment _env = null;
  private EntityStore _store = null;
  public Environment getEnv() {
    return _env;
  }
  public EntityStore getStore() {
    return _store;
  }
  
  public void init(File envHome, boolean readOnly, boolean transactional) throws DatabaseException{
    EnvironmentConfig config = new EnvironmentConfig();
    config.setAllowCreate(!readOnly);
    config.setReadOnly(readOnly);
    config.setTransactional(transactional);
    _env = new Environment(envHome, config);
    StoreConfig config2 = new StoreConfig();
    config2.setAllowCreate(!readOnly);
    config2.setReadOnly(readOnly);
    config2.setTransactional(transactional);
    _store = new EntityStore(_env, "ZipCodeEntityStore", config2);
  }
  
  public void shutdown() throws DatabaseException{
    if(_store != null){
      _store.close();
    }
    if(_env != null){
      _env.close();
    }
  }
  
}