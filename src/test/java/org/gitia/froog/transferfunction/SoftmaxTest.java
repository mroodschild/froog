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
package org.gitia.froog.transferfunction;

import org.gitia.froog.transferfunction.TransferFunction;
import org.gitia.froog.transferfunction.FunctionFactory;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SoftmaxTest {

    /**
     * Test of output method, of class Softmax.
     */
    @Test
    public void testOutput() {
        TransferFunction softmax = FunctionFactory.getFunction("softmax");
        SimpleMatrix z = new SimpleMatrix(5, 1, true,
                -3, -1, 0, 1, 3
        );
        double[] salida = softmax.output(z).getMatrix().getData();
        double[] esperado = {
            0.002055492,
            0.015188145,
            0.04128566,
            0.112226059,
            0.829244644};
        //softmax.output(z).print();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of output method, of class Softmax.
     */
    @Test
    public void testOutput2() {
        TransferFunction softmax = FunctionFactory.getFunction("softmax");
        SimpleMatrix z = new SimpleMatrix(3, 1, true,
                1, 2, 3
        );
        double[] expResult = {0.090030573, 0.244728471, 0.665240956};
        double[] result = softmax.output(z).getMatrix().getData();
        //softmax.output(z).print();
        assertArrayEquals(expResult, result, 0.000000001);
    }

    /**
     * Test of derivative method, of class Softmax.
     */
    @Test
    public void testDerivative() {

    }

    /**
     * Test of toString method, of class Softmax.
     */
    @Test
    public void testToString() {
        TransferFunction tansig = FunctionFactory.getFunction("softmax");
        String expResult = "softmax";
        String result = tansig.toString();
        assertEquals(expResult, result);
    }

}
