package com.example.myscala

import com.example.myscala.utils.StringDemo
import org.springframework.boot.SpringApplication

object MyScalaApplicationRunner {


  def main(args: Array[String]): Unit = {

    SpringApplication.run(classOf[MyScalaApplication], args: _*)


    val sd: StringDemo = new StringDemo

    sd.runDemo()
  }
}
