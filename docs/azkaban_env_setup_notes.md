## Environment Setup on CentOS Virtual Machine

Virtual Environment: 

- CentOS Linux 7 (Core)

- Linux 3.10.0-957.27.2.e17.x86_64



Reminder if this is a new VM, before anything else, to:

`sudo yum update`



Next, we need java, specifically Amazon Corretto 8 (I ran into issues using the openJDK version of the Java 8 jdk as it was missing some C library support that Azkaban uses). Since we are using CentOS, yum doesn't currently know where to find it (unlike dpkg if we were using a debian flavor of linux), so we will have to do a local install. First download the Linux .rpm file from [Amazon's download page](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html). Hint: if you are using an ssh shell to connect to your VM you can copy the .rpm file's URL to your clipboard and then use 

`curl -o ./java-1.8.0-amazon-corretto-devel-<VERSION#>.rpm 'paste-URL-path-to-rpm-file-here' `



Then, do a local install from this file:

`sudo yum localinstall java-1.8.0-amazon-corretto-devel-<VERSION#>.rpm`



Finally, check that your current version of java is using corretto 8:

`java -version`



You should see 'Corretto' in the OpenJDK Runtime Environment



### Installing and building Azkaban



install git if not already installed

`sudo yum install git`



Then, `cd` into the directory where you want to clone Azkaban and run:

`git clone https://github.com/azkaban/azkaban.git`



Once that is done, it is time to build azkaban for the first time! `cd` into azkaban's top level directory (An `ls` should show, among other things, a script called <span style="color:lime">`gradlew`</span>). Then, run the following command (this will take some time the first time, but will be faster on subsequent builds):

`sudo ./gradlew build installDist`



Some notes at this point. The gradlew script is written in such a way that it can be used to build individual pieces of azkaban or all pieces of azkaban depending on where in the directory structure it is run. Therefore, make sure you are in the correct directory when running this script or it will not behave correctly. Additionally, on subsequent builds, you can add `-x test` to the end to skip running the tests. This is useful because at least one of the tests is unreliable and can sometimes hang the entire build process indefinitely.


Now, lets run the vanilla solo server:

`cd azkaban-solo-server/build/install/azkaban-solo-server/`

`sudo bin/start-solo.sh`



Check the soloServerLog log file that is generated in the current directory to see if everything is running well. If it complains about files missing, make sure you are in the correct directory when you run the start-solo.sh script, or else it will break. To access the solo server which should now be running, open a browser and navigate to `localhost:8081`. If you are connecting remotely to your VM, you may need to configure the firewall to allow the server to be externally visible, preferably only with your local subnet.



To shut down the server, still within the `azkaban-solo-server/build/install/azkaban-solo-server/` directory, use:

`sudo bin/shutdown-solo.sh`



