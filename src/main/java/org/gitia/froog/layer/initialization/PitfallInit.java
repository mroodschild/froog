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
package org.gitia.froog.layer.initialization;

import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class PitfallInit implements WeightInit {

    Random random;

    /**
     * <b>Pitfall</b>: all zero initialization. Lets start with what we should
     * not do. Note that we do not know what the final value of every weight
     * should be in the trained network, but with proper data normalization it
     * is reasonable to assume that approximately half of the weights will be
     * positive and half of them will be negative. A reasonable-sounding idea
     * then might be to set all the initial weights to zero, which we expect to
     * be the “best guess” in expectation. This turns out to be a mistake,
     * because if every neuron in the network computes the same output, then
     * they will also all compute the same gradients during backpropagation and
     * undergo the exact same parameter updates. In other words, there is no
     * source of asymmetry between neurons if their weights are initialized to
     * be the same.
     *
     * @param matrix
     */
    @Override
    public void init(SimpleMatrix matrix) {
        matrix.zero();
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }
    
    @Override
    public String toString() {
        return "pitfall";
    }

}
