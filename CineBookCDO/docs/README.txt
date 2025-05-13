CineBook CDO - Cinema Booking System
====================================

A Java Swing application for booking movie tickets in Cagayan de Oro cinemas.

COMPILING AND RUNNING THE APPLICATION
-------------------------------------

1. Compiling the application:
   - Open a terminal/command prompt in the project root directory
   - Run the following command:
     javac -d bin src/**/*.java

2. Running the application:
   - After compiling, run the application with:
     java -cp bin src.Main

SAMPLE LOGIN CREDENTIALS
------------------------

Admin User:
- Username: admin
- Password: admin123

Regular Users:
- Username: john_doe
- Password: password123

- Username: jane_smith
- Password: password456

You can also register a new user or continue as a guest.

APPLICATION FEATURES
-------------------

User Features:
- Create account or checkout as guest
- View current movies in 3 cinemas
- Filter movies by cinema, genre, or search term
- Select movie and schedule
- Choose seats (up to 6 per booking)
- Add snacks and drinks
- Simulated payment processing
- E-ticket display

Admin Features:
- Manage movies (add, update, delete)
- Manage movie schedules
- View booking logs
- Generate sales reports

SIMULATION NOTES
---------------

1. Payment Processing:
   The application simulates payment processing with three methods:
   - Credit Card
   - GCash
   - PayMaya
   
   No actual payment gateway is connected; the payment is always successful.

2. Email Confirmation:
   The application simulates sending email confirmations by printing to the console.
   In a real application, this would be connected to an email service.

3. Report Export:
   The admin panel allows "exporting" reports to text files, but this is
   simulated by printing to the console in this demonstration version.

PROJECT STRUCTURE
----------------

The application follows the Model-View-Controller (MVC) pattern:

- Model: Classes representing data entities (Movie, User, Booking, etc.)
- View: UI components for user interaction
- Controller: Logic for handling user actions and updating the model/view

This separation makes the code more maintainable and extensible.

ACKNOWLEDGMENTS
--------------

This application was created as an educational project to demonstrate Java Swing
capabilities for desktop application development.

(c) 2023 CineBook CDO
