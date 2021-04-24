## Azkaban Working Notes

Azkaban is a distributed Workflow Manager, implemented at LinkedIn to solve the problem of Hadoop job dependencies. Here are links to their [Documentation](https://azkaban.readthedocs.io/en/latest/) and [Github](https://github.com/azkaban/azkaban). 

There are two additional Markdown files that talk about the process of getting Azkaban running on a CentOS VM called azkaban_env_setup.md and azkaban_webserver_setup_notes.md. These will definitely be useful if/when going to containerize Azkaban. Of note in these, some of the requirements to run azkaban were not immediately presented in the Azkaban documentation.



#### Some notes about the structure of Azkaban



Here is the top level of Azkaban's source code:

```
az-core/                                     Top level utility functions and classes
az-crypto/                                   
az-examples/
az-exec-util/                               
az-flow-trigger-dependency-plugin/
az-flow-trigger-dependency-type/
az-hadoop-jobtype-plugin/
az-hdfs-viewer/
az-intellij-style.xml
az-jobsummary/
azkaban-common/                             Contains a lot of the core azkaban sourcecode
azkaban-db/                                 
azkaban-exec-server/                        Main dir for the executor server
azkaban-hadoop-security-plugin/
azkaban-solo-server/                        Config for bundling an exec and webserver
azkaban-spi/
azkaban-web-server/                         Main dir for the web server
az-reportal/
bin/
build.gradle
CONTRIBUTING.md
docs/
gradle/
gradle.properties
gradlew*                                    Main script for building Azkaban pieces.
gradlew.bat
LICENSE
NOTICE
README.md
requirements.txt
settings.gradle
test/
tools/
```



Some notes:

- Azkaban-common has a ton of code in it. Both the webserver and exec server reference it a lot. If something is being imported from `azkaban.<thing>`, this is where it lives. 

- azkaban-exec-server and azkaban-web-server are the main directories for the exec server and the web server (obviously), and each can be run / built independently of one another. Instructions for how to get the webserver to run without the exec server are in azkaban_webserver_setup_notes.md. 

- The `gradlew` build script will build whatever it is run from within. So for example, a `./gradlew build` will build everything, but to just build the web server, for example, the command would need to be: `cd azkaban-web-server; ../gradlew build`. Also, the args that I always used for gradlew was `build installDist -x test` (the `-x test` simply disabled running all the unit tests, at least one of which was bugged and would crash the build without failing some of the time).

- There are some additional plugin-related directories here that I did not look too closely into.

- I was unable to get azkaban to run on windows. Its windows build script kept having issues and I was unable to get it to work properly. Additionally, even though there is a gradlew.bat file, Azkaban technically does not claim to support windows. 
- Documentation wise, there are a lot more code comments in azkaban-common and azkaban-exec-server than there are in the azkaban-web-server code. And there are many TODOs in the code, sometimes conflicting with each other!



Azkaban's build scripts, as well as its server run scripts are quite brittle, and may need to be looked into in the future. One qualitative example of this is that running the start script for the web server when it is already running will not only fail, but cause Azkaban to lose track of the process ID of the one that is currently running, causing its shutdown script to break. If this happens, one will have to manually hunt down the process and kill it. Another example is that both gradlew and all the start and shutdown scripts require you to be in a very specific working directory in your shell when they are called, or they will break.



### Azkaban Exec Server Working Notes

Azkaban is designed to run in single or multi executor mode, with an executor manager distributing work to one or more exec servers. To do this, the web server interacts with an interface called `ExecutorManagerAdapter` (which is located in `azkaban-common/src/main/java/azkaban/executor`). The executor manager is what handles workload balancing across executor servers.



The code that maps the Azkaban configuration DAG to actual flow execution code is contained in `/azkaban-exec-server/src/main/java/azkaban/dag`.



I did not dig too deeply into the implementation of an individual executor as its primary purpose is to run hadoop workflows. However, I did dig a bit into the data contract between the web and exec servers:

#### Internal Data Contract notes

The azkaban executor server's API request handling is handled in: 

`/azkaban-exec-server/src/main/java/azkaban/execapp/ExecutorServlet.java`

and references the following file for the strings associated with each API call: 

`/azkaban-common/src/main/java/azkaban/executor/ConnectorParams.java` 



the following API calls are supported by the exec server:

```
UPDATE_ACTION
STATUS_ACTION
EXECUTE_ACTION
CANCEL_ACTION
PAUSE_ACTION
RESUME_ACTION
PING_ACTION
LOG_ACTION
ATTACHMENTS_ACTION
METADATA_ACTION
RELOAD_JOBTYPE_PLUGINS_ACTION
ACTIVATE
DEACTIVATE
GET_STATUS
SHUTDOWN

```

In general, anything that involves directing the exec servers or checking/modifying their status is handled through this API, whereas *anything that involved reading output from completed executions is handled directly between the web server and the database*. This means that the output from the exec server does not get directly fed to the web server through an API, but rather that  it populates a database from which the web server reads. 

### Azkaban Web Server Working Notes 

The front end web server uses a Java back end and a JavaScript/Velocity front end. Other technologies that the front end uses are bootstrap, [backbone.js](https://backbonejs.org/), and jquery. For the job scheduler, Azkaban uses the [Quartz job scheduler](http://www.quartz-scheduler.org/) package. 

Currently, because of the data contract between the exec server and the web server, there are *not any reactive, real time updating front end web components*. All visualizations are static, with executor status being grabbed via API and executor results being pulled directly from the database. This means that if we want to have any of the functionality that the interCEPT dashboard currently has, it will need to be implemented separately. (Vue might be a good framework option here because it is designed from the get-go to be incrementally inserted into already developed, traditional web applications).

Another potential issue with the web server is the lack of commenting / documentation. The front end JavaScript code, in particular, is very sparsely commented. Additionally, the implementation of the web server's front-facing API (which actually is documented) is called from a variety of places in the front end JavaScript of the web server. It is, in my opinion, spaghetti code. Working on the front end JavaScript will likely be a pain point.

One nice thing that the web server does buy us, in my opinion, is user authentication / OAuth. Due to time constraints, I have not looked rigorously at their implementation to check for security flaws. Hopefully the fact that Azkaban is open source means that in the future we can pull any solutions or upgrades to this portion of the code as Azkaban is updated.



##### Potential drag and drop UI Frameworks that could be useful:

- [interact.js](https://interactjs.io/) 