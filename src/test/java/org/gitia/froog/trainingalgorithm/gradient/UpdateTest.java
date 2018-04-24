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
import org.gitia.froog.trainingalgorithm.updaterule.Update;
import org.gitia.froog.trainingalgorithm.updaterule.UpdateRule;
import org.gitia.froog.transferfunction.TransferFunction;
import org.junit.Test;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class UpdateTest {

    public UpdateTest() {
    }

    /**
     * Test of compute method, of class Gradient.
     */
    @Test
    public void testCompute() {
        double[][] w1 = {
            {-0.00615039, 0.0169021},
            {-0.02311792, 0.03137121},
            {-0.0169217, -0.01752545},
            {0.00935436, -0.05018221}};
        SimpleMatrix W1 = new SimpleMatrix(w1);

        double[][] b1 = {{-8.97523455e-07}, {8.15562092e-06}, {6.04810633e-07}, {-2.54560700e-06}};
        SimpleMatrix B1 = new SimpleMatrix(b1);

        double[][] w2 = {{-0.0104319, -0.04019007, 0.01607211, 0.04440255}};
        SimpleMatrix W2 = new SimpleMatrix(w2);

        double[][] b2 = {{9.14954378e-05}};
        SimpleMatrix B2 = new SimpleMatrix(b2);

        double[][] gW1 = {
            {0.00023322, -0.00205423},
            {0.00082222, -0.00700776},
            {-0.00031831, 0.0028636},
            {-0.00092857, 0.00809933}};

        double[][] gB1 = {{1.05570087e-07}, {-3.81814487e-06}, {-1.90155145e-07}, {5.46467802e-07}};
        double[][] gW2 = {{-1.75740039e-05, 3.70231337e-03, -1.25683095e-03, -2.55715317e-03}};
        double[][] gB2 = {{-1.08923140e-05}};

        List<SimpleMatrix> gradW = new ArrayList<>();
        gradW.add(new SimpleMatrix(gW1));
        gradW.add(new SimpleMatrix(gW2));
        List<SimpleMatrix> gradB = new ArrayList<>();
        gradB.add(new SimpleMatrix(gB1));
        gradB.add(new SimpleMatrix(gB2));

        Feedforward net = new Feedforward();
        net.addLayer(new Layer(W1, B1, TransferFunction.LOGSIG));
        net.addLayer(new Layer(W2, B2, TransferFunction.LOGSIG));

        UpdateRule update = new Update();
        update.updateParameters(net, 1, 0, 1.2, gradW, gradB);

        for (Layer l : net.getLayers()) {
            l.getW().print("%.8f");
            l.getB().print("%.8f");
        }

    }

}
