/*
 * Copyright 2022 
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
package org.gitia.froog;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.Dense;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Autoencoder implements NeuralNetwork{

    List<Dense> encoder = new ArrayList<>();
    List<Dense> decoder = new ArrayList<>();

    public Autoencoder(){

    }

    @Override
    public SimpleMatrix output(double[] input) {
        return null;
    }

    @Override
    public SimpleMatrix output(SimpleMatrix input) {
        return null;
    }

    @Override
    public List<Dense> layers() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
