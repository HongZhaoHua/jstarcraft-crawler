package com.jstarcraft.dataset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jstarcraft.ai.data.DataSpace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MovieLensTestCase {

    @Autowired
    @Qualifier("movieLensDataSpace")
    private DataSpace movieLensDataSpace;
    
    @Test
    public void testDataset() {
        System.out.println(movieLensDataSpace);
    }
    
}
