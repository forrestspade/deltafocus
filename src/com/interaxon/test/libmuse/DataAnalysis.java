package com.interaxon.test.libmuse;

import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.test.libmuse.DataBuffer;

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
    public DataBuffer alpScoreDB = new DataBuffer();
    public DataBuffer betScoreDB = new DataBuffer();
    public DataBuffer delScoreDB = new DataBuffer();
    public DataBuffer theScoreDB = new DataBuffer();
    public DataBuffer gamScoreDB = new DataBuffer();

    // Create an empty training set
    //Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 5);
    Instances isTrainingSet = new Instances("Rel", step1(), 10);
    public int classCounter = 0;
    public boolean ready = false;

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
    public FastVector step1() {
        // ArrayList<Double> to some element in fvWekaAttributes.
        //alpScorDB.dataSamples;

        // Declare two numeric attributes
        Attribute alpScoreAttr = new Attribute(“alpScore”);
        Attribute betSoreAttr = new Attribute(“betScore”);
        Attribute delScoreAttr = new Attribute(“delScore”);
        Attribute theScoreAttr = new Attribute(“theScore”);
        Attribute gamScoreAttr = new Attribute(“gamScore”);

        // Declare a nominal attribute along with its values
        /*FastVector fvNominalVal = new FastVector(3);
        fvNominalVal.addElement(“blue”);
        fvNominalVal.addElement(“gray”);
        fvNominalVal.addElement(“black”);
        Attribute Attribute3 = new Attribute(“aNominal”, fvNominalVal);*/

        // Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement(“green”);
        fvClassVal.addElement(“red”);
        Attribute ClassAttribute = new Attribute(“theClass”, fvClassVal);

        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector(6);
        fvWekaAttributes.addElement(alpScoreAttr);
        fvWekaAttributes.addElement(betScoreAttr);
        fvWekaAttributes.addElement(delScoreAttr);
        fvWekaAttributes.addElement(theScoreAttr);
        fvWekaAttributes.addElement(gamScoreAttr);
        fvWekaAttributes.addElement(ClassAttribute);
        return fwWekaAttributes;
    }

    pubilic Instances step2(String classname) {
        FastVector fvWekaAttributes = step1();

        // Set class index
        this.isTrainingSet.setClassIndex(classCounter); // means the 4th index

        // Create the instance
        Instance iExample = new DenseInstance(6);
        //iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), alpScoreDB.dataSamples.get(alpScoreDB.dataSamples.size-1));
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), (Double) alpScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), (Double) betScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), (Double) delScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), (Double) theScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), (Double) gamScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(5), classname);
        //return iExample;

    // for each sensor instance
        // add the instance
        this.isTrainingSet.add(iExample);
        if (this.classCounter > 9) {
            this.step3();
            this.ready = true;
        } else{
            this.classCounter++;
        }

    }


    // TODO: Function to train a classifier
    // see http://weka.wikispaces.com/Programmatic+Use
    public String step3(){ //Instances isTrainingSet) {
        // Create a naïve bayes classifier
        Classifier cModel = (Classifier)new NaiveBayes();
        cModel.buildClassifier(this.isTrainingSet);

        // Test the model (OPTIONAL)
        Evaluation eTest = new Evaluation(this.isTrainingSet);
        eTest.evaluateModel(cModel, this.isTestingSet);

        // Print the result à la Weka explorer:
        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);

        // Get the confusion matrix
        double[][] cmMatrix = eTest.confusionMatrix();
        return strSummary;
    }

    // TODO
    public Double step4amountOfAnger() {
        // Specify that the instance belong to the training set
        // in order to inherit from the set description

        // Create the instance
        Instance iUse = new DenseInstance(6);
        //iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), alpScoreDB.dataSamples.get(alpScoreDB.dataSamples.size-1));
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), (Double) alpScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), (Double) betScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), (Double) delScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), (Double) theScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), (Double) gamScoreDB.pop().getValues()[15]);
        iExample.setValue((Attribute)fvWekaAttributes.elementAt(5), classname);

        iUse.setDataset(isTrainingSet);

        // Get the likelihood of each classes
        // fDistribution[0] is the probability of being “positive”
        // fDistribution[1] is the probability of being “negative”
        double[] fDistribution = cModel.distributionForInstance(iUse);
        return fDistribution[1]-fDistribution[0];
    }
}