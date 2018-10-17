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
