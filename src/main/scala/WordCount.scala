import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object WordCount {

  /** counts all the words present in the book
   * @param lines takes the lines of the book as an input
   * @return returns the count of all the words
   */
  def countWords(lines: RDD[String]): RDD[(String,Int)] = {
    var words = lines.flatMap(line => line.split("\\W+"))
      .map(word => (word.toLowerCase(),1))
      .reduceByKey(_ + _)
    words
  }

  /** Sorts the word in the ascending order
   * @param wordsCount RDD containing the word count of the book
   */
  def sortWordCountByAscending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._2)
      .collect()
      .foreach(println)
  }

  /** Sorts the word count in the descending order
   * @param wordsCount RDD containing the word count of the book
   */
  def sortWordCountByDescending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._2,false)
      .collect()
      .foreach(println)
  }

  /** Sorts the word in the ascending order
   * @param wordsCount RDD containing the word count of the book
   */
  def sortWordByAscending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._1)
      .collect()
      .foreach(println)
  }

  /** Sorts the word in the descending order
   * @param wordsCount RDD containing the word count of the book
   */
  def sortWordByDescending(wordsCount: RDD[(String,Int)]): Unit = {
    wordsCount.sortBy(word => word._1,false)
      .collect()
      .foreach(println)
  }

  /** prints a RDD
   * @param result takes an RDD of (String,Int)
   */
  def print(result: RDD[(String, Int)]): Unit = {
    result.collect()
      .foreach(println)
  }

  /** looks up a specific word in the book
    * @param word the word which you want to lookup
    * @param result word count result of the book
   */
  def wordLookup(word: String, result: RDD[(String, Int)]): Unit = {
    // word -> the word which you want to look up in the book
    // if the word is present, it outputs the word and it's occurences. If not, prints out saying the word isn't present in the book.
    val lookUp = result.filter(elem => elem._1.equalsIgnoreCase(word))
    if (lookUp.isEmpty()){
      println(s"${word} isn't present in the book.")
    }else{
      lookUp.collect().foreach(w => println(s"${w._1} has ${w._2} occurence(s)."))
    }
  }

  /** searches and gives a list of words containing your desired word
   * @param word the word which you want to search
   * @param result word count result of the book
   * @return returns a list of words containing the input 'word'
   */
  def searchWord(word: String, result:RDD[(String,Int)]) = {
    // word -> the word which you want to search in the book
    // prints out a list of words containing your input 'word'
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

    //use the 'result' as a parameter in the defined functions
    // (searchWord, wordLookup, sortWordByDescending. sortWordByAscending, sortWordCountByAscending, sortWordCountByDescending) for your desired operations

  }
}
