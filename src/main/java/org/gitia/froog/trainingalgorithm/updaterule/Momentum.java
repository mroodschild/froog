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
package org.gitia.froog.trainingalgorithm.updaterule;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class Momentum implements UpdateRule {

    List<SimpleMatrix> gradW_prev = new ArrayList<>();
    List<SimpleMatrix> gradB_prev = new ArrayList<>();
    double momentum = 0;

    public Momentum() {
    }

    /**
     *
     * @param gradW
     * @param gradB
     */
    @Override
    public void init(List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
        gradB_prev.addAll(gradB);
        gradW_prev.addAll(gradW);
    }

    /**
     *
     * @param net
     * @param m
     * @param L2_lambda
     * @param learningRate
     * @param gradW
     * @param gradB
     */
    @Override
    public void updateParameters(Feedforward net, double m, double L2_lambda, double learningRate, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
//        gradW.get(1).print();
//        gradW_prev.get(1).print();
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer layer = net.getLayers().get(i);
            SimpleMatrix W = layer.getW();
            SimpleMatrix B = layer.getB();
            SimpleMatrix reg = W.scale(L2_lambda);
            SimpleMatrix vW = gradW_prev.get(i).scale(momentum).minus(gradW.get(i).plus(reg).scale(learningRate));
            W = W.plus(vW);
            SimpleMatrix vB = gradB_prev.get(i).scale(momentum).minus(gradB.get(i).scale(learningRate));
            B = B.plus(vB);
            layer.setW(W);
            layer.setB(B);
            net.getLayers().set(i, layer);
            gradW_prev.set(i, vW);
            gradB_prev.set(i, vB);
        }
    }

    @Override
    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

}