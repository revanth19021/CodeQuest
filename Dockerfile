
FROM tomcat:10.1-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY CodeQuest.war /usr/local/tomcat/webapps/ROOT.war

RUN sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml \
    && sed -i 's/port="8080"/port="10000"/' /usr/local/tomcat/conf/server.xml

EXPOSE 10000

CMD ["sh", "-c", "echo '=== WAR FILE ==='; ls -lh /usr/local/tomcat/webapps/ROOT.war; echo '=== WAR CONTENTS ==='; jar tf /usr/local/tomcat/webapps/ROOT.war | head -40; echo '=== STARTING TOMCAT ==='; catalina.sh run"]
