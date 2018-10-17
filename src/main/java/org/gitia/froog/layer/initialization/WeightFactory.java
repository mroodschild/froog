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

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class WeightFactory {

    /**
     * Implemented Functions:<br>
     * "default","smallrandom","positiverandom","he", "pitfall"
     *
     * @param init
     * @return
     */
    public static WeightInit getFunction(String init) {

        switch (init) {
            case WeightInit.DEFAULT:
                return new DefaultInit();
            case WeightInit.SMALLRANDOM:
                return new SmallRandomInit();
            case WeightInit.POSITIVERANDOM:
                return new PositiveRandomInit();
            case WeightInit.PITFALL:
                return new PitfallInit();
            case WeightInit.HE:
                return new HeInit();
            default:
                System.err.println("The init: '" + init + "' no exist, "
                        + "please probe with other init.");
                System.exit(0);
        }
        return null;
    }
}
