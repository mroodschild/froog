/*
 * The MIT License
 *
 * Copyright 2017 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
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
package org.gitia.froog;

import org.gitia.froog.layer.Layer;
import org.ejml.simple.SimpleMatrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roodschild, Matias <mroodschild@gmail.com>
 */
public class LayerTest {

    public LayerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of outputZ method, of class Layer.
     */
    @Test
    public void testOutputZ() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);

        Layer layer = new Layer(w1, b1, "tansig");

        double[] data2 = {0.2, 0.6};
        SimpleMatrix entrada = new SimpleMatrix(2, 1, true, data2);
        double[] esperado = {0.64, 0.4};
        assertArrayEquals(esperado, layer.outputZ(entrada).getDDRM().getData(), 0.0001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_SimpleMatrix() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        double[] data2 = {0.2, 0.6};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix a = new SimpleMatrix(2, 1, true, data2);

        Layer instance = new Layer(w1, b1, "tansig");

        double[] esperado = {0.5648995528462, 0.3799489622552};
        assertArrayEquals(esperado, instance.output(a).getDDRM().getData(), 0.0000001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_doubleArr() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);
        double[] a = {0.2, 0.6};
        
        Layer instance = new Layer(w1, b1, "tansig");

        double[] esperado = {0.5648995528462, 0.3799489622552};
        assertArrayEquals(esperado, instance.output(a).getDDRM().getData(), 0.0000001);
    }

}
