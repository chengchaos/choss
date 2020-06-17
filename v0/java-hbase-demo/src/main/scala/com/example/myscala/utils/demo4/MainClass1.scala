package com.example.myscala.utils.demo4

/**
  * Scala 类的主构造函数是以下的组合：
  *
  * <ul>
  * <li>构造函数参数</li>
  * <li>在类内部被调用的方法</li>
  * <li>在类内部执行的语句和表达式</li>
  * </ul>
  *
  * 构造函数字段可见性
  *
  * <ul>
  *   <li>如果 var 则生成 getter 和 setter 方法</li>
  *   <li>固若 val 则只生成 getter 方法</li>
  *   <li>如果没有 var 和 val 则不会生成 getter 和 setter </li>
  *   <li>var 和 val 可以被 private 修饰，可以防止生成  getter 和 setter 方法</li>
  *
  * </ul>
  *
  *
  *
  */

object MainClass1 {
  def main(args: Array[String]) = {
    val p = new Person("han", "dong")
    p.firstName = "韩冬"
    p.firstName_$eq("韩冬2")

    p.lastName = "高继原"
    p.age = 40
    val lastName = p.lastName
    println(s"lastName = $lastName")

    println("=====")
    // 在类体重定义的方法，实际上会在主构造方法重调用

    println("=== in le  MainClass1 : " + p)
  }
}

class Person(var firstName: String, var lastName: String) {

  // firstName 和 lastName
  // 是 private 的，但是 scala 为他们生成了
  // 访问（accessor）方法和
  // 修改（mutator）方法
  // 可以使用 person.firstName = "xxx"
  // val lastName = persion.lastName
  // 修改方法的名字是：
  // void firstName_$eq(String firstName) ...

  println("the constructor begins")

  // some class fields
  private val HOME = System.getProperty("user.home")
  var age = 0

  // some methods
  override def toString = s"$firstName $lastName is $age years old"

  def printHome: Unit = {
    println(s"HMOE = $HOME")
  }

  def printFullName: Unit = {
    println(toString)

  }

  printHome
  printFullName
  println("Still in the constructor")


}
