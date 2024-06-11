# Security Protocol: Logic For Security (DTU)

## Authentication

1. User request login on customer app.
2. customer app connects with MitID, the User is authenticated via MitID (external provider).
3. A token is generated and it's saved in customer app session for the User.
4. Customer App reaches Venue Server to create a new User in the database.
5. The User is saved in the database with his CPR as unique id.


A: Customer App
B: MitID
C: Venue Server
s: authentication 

## Ticket Purchase

Payment service?? If we need to have multiple payment options. For now let's keep it simple.

Knowledge: User session token from MitID (User is authenticated)

1. The user requests ticket information to the customer app
2. the customer app fetches ticket information from the Venue server
3. the Venue server returns (seats available, pice, etc...)
4. the User select his ticket and proceed with Mobile pay as payment method (checkout).
5. The customer app tells the venue server that the User is booking a ticket (verify that the User exists and can purchase). The ticket is booked with status PENDING and it's saved on the database (to reserve the seat during the payment) and it's assigned to the User. The VenueId is enabled for the User (saved in the database).
6. The customer app redirects the user to Mobile Pay and provides information about item to buy, price, user (CPR, name, etc...).
7. When the payment is successfully completed, the customer app reaches the venue server to book the ticket. The ticket status is changed to PAID. and the ticket paymentTimestamp is saved to the database.
8. a digital ticket is generated.
9. the venue app send the ticket to the customer app.
10. the user receives the ticket successfully from the customer app, with all the booking information. The ticket status is changed to BOOKED.

## Ticket Validation

Knowledge: User session token from MitID (User is authenticated)

Validation Device with VenueId

1. The User "show" his ticket from the customer app to the Validation device (QR code = h{ticketId, CPR}_venueId)
2. the Validation device decrypts the ticketId and the CPR number, fetches the ticket status from the venue server.
3. the Venue serve returns the valid ticket and status is changed to CONSUMED. And ticket validationDatetime is updated with current time in the database.
