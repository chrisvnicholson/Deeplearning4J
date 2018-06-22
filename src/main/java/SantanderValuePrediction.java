import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.analysis.columns.ColumnAnalysis;
import org.datavec.api.transform.analysis.columns.LongAnalysis;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.writable.Writable;
import org.datavec.spark.transform.AnalyzeSpark;
import org.datavec.spark.transform.misc.StringToWritablesFunction;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SantanderValuePrediction {

    private static Logger log = LoggerFactory.getLogger("SantanderValuePrediction.class");


    public static void main(String[] args) throws IOException, InterruptedException, IllegalAccessException, InstantiationException {

        System.setProperty("hadoop.home.dir","C:/Users/Rahul_Raj05/Downloads/winutils-master/winutils-master/hadoop-3.0.0/");
        Schema schema = new Schema.Builder()
                                  .addColumnsString("ID")
                                  .addColumnLong("target")
                                  .addColumnsDouble("col_%d",0,4990)
                                  .build();
        TransformProcess transformProcess = new TransformProcess.Builder(schema)
                                                                .removeColumns("ID")
                                                                .build();
        RecordReader recordReader = new CSVRecordReader(1,',');
        recordReader.initialize(new FileSplit(new ClassPathResource("train.csv").getFile()));
        TransformProcessRecordReader transformProcessRecordReader = new TransformProcessRecordReader(recordReader,transformProcess);


/*
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[*]");
        sparkConf.setAppName("Santander App");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> directory = javaSparkContext.textFile(new ClassPathResource("train.csv").getFile().getParent());
        JavaRDD<List<Writable>> parsedData = directory.map(new StringToWritablesFunction(new CSVRecordReader()));

        DataAnalysis dataAnalysis = AnalyzeSpark.analyze(schema,parsedData);
        System.out.println("args = [" + dataAnalysis + "]");
*/
       // List<Writable> record = transformProcessRecordReader.next();

        TransformProcessRecordReader temp = transformProcessRecordReader;
        double[] max = new double[4992];
        double[] min = new double[4992];
        while(temp.hasNext()){
               List<Writable> record = temp.next();
               for(int i=1;i<=4991;i++){
                     max[i]=Math.max(max[i],record.get(i).toDouble());
                     min[i]=Math.min(min[i],record.get(i).toDouble());
               }
        }
        int count=0;
        for(int i=1;i<=4991;i++){
            if(max[i]==min[i]){
                count++;
            }
        }
        System.out.println("args = [" + count + "]");





/*        TransformProcessRecordReader transformProcessRecordReader = new TransformProcessRecordReader(recordReader,transformProcess);
        //System.out.println("args = [" + recordReader.next().get(1).toString() + "]");
        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(transformProcessRecordReader,10,0,0,true);
        System.out.println("args = [" + dataSetIterator.next()+ "]");
        */


     //   RecordReader transformProcessRecordReader = new TransformProcessRecordReader(recordReader,transformProcess);

      //  int batchSize=10;
      //  DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader,10,1,2,true);
      //  System.out.println("args = [" + dataSetIterator.next() + "]");

    }
}
