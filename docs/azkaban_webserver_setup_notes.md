### Steps used set up azkaban-web-server



Assumptions:

- Azkaban solo server runs correctly
- OS: CentOS 7



First, I installed MariaDB onto the box: [Use this widget to install MariaDB for your OS](https://downloads.mariadb.org/mariadb/repositories/#mirror=accretive). MariaDB was necessary for CentOS 7 as azkaban and MySQL don't work together on Red Hat based OS's. (certain java libraries break). MariaDB is a fork of MySQL that is supported by red hat based distros of linux, including CentOS 7.



Once MariaDB was installed, with the following root credentials being:

```
username: root@localhost
password:                   #no pw; press [enter]
```



from a shell as sudo, I ran:

`mysql_secure_installation`

and followed the prompts as necessary to finish configuring the DB server. Of note! make sure to delete the anonymous user and test database or azkaban will fail to authenticate!!



I modified /etc/my.cnf to contain the following:

```
[mysqld]
max_allowed_packet=1024M
```



and restarted the service:

`service mysqld restart`



Next, I created a database and user for azkaban:

```
# mysql
MariaDB [(none)]> CREATE DATABASE azkaban;
MariaDB [(none)]> CREATE USER 'azkaban'@'%' IDENTIFIED BY 'azkaban123';
MariaDB [(none)]> GRANT SELECT,INSERT,UPDATE,DELETE ON azkaban.* to 'azkaban'@'%' WITH GRANT OPTION;

```



Then, I grabbed the sql script to create all azkaban tables from `azkaban/azkaban-db/build/distributions/azkaban-db-<version>.tar.gz` with:

`tar xvzf azkaban-db-<version>.tar.gz`

`cd azkaban-db-<version>`

`mysql -u root -p` (enter password)

```
MariaDB [(none)]> USE azkaban; 
MariaDB [azkaban]> SOURCE create-all-sql-<version>.sql; 
```



Which generates around 29 tables:



```
MariaDB[azkaban]> SHOW TABLES;
```



Next, I modified the config file of the web server at `azkaban/azkaban-web-server/src/main/resources/conf/azkaban.properties`:

```
database.type=mysql
mysql.port=3306
mysql.host=localhost
mysql.database=azkaban
mysql.user=azkaban
mysql.password=azkaban123
mysql.numconnections=100
```



Finally, to get the web server to not throw a fit about not having an exec server to connect to, in `azkaban-web-server/src/main/java/azkaban/webapp/AzkabanWebServer.java`, I had to comment out line 231:

```java
webServer.executorManagerAdapter.start(); //RYAN SHARP - comment out this line
```





Then, i rebuilt the webserver by running the following command __from within `azkaban/azkaban-web-server/`__ as sudo:

`../gradlew build installDist -x test`



Finally, to run the webserver, as sudo:

```
# cd build/install/azkaban-web-server
# bin/start-web.sh
```

