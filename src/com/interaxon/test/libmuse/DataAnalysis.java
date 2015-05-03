package com.interaxon.test.libmuse;

import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.test.limbuse.DataBuffer;

// buildClassifier(Instances instances)
//
// Data needs to be formatted as Instance to be Weka classified
// https://github.com/rjmarsan/Weka-for-Android/blob/master/src/weka/core/Instance.java

public class DataAnalysis
{
    //public DataBuffer eegDB = new DataBuffer();
    //public DataBuffer accDB = new DataBuffer();
    //public DataBuffer alpRelDB = new DataBuffer();
    //public DataBuffer battDB = new DataBuffer();
    public DataBuffer alpScorDB = new DataBuffer();
    public DataBuffer betScorDB = new DataBuffer();
    public DataBuffer delScorDB = new DataBuffer();
    public DataBuffer theScorDB = new DataBuffer();
    public DataBuffer gamScorDB = new DataBuffer();

    public void receiveMuseDataPacket(MuseDataPacket p) {
        switch (p.getPacketType()) {
            /*case EEG:
                //updateEeg(p.getValues());
                eegDB.receive(p);
                break;
            case ACCELEROMETER:
                //updateAccelerometer(p.getValues());
                accDB.receive(p);
                break;
            case ALPHA_RELATIVE:
                //updateAlphaRelative(p.getValues());
                alpRelDB.receive(p);
                break;*/
            /*case BATTERY:
                fileWriter.addDataPacket(1, p);
                // It's library client responsibility to flush the buffer,
                // otherwise you may get memory overflow.
                if (fileWriter.getBufferedMessagesSize() > 8096)
                    fileWriter.flush();
                break;*/
            case ALPHA_SCORE:
                alpScorDB.receive(p);
                break;
            case BETA_SCORE:
                betScorDB.receive(p);
                break;
            case DELTA_SCORE:
                delScorDB.receive(p);
                break;
            case THETA_SCORE:
                theScorDB.receive(p);
                break;
            case GAMMA_SCORE:
                gamScorDB.receive(p);
                break;
            default:
                break;
        }
    }

    // TODO: Function to convert data into Instance
    // TODO: Function to combine each sensor Instance into Instances
    public Instances aggregateInstances() {
        // ArrayList<Double> to some element in fvWekaAttributes.
        alpScorDB.dataSamples;

        // Declare two numeric attributes
        Attribute Attribute1 = new Attribute(“firstNumeric”);
        Attribute Attribute2 = new Attribute(“secondNumeric”);

        // Declare a nominal attribute along with its values
        /*FastVector fvNominalVal = new FastVector(3);
        fvNominalVal.addElement(“blue”);
        fvNominalVal.addElement(“gray”);
        fvNominalVal.addElement(“black”);
        Attribute Attribute3 = new Attribute(“aNominal”, fvNominalVal);*/

        // Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement(“positive”);
        fvClassVal.addElement(“negative”);
        Attribute ClassAttribute = new Attribute(“theClass”, fvClassVal);

        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector(4);
        fvWekaAttributes.addElement(Attribute1);
        fvWekaAttributes.addElement(Attribute2);
        fvWekaAttributes.addElement(Attribute3);
        fvWekaAttributes.addElement(ClassAttribute);

        // Create the instance
        Instance iExample = new DenseInstance(4);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), 1.0);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), 0.5);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(2), "gray");
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), "positive");
        return iExample;

        // Create an empty training set
        Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);

        // for each sensor instance
        // add the instance
        isTrainingSet.add(iExample);

        // Set class index
        isTrainingSet.setClassIndex(3);
        return isTrainingSet;
    }

    // TODO: Function to train a classifier
    // see http://weka.wikispaces.com/Programmatic+Use
    public void trainClassifier1(Instances isTrainingSet) {
        // Create a naïve bayes classifier
        Classifier cModel = (Classifier)new NaiveBayes();
        cModel.buildClassifier(isTrainingSet);
    }

    // TODO:
    public void testClassifier1() {
        // Test the model
        Evaluation eTest = new Evaluation(isTrainingSet);
        eTest.evaluateModel(cModel, isTestingSet);

        // Print the result à la Weka explorer:
        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);

        // Get the confusion matrix
        double[][] cmMatrix = eTest.confusionMatrix();
    }

    // TODO
    public void useClassifier1() {
        // Specify that the instance belong to the training set
        // in order to inherit from the set description
        iUse.setDataset(isTrainingSet);

        // Get the likelihood of each classes
        // fDistribution[0] is the probability of being “positive”
        // fDistribution[1] is the probability of being “negative”
        double[] fDistribution = cModel.distributionForInstance(iUse);
    }
}