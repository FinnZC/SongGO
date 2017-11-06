package com.finnzhanchen.songgo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void data_structure_test_expandableViewAdapter() throws Exception{
        ArrayList<ArrayList<String>> childNames =
                new ArrayList<ArrayList<String>>(5);
        childNames.add(new ArrayList<String>());
        childNames.get(0).add("String");
        assertEquals("String", childNames.get(0).get(0));
        //assertTrue(true);
    }


}