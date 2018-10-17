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
package org.gitia.froog.lossfunction;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class RMSETest {

    public RMSETest() {
    }

    /**
     * Test of cost method, of class RMSE.
     */
    @Test
    public void testCost() {
        System.out.println("cost");
        double[] data = {0, 0, 1.0};
        double[] data1 = {0.1, 0.1, 0.8};
        SimpleMatrix Yobs = new SimpleMatrix(3, 1, true, data);
        SimpleMatrix Ycalc = new SimpleMatrix(3, 1, true, data1);
        //Ycalc.print();
        //Yobs.print();
        RMSE instance = new RMSE();
        double expResult = 0.34996355115805830;
        double result = instance.cost(Ycalc, Yobs);
//        System.out.println("cost: " + result);
//        System.out.println("costAll: " + instance.costAll(Ycalc, Yobs));
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of costAll method, of class RMSE.
     */
    @Test
    public void testCostAll() {
        System.out.println("costAll");
        double[] data = {
            0.1, 0.3, 0.1, 0.9,
            0.1, 0.5, 0.1, 0.05,
            0.8, 0.1, 0.20, 0.05};
        SimpleMatrix Ycalc = new SimpleMatrix(3, 4, true, data);
        double[] data1 = {
            0, 0, 0, 1,
            0, 1, 0, 0,
            1.0, 0, 1, 0};
        SimpleMatrix Yobs = new SimpleMatrix(3, 4, true, data1);
//        Ycalc.print();
//        Yobs.print();
        RMSE instance = new RMSE();
        double expResult = 0.360838144602255;
        double result = instance.costAll(Ycalc, Yobs);
//        System.out.println("result: " + result);
        assertEquals(expResult, result, 0.0);
    }

}
