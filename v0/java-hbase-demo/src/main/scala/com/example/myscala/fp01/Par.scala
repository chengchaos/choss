package com.example.myscala.fp01

/**
  * <p>
  * <strong>
  * 用一句话描述功能
  * </strong><br /><br />
  * 如题。
  * </p>
  *
  * @author chengchaos[as]Administrator - 2019/8/19 14:47 <br />
  * @see 【相关类方法】
  * @since 1.1.0
  */
class Par[A] {

  private var value:A = _


}


object Par {

  def unit[A](a: => A) : Par[A] = {
    val p: Par[A] =  new Par[A]()
    p.value = a
    p
  }

  def get[A](a: Par[A]) : A = {
    a.value
  }
}
