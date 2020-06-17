package com.example.myscala.utils.demo4

/**
  * Case 类的狗仔函数默认是 val，所以定义一个 case  类的字段。
  * 但是没有加 val 或者 var
  * 仍旧可以访问这个字段，就像和加了 val 一样
  */
object DemoCaseClass {
  def main(args:Array[String]) = {

    case class Person(name: String)
    val p = Person("程超")
    val name = p.name

    println(s"name = $name")

    // 实例化 case 类，
    // 实际是调用下面的 apply 方法
    val p2 = Person2("chengchao", 40)
    val p3 = Person2.apply("chengchao", 40)
    // 如果想给 case 类加一个新的"构造函数",得实现
    // apply 方法

  }
}

/**
  * 在类内部以 this 为名的方法定义辅助构造函数
  * 可以定义多个辅助构造函数，
  * 但是这些构造函数必须有不同的签名
  *
  * <ul>
  *   <li>构造函数必须用 this  命名 </li>
  *   <li>每个构造函数必须调用之前定义的构造函数开始 </li>
  *   <li>构造函数必须有不同的签名 </li>
  *   <li>一个构造函数可以通过 this 调用另一个不同的构造函数 </li>
  * </ul>
  */
class Pizza(var crustSize:Int, var crustType: String) {
  // one-arg auxiliary constructor
  def this(crustSize: Int) {
    this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
  }
  // one-arg auxiliary constructor
  def this(crustType: String) {
    this(Pizza.DEFAULT_CRUST_SIZE, crustType)
  }

  // zero-arg auxiliary constructor
  def this() {
    this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
  }

  override def toString = s"A $crustSize inch pizza "
}

object Pizza {
  val DEFAULT_CRUST_SIZE = 12
  val DEFAULT_CRUST_TYPE = "THIN"
}

/**
  * Case 类是一个会自动生成很多模板代码的特殊类
  *
  * 因为这个原因，在 case 类里添加一个辅助构造函数不同于普通类的做法。
  * 这是由于它们不是真正的构造函数：它们是类的半生对象中的 apply 方法。
  *
  *
  */
case class Person2(var name:String, var age:Int)

