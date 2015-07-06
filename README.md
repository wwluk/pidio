[![Build Status](https://travis-ci.org/wwluk/pidio.svg?branch=master)](https://travis-ci.org/wwluk/pidio)
[![Codacy Badge](https://www.codacy.com/project/badge/bd91bce1c2784d3db57f55f67babef40)](https://www.codacy.com/app/wwluk/pidio)

# PiDio - REST interface for mpd

This project aims to provide REST interface for the Linux Music Player Daemon.
It is created mainly for working on the raspberry pi as an internet radio player.

This interface can cooperate with the [pidio_ui](https://github.com/wwluk/pidio) project, which is the simple management web application.

## Requirements
* java
* sbt
* mpd
* mpc

## Installation

Simply run:
```
sbt assembly
```

Resulting jar can be run in the following way:
```
java -jar pidio-assembly-0.1.jar
```



