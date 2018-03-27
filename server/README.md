# 服务器端部署指南

以下是服务器端代码部署指南，详细介绍了代码部署到服务器并运行的方法，如果有不理解的地方，欢迎邮件：[i@xlui.me](mailto:i@xlui.me) 或者 Issues。

> 注：安装指南不会讲如何登录服务器以及如何使用 ssh，这种基本操作需要自己百度。

## 1. 安装配置 Tomcat

到 [Apache Tomcat](https://tomcat.apache.org/) 下载最新的 Tomcat，并解压到安装位置。

```bash
wget http://mirrors.shu.edu.cn/apache/tomcat/tomcat-9/v9.0.5/bin/apache-tomcat-9.0.5.tar.gz
tar xzvf apache-tomcat-9.0.5.tar.gz
mv apache-tomcat-9.0.5 /usr/local/tomcat
```

修改 Tomcat 默认的配置文件 `/usr/local/tomcat/conf/server.xml`：

```xml
<Service name="FlippedClassroom"><!-- 修改 Service 名字 -->
    <Connector port="8081" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
    <!-- 修改 HTTP 端口 -->
    <Connector port="8010" protocol="AJP/1.3" redirectPort="8443" />
    <!--修改 AJP 端口 -->
    <Engine name="FlippedClassroom" defaultHost="localhost">
        <!-- 修改 Engine name -->
        <Realm className="org.apache.catalina.realm.LockOutRealm">
            <Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"/>
        </Realm>
        <Host name="localhost"  appBase="flippedclassroom" unpackWARs="true" autoDeploy="true">
            <!-- 修改 appBase 属性值，即现在项目部署的地址应该是 /usr/local/tomcat/flippedclassroom -->
            <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="flippedclassroom_log" suffix=".txt" pattern="%h %l %u %t &quot;%r&quot; %s %b" />
            <!-- 修改日志的 prefix，使其更有辨识度 -->
        </Host>
    </Engine>
</Service>
```

## 2. 安装 Java

```bash
yum install java -y
```

## 3. 创建数据库

安装 MySQL 或者 MariaDB，创建数据库 `flippedclassroom` 并授予权限：

```mysql
create database flippedclassroom character set utf8;
use flippedclassroom;
grant all privileges on flippedclassroom.* to admin@localhost identified by 'admin';
flush privileges;
```

切记，创建数据库的时候编码设置为 UTF-8，如果不设置，Hibernate 自动生成表的时候就会选择编码 Latin1，会导致插入或者查询中文的时候出现 JpaSystemException 异常。

数据库端口默认为 3306，如果有修改应该在项目的配置文件 `server/src/main/resources/application.properties` 中修改：

```yml
spring.datasource.url=jdbc:mysql://localhost:3306/flippedclassroom
```

为：

```yml
spring.datasource.url=jdbc:mysql://localhost:新端口号/flippedclassroom
```

## 4. 安装 Docker 并配置 Redis

项目使用 Redis 来缓存唯一 Token，而源码安装 Redis 配置方面有许多不方便的地方，所以我们采用 Docker。

安装 Docker：

```bash
yum install docker
systemctl start docker
systemctl enable docker
```

配置使用 USTC 镜像（加速）：

[https://lug.ustc.edu.cn/wiki/mirrors/help/docker](https://lug.ustc.edu.cn/wiki/mirrors/help/docker)

安装 Redis：

```bash
docker pull redis
```

启动 Redis：

```bash
docker run --name redis -p 6379:6379 --restart=always -d redis
```

这样就配置了 Docker 自启动和 Redis 自启动，关于 Docker 更多命令请自行百度。

## 5. 打包项目并上传

默认的打包方式就是 war，所以只需要在 Intellij IDEA 中点击 Build -> Build Artifacts -> server: war 即会在目录 `server/target` 下生成 `server-项目版本号.war` 文件，将该文件上传至服务器。

可以使用 scp 来上传：

```bash
scp -P 服务器ssh端口号 /path/to/FlippedClassroom/server/target/server-0.0.1-SNAPSHOT.war USERNAME@SERVER_IP:
```

如果需要多次部署，可以通过脚本来简化服务器部署流程：

```bash
#!/bin/bash
depPath=/usr/local/tomcat/flippedclassroom
snap='server-0.0.1-SNAPSHOT'

if [ $? -ne 0 ]
then
    echo "You must be root to run this sceipt!"
    exit 1
fi

rm ${depPath}/ROOT -rf
unzip ~/${snap}.war -d ${depPath}/ROOT
rm -f ~/${snap}.war
# 需要先配置好 supervisor，如果没有，请修改为命令手动重启 tomcat
supervisorctl restart tomcat
sleep 5
```

## 6. 创建必要文件夹并设置权限

项目默认保存上传的文件到 `/var/www/uploads/` 下，所以需要创建对应的目录并授予权限。

创建目录：

```bash
mkdir -p /var/www/uploads/avatar
mkdir -p /var/www/uploads/course/preview
mkdir -p /var/www/uploads/course/edata
mkdir -p /var/www/uploads/course/picture
```

确保你当前的用户（运行项目的用户）对 `/var/www` 有写入（write）权限，如果没有，请授予。

另一种更好的方式是创建 nologin 用户：

```bash
useradd -M -s /usr/bin/nologin www  # 添加 nologin 用户 www
chown -R www:www /var/www           # 更改 /var/www 及其子目录的所属用户与组
chmod -R 775 /var/www               # 更改 /var/www 及其子目录的权限，允许 www 组用户写入
usermod -a -G www CURRENTUSER       # 将 CURRENTUSER 加入 www 组，这样 CURRENTUSER 就可以在 /var/www 中写入/删除新文件
```

## 7. 部署并配置 supervisor 接管 Tomcat

虽然 Tomcat 也会自动解压 war 文件，但是它自动解压的速度太慢，我们使用 unzip 手动解压：

```bash
unzip server-0.0.1-SNAPSHOT.war -d /usr/local/tomcat/flippedclassroom/ROOT
rm -f server-0.0.1-SNAPSHOT.war
```

手动情况下不能很好的管理 Tomcat，我们要寻求更加自动化的方式，这里我选择了 supervisor。supervisor 是 Python 写的一个进程控制软件，它可以很好的控制进行的启动、停止、重启、日志记录等。

安装 Supervisor：

```bash
yum install supervisor
```

> 如果无法搜到 supervisor，可能是没有启用扩展源 EPEL。使用命令：`yum install epel-release` 安装 EPEL 源后再尝试安装 supervisor。

创建 Tomcat 的配置文件 `/etc/supervisord.d/tomcat.ini`:

```ini
[program:tomcat]
directory=/usr/local/tomcat
command=/usr/local/tomcat/bin/catalina.sh run
autostart=true
autorestart=true
redirect_stderr=true
stdout_logfile=/opt/tomcat.log
stdout_logfile_maxbytes=5MB
stdout_logfile_backups=10
```

这样 Tomcat 的启动、停止等就会被 supervisor 接管，启动 supervisor：

```bash
supervisord -c /etc/supervisord.conf
```

设置 supervisor 开机自启：

```bash
systemctl enable supervisord
```

重启 Tomcat 的命令：

```bash
supervisorctl restart tomcat
```

关于 supervisorctl，是 supervisor 提供给我们的一个简化管理的工具，更多内容请看：[https://xlui.me/t/supervisor/](https://xlui.me/t/supervisor/)

## 8. Nginx 反向代理

如果需要部署到域名下，通过 nginx 进行反向代理是最佳的方案。以下给出 nginx 反向代理的配置（/path/to/nginx/conf/vhost/domain_name.conf）：

```nginx
location / {
    proxy_pass http://127.0.0.1:8081/;
    proxy_redirect off;
    proxy_set_header    Host                $host;
    proxy_set_header    X-Real-IP           $remote_addr;
    proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
    proxy_set_header    X-Forwarded-Proto   $scheme;
}

#location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$
#{
#    expires      30d;
#}

#location ~ .*\.(js|css)?$
#{
#    expires      12h;
#}
```

`proxy_pass` 的地址需要是 tomcat 配置文件中的 http 端口。

重启 nginx，就可以通过域名访问项目了。

## 9. 结束

以上，就可以成功使用 server 端提供的 API 进行操作。如果有问题，欢迎邮件：[i@xlui.me](mailto:i@xlui.me) 或者 Issues。
