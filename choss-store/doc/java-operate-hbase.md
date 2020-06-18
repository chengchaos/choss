
## Java 操作 HBase 常见类

**HBaseConfiguration**

HBase 配置类，用于设置基本属性，比如  zookeeper 地址，超时时间等。


**HBaseAdmin**

HBase 管理 Admin 类，可以通过这个实例进行表的创建和删除等操作。


**Table**

HBase Table 操作类

**Put**

HBase 添加操作数据模型


Get

HBase 单个查询操作数据模型


Scan

HBase Scan 检索操作数据模型


Result

HBase 查询的结构哦类型

ResultScanner

HBase 检索结果模型



### 依赖

```xml
		<dependency>
			<groupId>org.apache.hbase</groupId>
			<artifactId>hbase-client</artifactId>
			<version>1.2.4</version>
			<exclusions>
				<exclusion>
					<groupId>io.netty</groupId>
					<artifactId>netty-all</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

```

### Hadoop 环境变量


Windows 的用户，需要下载 ：


然后设定环境变量：

```
HADOOP_HOME=C:\works\local\jvm\hadoop-common-2.2.0
CLASSPATH=%CLASSPATH%;%HADOOP_HOME\bin\winutils.exe
PATH=%PATH%;%HADOOP_HOME%\bin
```


### protobuf 版本问题

抛出类似的异常： `ClassNotFoundException: com.google.protobuf.LiteralByteString`

参考这里：

https://www.cnblogs.com/ao-xiang/p/10871558.html

```xml
		<dependency>
			<groupId>org.apache.hbase</groupId>
			<artifactId>hbase-shaded-client</artifactId>
			<version>${hbase.version}</version>
			<exclusions>
				<exclusion>
					<groupId>io.netty</groupId>
					<artifactId>netty-all</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
```