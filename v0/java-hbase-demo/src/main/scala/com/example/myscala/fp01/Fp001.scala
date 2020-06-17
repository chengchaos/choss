package com.example.myscala.fp01

/**
  * <p>
  * <strong>
  * 用一句话描述功能
  * </strong><br /><br />
  * 如题。
  * </p>
  *
  * @author chengchaos[as]Administrator - 2019/8/19 14:14 <br />
  * @see 【相关类方法】
  * @since 1.1.0
  */
class Fp001 {


  def sum001(seqi: Seq[Int]) : Int = {

    if (seqi.isEmpty) {
      0
    } else {
//      seqi.foldLeft(0) (_ + _)
      seqi.sum
    }

  }

  def sum002(seqi: IndexedSeq[Int]) : Int = {

    if (seqi.size <= 1) {
      val x = seqi.headOption getOrElse 0
      println(s"get ... $x")
      x
    } else {
      val (l , r) = seqi.splitAt(seqi.size /2 )
      sum002(l) + sum002(r)
    }
  }

  def sum003(seqi: IndexedSeq[Int]) : Int = {
    if (seqi.size <=1) {
      seqi.headOption getOrElse 0
    } else {
      val (l, r) = seqi.splitAt(seqi.size / 2)
      val sumL : Par[Int] = Par.unit(sum003(l))
      val sumR : Par[Int] = Par.unit(sum003(r))
      Par.get(sumL) + Par.get(sumR)
    }

  }

}

object Fp001 {

  def main(args: Array[String]): Unit = {

    val obj = new Fp001
    val seqi = Seq(1, 2, 3, 4, 5)
    val sum1 = obj.sum001(seqi)

    println(s"sum1 -=> $sum1")

    val seqi2 = IndexedSeq(1, 2, 3, 4, 5)
    val sum2 = obj.sum002(seqi2)

    println(s"sum2-=> $sum2")

    val seqi3 = IndexedSeq(1, 2, 3, 4, 5)
    val sum3 = obj.sum003(seqi3)

    println(s"sum3 -=> $sum3")
  }
}
