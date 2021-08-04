package by.kozlov.iba

import org.apache.spark.sql.SparkSession

object Configuration {
  val ss = SparkSession
    .builder()
    .appName("ibaTask")
    .config("spark.hadoop.fs.stocator.scheme.list", "cos")
    .config("fs.stocator.cos.impl", "com.ibm.stocator.fs.cos.COSAPIClient")
    .config("fs.cos.impl", "com.ibm.stocator.fs.ObjectStoreFileSystem")
    .config("fs.stocator.cos.scheme", "cos")
    .config("fs.cos.sparkobject123.access.key", sys.env("access.key"))
    .config("fs.cos.sparkobject123.endpoint", sys.env("endpoint"))
    .config("fs.cos.sparkobject123.secret.key", sys.env("secret.key"))
    .config("fs.cos.service.v2.signer.type", "false")
    .getOrCreate()
  val inputDB = ss.read.format("jdbc")
    .option("username", sys.env("dbUsername"))
    .option("password", sys.env("dbPassword"))
    .option("driver", "com.ibm.db2.jcc.DB2Driver")
//    .option("dbtable","(SELECT * FROM PRT00338.product_record WHERE ((MOD(year, product_id) >= 1 and MOD(year, product_id) < 3000)")
//    .option("partitionColumn","mod")
//    .option("lowerBound","1")
//    .option("upperBound","3000")
    .option("numPartitions","15")
    .option("partitionColumn","year")
    .option("lowerBound","2017")
    .option("upperBound","2019")
    .option("url", "jdbc:db2://dashdb-txn-sbox-yp-dal09-14.services.dal.bluemix.net:50000/BLUDB:user=" + sys.env("dbUsername") + ";PWD=" + sys.env("dbPassword") + ";Security=SSL;")
    .option("dbtable", "PRT00338.product_record")
    .load()
}
