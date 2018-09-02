package com.finnzhanchen.songgo;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;


// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({SongParserInstrumentedTest.class,
                     PlacemarkParserInstrumentedTest.class,
                    UIButtonExistanceInstrumentedTest.class,
                    UIGamePropertyGeorgeSquareInstrumentedTest.class,
                    UIGamePropertyMyCurrentLocationInstrumentedTest.class,
                    UIGamePropertyWallStreetInstrumentedTest.class,
                    UISuperpower1InstrumentedTest.class,
                    UISuperpower2InstrumentedTest.class,
                    UIUserNamePersonalisationInstrumentedTest.class,
                    UIWinInstrumentedTest.class})
public class InstrumentedTestSuite {

}


