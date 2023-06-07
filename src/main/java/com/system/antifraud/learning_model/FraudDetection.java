package com.system.antifraud.learning_model;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;



@Component
public class FraudDetection {

    public J48 tree;

    public List<CheckFraudRequest> transactions;

    public FraudDetection() throws Exception {
        tree = new J48();
        tree.buildClassifier(getTrainingData());
    }

    public String detectFraud(CheckFraudRequest transaction) throws Exception {

        Instance instance = new DenseInstance(6);
        instance.setValue(0, transaction.getSuspicious() > 0 ? 1 : 0);
        instance.setValue(1, !transaction.getTranCountry().equals(transaction.getSenderCountry()) ? 1 : 0);
        instance.setValue(2, !transaction.getTranRegion().equals(transaction.getSenderRegion()) ? 1 : 0);
        instance.setValue(3, transaction.getTime() < 120 ? 1 : 0);
        instance.setValue(4, transaction.getTranSum() > transaction.getSenderAvgSum() ? 1 : 0);
        instance.setDataset(getTrainingData());
        // Классификация экземпляра данных
        double result = tree.classifyInstance(instance);

        if (result == 0) {
            System.out.println("Transaction is safe");
            return "green";
        } else if (result == 1) {
            System.out.println("Transaction is suspicious");
            return "yellow";
        } else {
            System.out.println("Transaction is fraud");
            return "red";
        }
    }

    public Instances getTrainingData() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\DELL\\Desktop\\Учёба\\ПРИм-121\\ВКР\\antifraud\\target\\classes\\static\\training_data.arff"));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);

        return data;
    }

    public String checkTransaction(String json) throws Exception {
        Gson gson = new Gson();

        System.out.println("checkTransaction "+json);
        CheckFraudRequest transaction = gson.fromJson(json,CheckFraudRequest.class);
        return detectFraud(transaction);
    }


}