package luxe.chaos.utils


/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/2 16:49 <br />
 * @since [产品模块版本]
 * @see [相关类方法]
 *
 */
class PasswdCreator {

  def createPwd(strLength: Int): String = {

    val upps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lows = "abcdefghijklmnopqrstuvwxyz"

    val nums = "1234567890"
    //    val oths = """"!@#$%^&*()-_=+\\|`~[{]};:\'\",<.>/?"""
    val oths = "!@#$%^&*()-_=+{}[];:,.<>/?"

    val total = upps + lows + nums + oths;

    //    println("upps lenght: "+ upps.length)
    //    println("lows lenght: "+ lows.length)
    //    println("nums lenght: "+ nums.length)
    //    println("oths length: "+ oths.length)
    //    println("total length : "+ total.length)



    val len = if (strLength > 6) strLength else 6

    val target = new Array[Char](len)

    target(0) = upps(scala.util.Random.nextInt(upps.length))
    target(1) = upps(scala.util.Random.nextInt(upps.length))

    for (i <- 2 to len - 3) {
      target(i) = total(scala.util.Random.nextInt(total.length))
    }

    target(len - 2) = nums(scala.util.Random.nextInt(nums.length))
    target(len - 1) = nums(scala.util.Random.nextInt(nums.length))

    new String(target)

  }

}

object PasswdCreator {

  def main(args:Array[String]) :Unit = {

    val str = new PasswdCreator().createPwd(24)

    println(s"result is :\n$str")
  }
}