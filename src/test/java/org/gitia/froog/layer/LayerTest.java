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
package org.gitia.froog.layer;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.transferfunction.TransferFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LayerTest {

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_doubleArr() {
        System.out.println("output");
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data1 = {0.1, 0.2};
        SimpleMatrix w = new SimpleMatrix(2, 3, true, data);
        SimpleMatrix b = new SimpleMatrix(2, 1, true, data1);
        double[] input = {0.2, 0.4, 0.6};
        Layer instance = new Layer(w, b, TransferFunction.LOGSIG);
        double[] expResult = {0.5938731029341, 0.6984652160025};
        double[] result = instance.output(input).getDDRM().getData();
        assertArrayEquals(expResult, result, 0.0000000000001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_SimpleMatrix() {
        System.out.println("output");
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data1 = {0.1, 0.2};
        double[] data2 = {0.2, 0.4, 0.6};
        SimpleMatrix w = new SimpleMatrix(2, 3, true, data);
        SimpleMatrix b = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix input = new SimpleMatrix(3, 1, true, data2);
        Layer instance = new Layer(w, b, TransferFunction.LOGSIG);
        double[] expResult = {0.5938731029341, 0.6984652160025};
        double[] result = instance.output(input).getDDRM().getData();
        instance.output(input).print();
        assertArrayEquals(expResult, result, 0.0000000000001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_SimpleMatrixAll() {
        System.out.println("output");
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data1 = {0.1, 0.2};
        double[] data2 = {0.2, 0.4, 0.6, 0.2, 0.4, 0.6};
        SimpleMatrix w = new SimpleMatrix(2, 3, true, data);
        SimpleMatrix b = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix input = new SimpleMatrix(3, 2, false, data2);
        Layer instance = new Layer(w, b, TransferFunction.LOGSIG);
        double[] expResult = {0.5938731029341, 0.5938731029341, 0.6984652160025, 0.6984652160025};
        double[] result = instance.output(input).getDDRM().getData();
        assertArrayEquals(expResult, result, 0.0000000000001);
    }

    /**
     * Test of outputN method, of class Layer.
     */
    @Test
    @Ignore
    public void testOutputN() {
        System.out.println("outputN");
        SimpleMatrix z = null;
        Layer instance = new Layer();
        SimpleMatrix expResult = null;
        SimpleMatrix result = instance.outputN(z);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of outputZ method, of class Layer.
     */
    @Test
    @Ignore
    public void testOutputZ() {
        System.out.println("outputZ");
        SimpleMatrix a = null;
        Layer instance = new Layer();
        SimpleMatrix expResult = null;
        SimpleMatrix result = instance.outputZ(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
