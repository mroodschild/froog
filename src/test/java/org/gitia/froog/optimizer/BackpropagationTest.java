/*
 * Copyright 2018 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.froog.optimizer;

// import org.gitia.froog.optimizer.Backpropagation;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild
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
