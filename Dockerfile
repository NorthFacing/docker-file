FROM ubuntu:16.04
MAINTAINER Bob "0haizhu0@gmail.com"
ENV REFRESHED_AT 2017-09-12

ENV TZ Asia/Shanghai
RUN echo $TZ > /etc/timezone && \
    apt-get update && apt-get install -y tzdata && \
    rm /etc/localtime && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    apt-get clean

RUN apt-get -y update && apt-get -y install git && apt-get clean
RUN apt-get -y update && apt-get -y install python && apt-get clean

RUN mkdir -p /root/joyoung

RUN cd /root/joyoung/ && \
    git clone https://github.com/NorthFacing/docker-file.git sdk && \
    cd sdk && \
    git checkout jdk160 && \
    cp -rf jdk1.6.0_38 ../jdk1.6.0_38 && \
    git checkout playframework && \
    cp -rf playframework ../playframework && \
    rm -rf sdk/

ADD jdk1.6.0_38/ /root/joyoung/jdk1.6.0_38/
ADD precompile_jy_ras_cms.sh /root/joyoung/precompile_jy_ras_cms.sh

ENV PLAY_HOME /root/joyoung/playFramework
ENV PATH $PATH:$PLAY_HOME/play

ENV JAVA_HOME /root/joyoung/jdk1.6.0_38
ENV CLASSPATH .:$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$CLASSPATH
ENV PATH $JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH

EXPOSE 80
