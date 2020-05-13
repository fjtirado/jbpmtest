# jbpmtest
A repo to store different projects developed while learning JBPM

* test project contains a plain JVM running embedded JBPM engine. 
It is mainly intended as an API showcase and automatically completes  all human tasks included in the sample BPMN2 file. The main difference with projects generated from eclipse plugin is that is maven based and does not include any referece to jbpm test artifact. 
* bussiness-application-service is a spring-boot application running embedded JBPM engine containing a simple booking procdure. Its main purpose
is to simulate a real customer use case (with a custom UI to resolve human tasks) 
