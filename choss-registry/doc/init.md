

## Server 端

**1: 依赖**

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
```

**2: 注解**

```
@SpringBootApplication
@EnableEurekaServer
```

**3: 配置文件**

```yaml
server:
  port: 8761
spring:
  application:
    name: choss-registry

eureka:
  instance:
    hostname: localhost
    client:
      registerWithEureka: false
      fetchRegistry: false
      serviceUrl:
        defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
    server:
      waitTimeInMsWhenSyncEmpty: 0
      enableSelfPreservation: false
    prefer-ip-address: true # 使用 IP 地址定义注册中心地址
```

## Client 端


**1: 依赖**

```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
```

**2: 注解**

```
@SpringBootApplication
@EnableDiscoveryClient
```

**3: 配置文件**

```yaml

server:
  port: 8081

spring:
  application:
    name: choss-worker
  profiles: dev


eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
    prefer-ip-address: true # 使用 IP 地址定义注册中心地址
```
