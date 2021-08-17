java -javaagent:byteman.jar=script:trace-null-tccl.btm,boot:byteman.jar -Dorg.jboss.byteman.transform.all -jar target/quarkus-app/quarkus-run.jar
