# BookingDetailsDAOdynamoJava
Creating/Running Java Application connected to Dynamo DB and perform insert operation through AWS lambda function 

In Intellij :
Create a Java Project with Maven;
After completing the code, execute this command:
	mvn clean package
Go inside the target folder:
BookingDAO-1.0-SNAPSHOT-shaded.jar ( use the shaded.jar) for putting it into lambda function

For AWS:
1.create Lambda function
2. Select Runtime Jva 8 on Amazon Linux 2
3.Click on create function
4. Go to your Lambda function
5. Click on Code
6. Click on upload from (select the jar)
7. Save

We need to change the handler inside Lambda Function.
Go to Runtime settings Section in same section of Code , click on edit;
 update:
Eg: org.booking.handler.SaveBookingDetailsHandler::handleRequest
<classname from package>::handleRequest

