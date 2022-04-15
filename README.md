## 零. 基本概念

> SSM（Spring+SpringMVC+MyBatis）框架集由Spring、MyBatis两个开源框架整合而成（SpringMVC是Spring中的部分内容），常作为数据源较简单的web项目的框架。

页面发送请求给控制器，控制器调用业务层处理逻辑，逻辑层向持久层发送请求，持久层与数据库交互，后将结果返回给业务层，业务层将处理逻辑发送给控制器，控制器再调用视图展现数据。

> **意义:**
> 1. 通过Spring IOC容器管理第三方框架对象, 让框架形成整体
> 2. Spring/Spring MVC/MyBatis是目前的主流框架搭配具有掌握的必要
> 3. SSM的配置与使用是所有Java工程师必须掌握的技能

### 0.1 Spring

Spring就像是整个项目中装配bean的大工厂，在配置文件中可以指定使用特定的参数去调用实体类的构造方法来实例化对象。也可以称之为项目中的粘合剂。
Spring的核心思想是IoC（控制反转），即不再需要程序员去显式地`new`一个对象，而是让Spring框架帮你来完成这一切。

### 0.2 SpringMVC

SpringMVC在项目中拦截用户请求，它的核心Servlet即DispatcherServlet承担中介或是前台这样的职责，将用户请求通过HandlerMapping去匹配Controller，Controller就是具体对应请求所执行的操作。SpringMVC相当于SSH框架中struts。

### 0.3 mybatis

mybatis是对jdbc的封装，它让数据库底层操作变的透明。mybatis的操作都是围绕一个sqlSessionFactory实例展开的。mybatis通过配置文件关联到各实体类的Mapper文件，Mapper文件中配置了每个类对数据库所需进行的sql语句映射。在每次与数据库交互时，通过sqlSessionFactory拿到一个sqlSession，再执行sql命令。

## 一. 配置信息概览

### 1.1 项目文件结构图:

**项目搭建完成后的项目结构图:**

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415171619290-999204992.png)

**重点文件为: `applicationContext.xml` 和 `mybatis-config.xml`**

### 1.2 使用的框架版本:

  **Spring 5.3.17**

  **Spring MVC 5.3.17**

  **MyBatis 3.5.9**

## 二. Spring 与 Spring MVC 环境配置

### 2.1 新建一个标准的Maven Web项目

首先创建一个标准的 **Maven** 工程

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415175726356-412615961.png)

然后打开 **Project Structure** , 选择**Modules**选项卡, 点击 **+号**

选择 **Web** 组件, 然后修改 **Path 和 Resource路径** 为下图所示, 最后点击 **Create Artifact** :

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415180411402-1060256721.png)

导出文件夹名称可以修改为自己想要的, 其他的配置均不用修改, 然后点击 **ok**:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415180641545-1027704433.png)

最后配置 `Tomcat`, 打开 **Run/Debug Configurations**, 点击 **+号**, 选择 **Tomcat Server** 下的 **Local**:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415181415960-411682452.png)

之后点击 **Deployment**, 点击 **+号** 选择自己刚刚创建的 **Artifact**

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415181602481-1119653640.png)

一般来说, **Application context** 修改为 **"/"** (国际惯例😀), 之后点击 **ok**

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415181829100-1749339927.png)

这样, 一个标准的 **Maven Web** 项目就新建完成了

### 2.2 引入 Spring 和 Spring MVC 的依赖

在 `pom.xml` 文件中添加一些依赖:

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.17</version>
        </dependency>
    </dependencies>
```

一般来说 `spring-webmvc` 包下面的jar文件能够满足开发的绝大多数要求, 下面就是该包下引入的依赖:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415182130280-1267590043.png)

### 2.3 在 web.xml 文件中配置 DispatcherServlet

> DispatcherServlet是前置控制器。拦截匹配的请求，把拦截下来的请求，依据相应的规则分发到目标Controller来处理，是配置Spring MVC的第一步。

```xml
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>0</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

servlet的参数 `contextConfigLocation` 指向 `applicationContext.xml` 配置文件, 这个文件我们稍后创建

`<load-on-startup>` 标签设置为 0 表示: 这个servlet在web容器启动时就加载, 而不是访问时再加载

### 2.4 创建 applicationContext.xml 配置文件

在 **resources** 文件夹下新建一个 `annotationContext.xml` 文件, 内容如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

