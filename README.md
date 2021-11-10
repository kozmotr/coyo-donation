donation-challange repo is forked from coyoapp/donation-challenge
I've added some features to it and fixed a few things. Other than that
the other services are implemented from strach.
This repo is demonstration of micro-service arthitecture 
and spring boot .

Okey, lets jump in what i have done in 2 days for this forked repo.:

The donation monolithic structure is refactored
and it re arthitectured as micro-service.
We have service-registry service for service
discovery ability.(Eureka)
We have api-gateway that in front of request
also capable of load balancing. Since we don't
have clustured micro service yet, it does not mean
anything. But as todo , k8s configs will be written
then it will gain mean.
We have config server which is also very important
for microservice arthitecture.

I havent added hystrix or resillence4j as
circuit breaker for simplicity but it is
just a maven depandcy and 4 lines of configuration.

I creaate a mail-service that is capable of
sending mail regarding to their importance.

This mail service expose one get api that
accepts an byte array, and convert this byte
array to MimeMessage and send it. So any other
service can use by just creating their MimeMessage
and send this mail service to be taken care.

I simulated a priority based job scheduler in mail
service to just and simulate a priority based
job scheduler pattern.
In normal case all mails should be persisted
and retrieved from there to process.
But for simplicity I didnt do this.

Regards,
Ozan Ozenoglu
