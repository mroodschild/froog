/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.trainingalgorithm.gradient;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;
import org.gitia.froog.transferfunction.TransferFunction;
import org.junit.Test;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class GradientTest {

    public GradientTest() {
    }

    /**
     * Test of compute method, of class StandardGradient.
     */
    @Test
    public void testCompute() {
        double[][] w1 = {
            {-0.00416758, -0.00056267},
            {-0.02136196, 0.01640271},
            {-0.01793436, -0.00841747},
            {0.00502881, -0.01245288}};
        SimpleMatrix W1 = new SimpleMatrix(w1);

        double[][] w2 = {
            {-0.01057952, -0.00909008, 0.00551454, 0.02292208}};
        SimpleMatrix W2 = new SimpleMatrix(w2);

        double[][] a1 = {
            {-0.00616578, 0.0020626, 0.00349619},
            {-0.05225116, 0.02725659, -0.02646251},
            {-0.02009721, 0.0036869, 0.02883756},
            {0.02152675, -0.01385234, 0.02599885}};
        SimpleMatrix A1 = new SimpleMatrix(a1);

        double[][] a2 = {
            {0.5002307, 0.49985831, 0.50023963}};
        SimpleMatrix A2 = new SimpleMatrix(a2);

        List<SimpleMatrix> Activations = new ArrayList<>();
        Activations.add(A1);
        Activations.add(A2);

        Feedforward net = new Feedforward();
        net.addLayer(new Layer(W1, W1, TransferFunction.TANSIG));
        net.addLayer(new Layer(W2, W2, TransferFunction.TANSIG));
        
        double[][] x = {{1.62434536, -0.61175641, -0.52817175},
        {-1.07296862, 0.86540763, -2.3015387}};
        double[][] y = {{1, 0, 1}};
        SimpleMatrix X = new SimpleMatrix(x);
        SimpleMatrix Y = new SimpleMatrix(y);

        List<SimpleMatrix> gradW = new ArrayList<>();
        gradW.add(W1);
        gradW.add(W1);
        List<SimpleMatrix> gradB = new ArrayList<>();
        gradB.add(W1);
        gradB.add(W1);
        
        StandardGradient instance = new StandardGradient();
        instance.compute(net, Activations, gradW, gradB, X, Y);
        for (int i = 0; i < gradB.size(); i++) {
            SimpleMatrix gB = gradB.get(i);
            SimpleMatrix gW = gradW.get(i);
            System.out.println("gW"+i);
            gW.print("%.8f");
            System.out.println("gB"+i);
            gB.print("%.8f");
        }
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

}