</beans>
```

之后Idea会智能提示**没有为此文件配置应用程序上下文**, 点击配置, 直接 **ok** 即可:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415183142981-352481104.png)


![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415183422187-956706263.png)

### 2.5 开启 Spring MVC 注解模式

在 `applicationContext.xml` 文件中添加以下配置:

```xml
    <!--开启spring的注解模式, 注解的扫描范围为com.ctgu包以及其子包下的文件-->
    <context:component-scan base-package="com.ctgu"/>
    <!--springMVC注解模式-->
    <mvc:annotation-driven/>
    <!--过滤静态资源,当springmvc servlet找不到资源时, 
	将请求转发给defaultServlet 排除图片/js/css等静态资源, 提高执行效率-->
    <mvc:default-servlet-handler/>
```
>关于 `<mvc:default-servlet-handler/>` 的一些解释:
tomcat容器的web.xml中有一个defaultServlet(用于处理静态资源)，映射路径是"/"，我们自定义的web.xml最终相当于会与容器的web.xml合并，而自定义DispatchServlet一般也是使用"/"，导致容器中的defaultServlet被覆盖，从而静态资源请求也会被发送到springmvc，springmvc会去找这个路径的映射器(相当于对应的Controller，这是找不到的)，配置这个 `<mvc:default-servlet-handler>`，就是在SpringMvc找不到映射路径后，再将其转给tomcat的defaultServlet，tomcat就可以正确解析静态资源路径。

### 2.6 配置请求和响应的字符集编码

配置字符集编码为 **UTF-8** 防止中文字符乱码

在 `web.xml` 文件中添加过滤器, 防止请求乱码:

```xml
    <filter>
        <filter-name>characterFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

在 `applicationContext.xml` 文件中修改 `<mvc:annotation-driven/>` 标签如下: 防止响应乱码:

```xml
    <mvc:annotation-driven>
        <!--4. 解决响应的中文乱码问题-->
        <mvc:message-converters>
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <property name="supportedMediaTypes">
                    <list>
                        <value>text/html;charset=utf-8</value>
                        <!--配置json字符串响应的字符串编码格式-->
                        <value>application/json;charset=utf-8</value>
                    </list>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
```

### 2.7 配置 FreeMarker 模板引擎

在 `pom.xml` 文件中的 `<dependencies>` 标签后面追加相关依赖:

```xml
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>2.3.31</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>5.3.18</version>
        </dependency>
```

导入相关依赖后, 在 `applicationContext.xml` 进行配置:

```xml
<!--    5. 配置freemarker模板引擎配置文件-->
    <!--    需要给 FreeMarkerViewResolver 设置一个 FreeMarkerConfig 的 bean 对象来定义 FreeMarker 的配置信息-->
    <bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
        <property name="templateLoaderPath" value="/WEB-INF/ftl"/>
        <property name="freemarkerSettings">
            <props>
                <!--设置响应到ftl文件中的字符的编码格式为utf-8-->
                <prop key="defaultEncoding">UTF-8</prop>
            </props>
        </property>
    </bean>
    <!--   创建视图解析器 -->
    <bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
        <!--使用fmt模板引擎产生的新的html页面的响应输出的类型为utf-8-->
        <property name="contentType" value="text/html;charset=utf-8"/>
        <property name="suffix" value=".ftl"/>
    </bean>
```
创建相应的目录和文件:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415191836929-1916512727.png)

### 2.8 配置 Json序列化 组件

在 `pom.xml` 文件中的 `<dependencies>` 标签后面追加相关依赖:

```xml
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.13.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.13.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.2.2</version>
        </dependency>
```

创建一个 `TestController` 类来测试 **FreeMarker 模板引擎**

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415192529546-353341778.png)

在运行 Tomcat 之前, 要将之前Maven导入的包全部 **Put into Output Root**

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415192909185-1789247078.png)

输入网址后能够正常显示, 就算成功:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415193058753-643162038.png)

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415193120024-1822608120.png)


## 三. Spring 与 Mybatis 整合配置

### 3.1 注入mybatis-spring依赖及JDBC驱动

在 `pom.xml` 文件中的 `<dependencies>` 标签后面追加相关依赖:

```xml
        <!--引入Mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.9</version>
        </dependency>
        <!--spring链接jdbc驱动-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.3.17</version>
        </dependency>
        <!--mybatis和spring整合的依赖包-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.6</version>
        </dependency>
        <!--mysql连接驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
        <!--数据库连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.2.8</version>
        </dependency>
```

### 3.2 配置 数据库连接池

