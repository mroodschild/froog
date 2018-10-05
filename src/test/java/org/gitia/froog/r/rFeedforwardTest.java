/*
 * The MIT License
 *
 * Copyright 2018 Matías Rodschild <mroodschild@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gitia.froog.r;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class rFeedforwardTest {
    
    public rFeedforwardTest() {
    }

    /**
     * Test of out method, of class rFeedforward.
     */
    @Test
    public void testOut() {
        System.out.println("out");
        double[] matrix = {0.4,0.5,0.6,0.7,0.8,0.9};
        int row = 2;
        int col = 3;
        rFeedforward net = new rFeedforward();
        net.addLayer(2, 2, "tansig");
        net.addLayer(2, 1, "purelim");
        
        double[] expResult = net.out(matrix, 2, 3);
        for (int i = 0; i < expResult.length; i++) {
            double d = expResult[i];
            System.out.printf("%.2f\t",d);
        }
        
        System.out.println("");
    }
    
}
