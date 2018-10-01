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
package org.gitia.froog.trainingalgorithm.accelerate;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class MomentumRumelhart implements Accelerate {

    private double beta;

    List<SimpleMatrix> vW = new ArrayList<>();
    List<SimpleMatrix> vB = new ArrayList<>();

    public MomentumRumelhart() {
    }

    public MomentumRumelhart(double beta) {
        this.beta = beta;
    }

    @Override
    public void grad(List<SimpleMatrix> dW, List<SimpleMatrix> db, double learningRate) {
        if (vW.isEmpty()) {
            init(vW, dW);
            init(vB, db);
        }

        for (int i = 0; i < dW.size(); i++) {
            SimpleMatrix vdW = vW.get(i);
            SimpleMatrix vdb = vB.get(i);

            vdW = vdW.scale(beta).plus(dW.get(i).scale(learningRate));
            vdb = vdb.scale(beta).plus(db.get(i).scale(learningRate));
            
            vW.set(i, vdW);
            vB.set(i, vdb);

            dW.set(i, vdW);
            db.set(i, vdb);
        }
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getBeta() {
        return beta;
    }

    /**
     *
     * @param v
     * @param grad
     */
    private void init(List<SimpleMatrix> v, List<SimpleMatrix> grad) {
        for (int i = 0; i < grad.size(); i++) {
            SimpleMatrix vAux = grad.get(i).copy();
            vAux.zero();
            v.add(vAux);
        }
    }

}