在 `applicationContext.xml` 进行配置:

```xml
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="数据库连接"/>
        <property name="username" value="用户名"/>
        <property name="password" value="密码"/>
        <property name="initialSize" value="初始化连接数量"/>
        <property name="maxActive" value="最大连接数量"/>
    </bean>
```
### 3.3 配置 SqlSessionFactory 初始化 Mybatis

在 `applicationContext.xml` 进行配置:

```xml
 <bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations" value="classpath:mappers/*.xml"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>
```

### 3.4 创建 mybatis-config.xml 配置文件

在 **resources** 文件夹下新建一个 `mybatis-config.xml` 文件, 内容如下:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
 <!--自动驼峰命名转换-->
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>
```

在 **resources** 文件夹下新建一个 **mappers** 文件夹, 用来存放mapper文件

### 3.5 配置 Mapper 扫描器

在 `applicationContext.xml` 进行配置:

```xml
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.ctgu.ssm.mapper"/>
    </bean>
```

这段配置会扫描 com.ctgu.ssm.mapper 下的所有接口，然后创建各自接口的动态代理类。这样，Service就可以注入dao的实例了。

## 四. Spring 与 其他组件 整合配置

### 4.1 引入 Junit单元测试 框架

在 `pom.xml` 文件中的 `<dependencies>` 标签后面追加相关依赖:

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.17</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
```

引入 `javax.servlet-api` 是为了在测试的时候不会因为缺少改api而报错

### 4.2 创建测试相关文件

首先是数据库, 以及表文件:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415201114138-1637274928.png)

之后是以下三个文件:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415201315513-1228112446.png)

源码如下:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415201438289-882912038.png)

之后再创建测试类:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415201607818-580259154.png)

运行测试类, 运行结果如下:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415201859556-531960633.png)

查看数据库表信息, 成功插入数据:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415201929012-455468247.png)

### 4.3 logback日志输出

在 `pom.xml` 文件中的 `<dependencies>` 标签后面追加相关依赖:

```xml
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.11</version>
        </dependency>
```

在 **resources** 文件夹下新建一个 `logback.xml` 文件, 内容如下:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{HH:mm:ss} %-5level [%thread] %logger{30} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <root level="debug">
        <appender-ref ref="console"/>
    </root>
</configuration>
```

这个文件规定了日志输出的格式和什么级别的信息需要输出到控制台中

再次运行测试文件, 会发现多了许多细节:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415202405767-1103447828.png)

### 4.4 声明式事务

在 `applicationContext.xml` 进行配置:

```xml
    <!--创建事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--启用注解形式的事务控制-->
    <tx:annotation-driven/>
```

修改 `TestService` 类:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415203257328-1862082953.png)

主动制造异常, 并再次运行测试类:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415203503045-1407584076.png)

查看数据库表发现数据确实已经回滚, 并未部分插入

注释掉异常再次运行:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415203633420-423157300.png)

查看数据库表发现数据插入成功!

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415203717383-1123537201.png)

至此说明声明式事务配置完成

## 五. Spring 与 Mybatis-Plus 的整合配置

> MyBatis-Plus（简称 MP）是一个 MyBatis (opens new window)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

### 5.1 引入mybatis-plus依赖

在 `pom.xml` 文件中的 `<dependencies>` 标签后面追加相关依赖:

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus</artifactId>
            <version>3.5.1</version>
        </dependency>
```

### 5.2 Spring XML更改配置SqlSessionFactory实现类

在 `applicationContext.xml` 中修改:

```xml
<bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
```

改为Mybatis-Plus的FactoryBean:

```xml
<bean id="sessionFactory" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
```

### 5.3 mybatis-config.xml增加Mybatis-Plus分页插件

在 `mybatis-config.xml` 中配置:

```xml
<!--    添加mybatis-plus的分页插件-->
    <plugins>
        <plugin interceptor="com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor">
            <property name="@page" value="com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor"/>
        </plugin>
    </plugins>
```

### 5.4 测试 mybatis-plus 插件

创建 `Test` 实体类, 对应到数据库:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415212310694-625862865.png)

修改 `TestMapper` 接口:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415212435490-913620891.png)

修改 `TestMapper.xml` mapper文件:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415212519932-1404172712.png)

创建新的测试类 `MybatisPlusTest`:

![image](https://img2022.cnblogs.com/blog/2494807/202204/2494807-20220415212725999-1510315586.png)

能够顺利通过测试则, mybatis-plus配置成功
