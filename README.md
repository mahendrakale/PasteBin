# PasteBin
PasteBin

Requirements :
==============
write a command line utility that can shorten a URL and unshorten a short URL
Commands

● urlshortener shorten <URL>
○ This command will create a shortened version of given url and display it on
screen

● urlshortener unshorten <shortened URL>
○ This command should display original url of given shortened url

Sample input and output
$ urlshoretner shorten http://clypd.com/careers
Output:
http://u.rl/abc
$ urlshoretner unshorten http://u.rl/abc
Output:
http://clypd.com/careers

Setup :
========
1. JDK 1.8 compiler & runtime
2. Redis server 2.8.13 or later installed & running on 6379 port
3. maven 4.x to build & run
4. Spring boot needs port 8080
5. Curl command

Build :
========
1. go to UrlServer folder
2. build : mvn clean install

Run :
======
1. mvn spring-boot:run


Frameworks used :
=================
1. Spring boot
2. RestAssured
3. Spring Redis
4. Guava

TODO :
========
1. Introduce distributed cache to return most recent url ids.
2. Performance tests to benchmark load
3. Optimized hash function based on performance statistics.
4. More negative unit test case coverage.
