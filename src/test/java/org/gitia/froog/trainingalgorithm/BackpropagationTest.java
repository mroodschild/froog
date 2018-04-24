/*
 * The MIT License
 *
 * Copyright 2018 Marilina.
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
package org.gitia.froog.trainingalgorithm;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Marilina
 */
public class BackpropagationTest {
    
    public BackpropagationTest() {
    }

    /**
     * Test of train method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testTrain_3args_1() {
        System.out.println("train");
        Feedforward net = null;
        SimpleMatrix input = null;
        SimpleMatrix output = null;
        Backpropagation instance = new Backpropagation();
        instance.train(net, input, output);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of train method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testTrain_3args_2() {
        System.out.println("train");
        Feedforward net = null;
        double[][] input = null;
        double[][] output = null;
        Backpropagation instance = new Backpropagation();
        instance.train(net, input, output);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testInit() {
        System.out.println("init");
        Backpropagation instance = new Backpropagation();
        instance.init();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loss method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testLoss() {
        System.out.println("loss");
        SimpleMatrix yCalc = null;
        SimpleMatrix yObs = null;
        Backpropagation instance = new Backpropagation();
        double expResult = 0.0;
        double result = instance.loss(yCalc, yObs);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of L2_reg method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testLossRegularization() {
        System.out.println("lossRegularization");
        Backpropagation instance = new Backpropagation();
        double expResult = 0.0;
        double result = instance.L2_reg(1);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNet method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testSetNet() {
        System.out.println("setNet");
        Feedforward net = null;
        Backpropagation instance = new Backpropagation();
        instance.setNet(net);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNet method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testGetNet() {
        System.out.println("getNet");
        Backpropagation instance = new Backpropagation();
        Feedforward expResult = null;
        Feedforward result = instance.getNet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInputTest method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testSetInputTest() {
        System.out.println("setInputTest");
        SimpleMatrix inputTest = null;
        Backpropagation instance = new Backpropagation();
        instance.setInputTest(inputTest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutputTest method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testSetOutputTest() {
        System.out.println("setOutputTest");
        SimpleMatrix outputTest = null;
        Backpropagation instance = new Backpropagation();
        instance.setOutputTest(outputTest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTestFrecuency method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testSetTestFrecuency() {
        System.out.println("setTestFrecuency");
        int testFrecuency = 0;
        Backpropagation instance = new Backpropagation();
        instance.setTestFrecuency(testFrecuency);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setClassification method, of class Backpropagation.
     */
    @Ignore
    @Test
    public void testSetClassification() {
        System.out.println("setClassification");
        boolean classification = false;
        Backpropagation instance = new Backpropagation();
        instance.setClassification(classification);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
