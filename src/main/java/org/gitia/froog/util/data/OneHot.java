package org.gitia.froog.util.data;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class OneHot {

    LabelEncoder<String> labelEncoder;

    /**
     * inicializamos OneHot con las etiquetas a utilizar, remueve las etiquetas
     * duplicadas
     *
     * @param words
     */
    public OneHot(String[] words) {
        String[] tags = removeDuplicates(words);
        labelEncoder = new LabelEncoder<String>(tags);
    }

    /**
     * devuelve la cantidad de etiquetas encontradas
     *
     * @return
     */
    public int getNumberOfClasses() {
        return labelEncoder.getNumClasses();
    }

    /**
     * binariza el codigo de la etiqueta
     *
     * @param label
     * @return
     */
    public double[] encode(double label) {
        double[] oneHot = new double[getNumberOfClasses()];
        oneHot[(int) label] = 1;
        return oneHot;
    }

    /**
     * convierte la etiqueta de un String a binario
     *
     * @param label
     * @return
     */
    public double[] encode(String label) {
        return this.encode(labelEncoder.encode(label));
    }

    /**
     * esperamos un array de binarios con las etiquetas pasadas
     *
     * @param labels
     * @return
     */
    public double[] encode(String[] labels) {
        double[] result = new double[getNumberOfClasses() * labels.length];
        int pos = 0;
        for (int i = 0; i < labels.length; i++) {
            String tag = labels[i];
            double[] code = this.encode(tag);
            System.arraycopy(code, 0, result, pos, code.length);
            pos += code.length;
        }
        return result;
    }

    /**
     * convierte el binario a etiqueta
     *
     * @param oneHot
     * @return
     */
    public double decode(double[] oneHot) {
        return ArrayUtils.indexOf(oneHot, 1);
    }

    /**
     * convierte el binario a etiqueta
     *
     * @param oneHot
     * @return
     */
    public String tag(double[] oneHot) {
        return labelEncoder.decode((int) this.decode(oneHot));
    }

    public String tag(double code) {
        return labelEncoder.decode((int) code);
    }

    /**
     * Mantiene el orden de ocurrencia de las palabras
     *
     * @param words
     * @return
     */
    private String[] removeDuplicates(String[] words) {
        Set<String> wordSet = new LinkedHashSet<>(Arrays.asList(words));
        String[] wordArray = wordSet.toArray(new String[0]);
        return wordArray;
    }
}