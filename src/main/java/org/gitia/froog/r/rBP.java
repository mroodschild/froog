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
package org.gitia.froog.r;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.lossfunction.LossFunction;
import org.gitia.froog.trainingalgorithm.Backpropagation;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class rBP {
    Backpropagation bp;

    public rBP() {
        bp = new Backpropagation();
    }
    
    public void configure(int epoch, String lossFunction){
        bp.setEpoch(0);
        bp.setLossFunction(LossFunction.RMSE);
    }
    
    public void train(Feedforward net, double[] in, int row_in, int col_in, double[] out, int row_out ,int col_out){
        SimpleMatrix input = new SimpleMatrix(row_in, col_in, true, in);
        SimpleMatrix output = new SimpleMatrix(row_out, col_out, true, out);
        bp.train(net, input, output);
    }
    
}
