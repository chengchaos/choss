
## What is CHOSS

CHOSS is the Choss HBase Object Storage Service



### Development Env configuration:

**HDFS**


hdfs-site.xml


```xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>

        <property>
            <name>dfs.namenode.name.dir</name>
            <value>file:///works/data/hadoop/dfs/name</value>
        </property>

        <property>
            <name>dfs.datanode.data.dir</name>
            <value>file:///works/data/hadoop/dfs/data</value>
        </property>
    </configuration>
```

core-site.xml

```xml
    <configuration>

        <property>
            <name>hadoop.tmp.dir</name>
            <value>file:///works/data/hadoop</value>
        </property>

        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://0.0.0.0:9000</value>
        </property>
    </configuration>
```


```bash
$ bin/hdfs namenode -format
$ sbin/start-dfs.sh 
```


**HBase**



先复制 hadoop 的两个 xml 文件到 HBase 的 conf 目录


```bash
$ cp /works/hadoop/etc/hadoop/core-site.xml .
$ cp /works/hadoop/etc/hadoop/hdfs-site.xml .
```

编辑 hbase-env.sh

```
export JAVA_HOME=/usr/java/default
# Tell HBase whether it should manage it's own instance of Zookeeper or not.
# export HBASE_MANAGES_ZK=true


```


编辑 hbase-site.xml 


```xml
    <configuration>

        <property>
            <name>hbase.rootdir</name>
            <value>hdfs://localhost:9000/hbase</value>
        </property>

        <property>
            <name>hbase.zookeeper.property.dataDir</name>
            <value>/works/data/hadoop/zookeeper</value>
        </property>

        <property>
            <name>hbase.cluster.distributed</name>
            <value>true</value>
        </property>
		
    </configuration>

```

启动 HBase

```bash
$ bin/start-hbase.sh
$ hbase shell
2020-02-16 13:46:22,608 WARN  [main] util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/works/hbase-1.2.11/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/works/hadoop-2.6.0-cdh5.7.0/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
HBase Shell; enter 'help<RETURN>' for list of supported commands.
Type "exit<RETURN>" to leave the HBase Shell
Version 1.2.11, rca53d58f5b7abde0c189c9f78baf4246bddffac3, Fri Feb 15 18:12:16 CST 2019

hbase(main):001:0> status
1 active master, 0 backup masters, 1 servers, 0 dead, 1.0000 average load



```


## DB

user_info

| field | type | description | 
| ---- | ---- | ---- |
| user_id | bigint| primary key |
| user_name | varchar(50) | user name union |
| password | varch4(255) | password  |
| detail_info | varchar(255) | description |
| create_time | timestamp | as the title |


token_info

| field | type | description | 
| ---- | ---- | ---- |
| token | varchar(32) | primary key |
| expire_itme | int(11) | expire time, default 7 day |
| refresh_time | timestamp | .. |
| active | tinyint | .. |
| creator | bigint | user_id |
| create_time | timestamp | create time |

auth:

| field | type | description | 
| ---- | ---- | ---- |
| bucket_name | varchar(32) | bucket name |
| target_token | varchar(32) | ... |
| auth_time | timestamp | auth time |


bucket_info :

| field | type | description | 
| ---- | ---- | ---- |
| bucket_id | varchar(32) | primary key |
| bucket_name | varchar(32) | bukket name |
| detail | varchar(255) | description |
| creator | varchar(32) | creator name |
| create_time | timestamp | create time |

### 

```sql

CREATE DATABASE `choss` /*!40100 COLLATE 'utf8mb4_general_ci' */

use `choss`;

CREATE TABLE `user_info` (
	`user_id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(50) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`detail` VARCHAR(255) NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`user_id`),
	UNIQUE INDEX `user_name` (`user_name`)
)
COMMENT='user_info'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `token_info` (
	`token` VARCHAR(50) NOT NULL,
	`is_active` INT NOT NULL,
	`expire_time` BIGINT NOT NULL,
	`user_id` BIGINT NOT NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`refresh_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`token`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `bucket_info` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NOT NULL,
	`bucket_name` VARCHAR(50) NOT NULL,
	`detail` VARCHAR(50) NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)
COMMENT='bucket_info'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `auth_info` (
	`bucket_id` BIGINT NOT NULL,
	`token` VARCHAR(50) NOT NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`bucket_id`, `token`)
)
COMMENT='auth_info'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```






 