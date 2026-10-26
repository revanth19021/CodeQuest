
FROM tomcat:10.1-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY CodeQuest.war /usr/local/tomcat/webapps/ROOT.war

# Disable Tomcat's shutdown listener.
RUN sed -i 's/port="8005"/port="-1"/' \
    /usr/local/tomcat/conf/server.xml

# Configure Tomcat's HTTP connector for Render.
RUN sed -i 's/port="8080"/port="10000"/' \
    /usr/local/tomcat/conf/server.xml

EXPOSE 10000

CMD ["catalina.sh", "run"]
