package edu.ndsu.eci.international_capstone_exchange.pages.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;

public class Logging {
  /** Tapestry package used in logger */
  private static final String TAPESTRY_PACKAGE = "org.apache.tapestry";

  /** NDSU library package used in logger */
  private static final String NDSU_PACKAGE = "edu.ndsu";

  /** Cayenne package used in logger */
  private static final String CAYENNE_PACKAGE = "org.apache.cayenne";
  
  /** Project package used in logger */
  private static final String APPLICATION_PACKAGE = "ndsu.eci.international_capstone_exchange";
  
  @Property
  private List<Level> allLevels; 
  
  /** Holds root level selection. */
  @Property
  private Level rootLevel;
  
  /** Holds root level selection. */
  @Property
  private Level applicationLevel;
  
  /** Holds cayenne level selection. */
  @Property
  private Level cayenneLevel;
  
  /** Holds root level selection. */
  @Property
  private Level ndsuLevel;
  
  /** Holds root level selection. */
  @Property
  private Level tapestryLevel;
  
  /**
   * Setup Render used to get pending task information.
   */
  public void setupRender() {
    allLevels = new ArrayList<>();
    allLevels.add(Level.ALL);
    allLevels.add(Level.DEBUG);
    allLevels.add(Level.ERROR);
    allLevels.add(Level.FATAL);
    allLevels.add(Level.INFO);
    allLevels.add(Level.OFF);
    allLevels.add(Level.TRACE);
    allLevels.add(Level.WARN);
    
    rootLevel = LogManager.getRootLogger().getLevel();
    applicationLevel = LogManager.getLogger(APPLICATION_PACKAGE).getEffectiveLevel();
    cayenneLevel = LogManager.getLogger(CAYENNE_PACKAGE).getEffectiveLevel();
    ndsuLevel = LogManager.getLogger(NDSU_PACKAGE).getEffectiveLevel();
    tapestryLevel = LogManager.getLogger(TAPESTRY_PACKAGE).getEffectiveLevel();
  }
  /**
   * Change log level as appropriate.
   */
  public void onSuccessFromRoot() {
    LogManager.getRootLogger().setLevel(rootLevel);
  }
  
  /**
   * Change log level as appropriate.
   */
  public void onSuccessFromApplication() {
    LogManager.getLogger(APPLICATION_PACKAGE).setLevel(applicationLevel);
  }
  
  /**
   * Change log level as appropriate.
   */
  public void onSuccessFromCayenne() {
    LogManager.getLogger(CAYENNE_PACKAGE).setLevel(cayenneLevel);
  }
  
  /**
   * Change log level as appropriate.
   */
  public void onSuccessFromNdsu() {
    LogManager.getLogger(NDSU_PACKAGE).setLevel(ndsuLevel);
  }
  
  /**
   * Change log level as appropriate.
   */
  public void onSuccessFromTapestry() {
    LogManager.getLogger(TAPESTRY_PACKAGE).setLevel(tapestryLevel);
  }
  
  /**
   * Encoder used to translate between selection Strings and selected Level value.
   * @return Encoder for Level translation.
   */
  public ValueEncoder<Level> getLevelEncoder() {
    return new ValueEncoder<Level>() {

      @Override
      public String toClient(Level value) {
        return value.toString();
      }

      @Override
      public Level toValue(String clientValue) {
        switch(clientValue) {
        case "ALL":
          return Level.ALL;
        case "DEBUG":
          return Level.DEBUG;
        case "ERROR":
          return Level.ERROR;
        case "FATAL":
          return Level.FATAL;
        case "INFO":
          return Level.INFO;
        case "OFF":
          return Level.OFF;
        case "TRACE":
          return Level.TRACE;
        case "WARN":
          return Level.WARN;
        default:
          return null;
        }
      }
    };
  }
}
