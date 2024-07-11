package com.valoores.cassandra.config;
import org.apache.spark.sql.SparkSession;

public class SparkConfig {
	private static SparkSession spark;

	private SparkConfig() {
	}

	public static SparkSession getSparkSession() {
		if (spark == null) {
			synchronized (SparkConfig.class) {
//				if (spark == null) {
//					System.out.println(" spark == null ");
////					spark = SparkSession.builder().master("local")
//					spark = SparkSession.builder().master("spark://10.10.10.70:7077")
//
//				    .config("spark.cassandra.connection.host", "10.10.10.70")
//
////					.config("spark.executor.instances", "2")
//					.config("spark.executor.memory", "10G") 
//					.config("spark.driver.memory", "10G")
////                    .config("spark.executor.cores", "8") 
//					
//					.config("spark.sql.shuffle.partitions", "200")
//					.config("spark.default.parallelism", "10")
//	                .config("spark.sql.execution.arrow.pyspark.enabled", "true")  // Enable Arrow optimization
////					.config("spark.sql.catalog.casscatalog", "com.datastax.spark.catalog.CatalogConnector")
//					.config("spark.cassandra.auth.username", "cassandra")
//					.config("spark.cassandra.auth.password", "cassandra")
//				    .config("spark.task.cpus", "8") // Set the number of CPU cores per task
////				    .config("spark.jars","/u01/sb-cassandra/spark/cassandra-spark.jar,/u01/sb-cassandra/spark/spark-cassandra-connector-assembly_2.12-3.3.0.jar,/u01/sb-cassandra/spark/scala-library-2.12.11.jar")
//	                
//				    .config("spark.jars","/u01/sb-cassandra/spark/cassandra-spark.jar")
//
//				    .config("spark.sql.extensions","com.datastax.spark.connector.CassandraSparkExtensions")
//
//				    
//				    .config("spark.serializer", KryoSerializer.class.getName())
//					.appName("vcisN")
//					.getOrCreate();
//					
//				}
				
				
				
				
				
				if (spark == null) {
					System.out.println(" spark == null ");
//					spark = SparkSession.builder().master("spark://10.10.10.70:7077")
					spark = SparkSession.builder().master("local")
							
							
				    .config("spark.cassandra.connection.host", "10.10.10.70")
//						    .config("spark.cassandra.connection.host", "10.1.10.110")
					.config("spark.cassandra.auth.username", "cassandra")
					.config("spark.cassandra.auth.password", "cassandra")
//					.config("spark.executor.instances", "4")
					.config("spark.executor.memory", "50G") 
					.config("spark.driver.memory", "50G")
//					
//					.config("spark.executor.cores", "4")   
//					
					.config("spark.sql.shuffle.partitions", 200)
					.config("spark.default.parallelism", 10)
	                .config("spark.sql.execution.arrow.pyspark.enabled", "true")  // Enable Arrow optimization
					.config("spark.sql.catalog.casscatalog", "com.datastax.spark.catalog.CatalogConnector")

					
//				    .config("spark.task.cpus", "4") // Set the number of CPU cores per task
//				    .config("spark.serializer", KryoSerializer.class.getName())
				    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
			        .config("spark.dynamicAllocation.enabled", "true")

	               

					.appName("vcisN")
					.getOrCreate();
				}

			}
		} else {
			System.out.println("spark !! null");
		}
		return spark;
	}
}
