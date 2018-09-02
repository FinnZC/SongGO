package com.finnzhanchen.songgo;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;


// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({AccumulateUnitTest.class,
                     SongUnitTest.class,
                     PlacemarkUnitTest.class})
public class UnitTestSuite {

}


