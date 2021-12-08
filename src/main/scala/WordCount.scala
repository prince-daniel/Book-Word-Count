import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object WordCount {

  //counts the word
  def countWords(lines: RDD[String]): RDD[(String,Int)] = {
    var words = lines.flatMap(line => line.split("\\W+"))
      .map(word => (word.toLowerCase(),1))
      .reduceByKey(_ + _)
    words
  }

  //Sorts the word count in ascending order
  def sortWordCountByAscending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._2)
      .collect()
      .foreach(println)
  }

  //Sorts the word count in descending order
  def sortWordCountByDescending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._2,false)
      .collect()
      .foreach(println)
  }

  //Sorts the word in ascending order
  def sortWordByAscending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._1)
      .collect()
      .foreach(println)
  }

  //Sorts the word in descending order
  def sortWordByDescending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._1,false)
      .collect()
      .foreach(println)
  }

  //Prints the word count result
  def print(result: RDD[(String, Int)]): Unit = {
    result.collect()
      .foreach(println)
  }

  //lookup word in the book
  def wordLookup(word: String, result: RDD[(String, Int)]) = {
    val lookUp = result.filter(elem => elem._1.equalsIgnoreCase(word))
    if (lookUp.isEmpty()){
      println(s"${word} isn't present in the book.")
    }else{
      lookUp.collect().foreach(w => println(s"${w._1} has ${w._2} occurence(s)."))
    }
  }

  def searchWord(word: String, result:RDD[(String,Int)]) = {
    result.filter(w => w._1.contains(word))
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    var sc = new SparkContext("local[*]","bookCount")
    //<book> with .txt file extension
    val book = "My-Queen--A-Weekly-J-Lurana-Waterhou-.txt"
    var lines = sc.textFile(s"book/$book")

    //counts the words in the book
    val result = countWords(lines)

    sortWordByAscending(searchWord("love",result))

  }
}
