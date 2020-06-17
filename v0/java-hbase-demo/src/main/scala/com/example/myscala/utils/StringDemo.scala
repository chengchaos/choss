package com.example.myscala.utils

import scala.util.matching.Regex

class StringDemo {


  def runDemo(): Unit = {

    val s1 = "Hello"
    val s2 = "Hello"

    val s3 = "H" + "ello"

    println(s1 == s2)

    println(s1 == s3)

    println(s1 == null)

    println(s1.toUpperCase)


    val foo =
      """this is a multiline
        #String
      """.stripMargin('#')

    println(foo)

    "hello, world, butter, Coco Puffs".split(",").map(_.trim).foreach(println)

    // 字符串中的变量代换
    // 在字符串前面加字母 's'
    // 在字符串前面添加字母 's' 时,其实是在创建一个处理字符串字面量
    // 还可以用花括号把表达式包起来在放到字符串中
    // s 其实是一个方法.
    val name = "Fred"
    val age = 33
    val weight = 200.00D

    println(s"$name is ${age + 1} years old, and weight $weight pounds.")
    println(s"You are 33 years old: ${ age == 33}")


    case class Student(name: String, score: Int)

    // 还可以打印对象
    val handong = Student("Handong", 95)
    println(s"${handong.name} has a score of ${handong.score}")

    // 字符串插值 f (printf 格式化)
    println(f"$name is $age years old, and weights $weight%.2f pounds.")

    val out = f"$name, you weight $weight%.0f pounds.";

    println(out)

    // raw 插入符
    println(raw"foo\nbar") // foo\nbar



  }

  def rundemo15(): Unit = {
    // 挨个处理字符串中的字符:

    val upper = "hello, world".map(c => c.toUpper)
    println(upper)
    val upper2 = "hello, world".map(_.toUpper);
    println(upper2)

    val upper3 = "hello, world".filter(_ != ',')
      .map(_.toUpper)

    for (c <- "hello") println(c)

    val upper4 = for (c <- "hello, world") yield c.toUpper

    val result = for {
      c <- "hello, world"
      if c != 'l'
    } yield c.toUpper

    "hello".foreach(println)

    val hello = "HELLO"

    hello.map(c => (c.toByte + 32).toChar)

    hello.map{c => (c.toByte + 32).toChar}

    def toLower(c: Char): Char = (c.toByte + 32).toChar
    val hello2 = hello.map(toLower)
    println(hello2)


    val hello3 = for (c <- hello) yield toLower(c)
    println(hello3)

    val toLower2 = (c: Char) => (c.toByte + 32).toChar

    hello.map(toLower2)
  }


  def rundemo2() : Unit = {
    // 判断一个字符串是否符合一个正则表达式
    // 在一个 String 上调用 .r 方法可以创建一个 Regex 对象,
    // 之后再场照是否含有一个匹配时就可以用  findFirstIn
    // 当需要查找是否完全匹配时可以用 findAllIn
    val numPattern = "[0-0]+".r
    val address = "123 Main Street Suite 101"

    val match1:Option[String] = numPattern.findFirstIn(address)
    val matches:Regex.MatchIterator = numPattern.findAllIn(address)
    val matchesAsArr:Array[String] = numPattern.findAllIn(address).toArray

    val match11 = match1.getOrElse("no match")
    println(match11)

    // 因为 Option 是 0 或者一个的元素集合, 有经验的 Scala 开发人员在这种情况下会使用
    // foreach 循环:
    numPattern.findFirstIn(address).foreach{ e =>
      println("e === "+ e)
    }

    match1 match {
      case Some(s) => println(s"Found: $s")
      case None =>
    }

    // 创造一个 Regex 对象

    import scala.util.matching.Regex

    val numPattern2 = new Regex("[0-0]+")



    println()
  }

  // 字符串中的替换模式
  def rundemo17() : Unit = {
    // 1. 可以在一个 String 上面调用 replaceAll
    val address = "123 Main Street"
    val address2 = address.replaceAll("[0-9]", "*")
    // 可以创建一个正则表达式,在正则表达式上调用 replaceAllIn 方法
    // 或者 replaceFirstIn
    val regex = "[0-9]".r
    val address3 = regex.replaceAllIn(address, "x")

  }

  // 抽取
  def rundemo18():Unit = {
    val address = ""
    val pattern = "([0-98])([A-Za-z])".r
    val pattern(count, fruit) = "1000 Bananas"
    println(s"count : $count, fruit: $fruit")
  }

  // 访问字符串中的一个字符
  def rundom19() : Unit = {
    val hello = "hello"
    var h = hello.charAt(0)
    val h2 = hello(0)

    println(s"1 = $h;  2 = ${h2}")
  }


  /**
    * 1.10 在 String 中添加自定义的方法:
    *
    * 一个隐式转换类必须定义在允许定义方法的范围内(不在最高层)
    * 隐式转换类必须定义在一个类或者对象或者包的内部.
    */
  def rundemo110() :Unit = {
    // 在 Scala 2.10 中,定义一个隐式转换, 然后再这个类里面定义一些方法
    implicit class StringImprovements(s: String) {
      def increment = s.map(c => (c + 1).toChar)
    }

    val result = "HAL".increment
  }


  def rundemo21() : Unit = {
    val x = "10"
    @throws(classOf[NumberFormatException])
    def toInt(s: String) = s.toInt

    def toInt2(s: String) : Option[Int] = {
      try {
        Some(s.toInt)
      } catch {
        case e: NumberFormatException => None
      }
    }
    val y = toInt2(x).getOrElse(0)
    val z = toInt2(x) match {
      case Some(m) => m
      case None => 0
    }

    println(s"$x, $y $z")

  }

  /**
    * 数值类型转换
    */
  def rundemo22() : Unit = {

    val b = 19.45.toInt
    val f = 19.toFloat

    val a = 1000L
    if (a.isValidByte) {
      println(a.toByte)
    }

    if (a.isValidShort) {
      println(a.toShort)
    }

    val d = 0: Byte
    val e = 0: Int
    val f2 = 0: Short

    val s = "Dave"
    val obj = s: Object
    val arr:Array[String] = Array("1", "2", "3")

    // var arr2 = arr: _*

    var name = null.asInstanceOf[String]

    val r = scala.util.Random
    val r2 = r.nextInt
    val r3 = r.nextInt(100)



  }
}
