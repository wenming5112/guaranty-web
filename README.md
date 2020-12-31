Guaranty Admin
==============

接口完成度
-----

- [x] 用户注册
- [x] 用户登录
- [x] 实名认证
- [x] 修改密码
- [x] 文件上传



Maven Setting
-------------
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <!--local repo-->
	<localRepository>E:/wzm_maven_repo</localRepository>

	<pluginGroups>
	</pluginGroups>

	<proxies>
	</proxies>
	
    <servers>
        <server>
            <id>public-repo</id>
            <username>mvn123</username>
            <password>1234566</password>
        </server>
		
		<server>
		  <id>releases-repo</id>
		  <username>mvn123</username>
		  <password>1234566</password>
		</server>

		<server>
		  <id>snapshots-repo</id>
		  <username>mvn123</username>
		  <password>1234566</password>
		</server>
    </servers>
	
	<mirrors>
        <!-- 阿里云仓库 -->
        <mirror>
            <id>ali maven</id>
            <mirrorOf>*</mirrorOf>
            <name>ali maven</name>
            <url>https://maven.aliyun.com/repository/public/</url>
        </mirror>

        <mirror>
            <id>public-repo</id>
            <mirrorOf>*</mirrorOf>
            <name>Nexus osc</name>
            <url>http://nexus.ushengame.com/repository/maven-public/</url>
        </mirror>
	</mirrors>

</settings>
```


部署说明
========

