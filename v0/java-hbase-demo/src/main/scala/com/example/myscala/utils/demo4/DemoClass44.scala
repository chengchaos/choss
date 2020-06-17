package com.example.myscala.utils.demo4

/**
  * 定义私有的主构造函数:
  *
  * 在类名和构造函数接受的任意参数之间插入一个 private 关键字
  * 就可以创建私有的主构造函数
  */
class DemoClass44 private {

}

class DemoClass442 private (name: String) {


}

/**
  * Scala 中实现单例模式的方法就是把主构造函数变为私有,
  * 然后在类的伴生对戏中实现一个 getInstance 方法.
  */
object DemoClass442 {
  val demoClass442 = new DemoClass442("chenghao")
  def getInstance: DemoClass442 = demoClass442
}
