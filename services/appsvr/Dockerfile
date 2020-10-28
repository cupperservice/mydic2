FROM centos:7

RUN yum update -y && \
yum install -y curl && \
yum install -y unzip && \
yum install -y zip && \
yum install -y which && \
curl -s "https://get.sdkman.io" | bash && \
source "/root/.sdkman/bin/sdkman-init.sh" && \
sdk install java 11.0.9.j9-adpt && \
sdk install sbt 1.3.12

CMD tail -f > /dev/null
