Note: Cloud Database is fairly slow so some screens/actions may take time render/process.

### To run the application run SchedulerProApplication.java

#### Table of Contents
	Section 1 - Create an Account
    Section 2 - Login
    Section 3 - Calendar
    Section 4 - Manage Customers
    Section 5 - Manage Appointments
    Section 6 - Reports
    Section 7 - Misc

##### Section 1 - Create an Account
Upon loading the application you will be given the option to Create an Account.  If you are a new user click the `Create an Account` button.

Upon arrival at the `Create an Account` screen you will be prompted to enter a username and password.  To create an account enter a username
and password combination and press the `Create an Account` button.  If successful, you will receive a message indicating so.  If there are
errors you will receive messages indicating what errors were encountered.

After creating an account click the 'X' button to return to the Login screen.

Notes:
	The form supports the alternative language of German.
		Locale.setDefault(new Locale("de", "DE"));
	You cannot use the username of an existing user.
	Your username must be >= 1 character.

##### Section 2 - Login

A user is able to access the application with a valid username and password combination.  To login enter your username and password
combination and click `Login`.

Notes:
	The form supports the alternative language of German.
		Locale.setDefault(new Locale("de", "DE"));
	You must enter a valid username and password combination.
	User login and logout activity is tracked in the log.txt file that exists in the root directory of the project.

##### Section 3 - Calendar

Upon logging into the application you will be taken to the `Calendar` screen `By Month` view that will be populated with any existing appointments that exist
for your logged in user account. If you wish to change your calendar view you may do so by clicking `View` on the navigation bar at the top of the `Calendar` 
screen and choosing `View -> By Week`. 

Notes:
	If no appointment data exists for a day on the calendar you will see a message indicating that no content could be found.
	You can use the navigation arrows at the top of the screen to change the Month/Year to view a particular point in time.
	You can right-click on a particular day on the calendar to utilize actions for adding/updating/deleting appointments.
	Appointment times are automatically adjusted based on the user's timezone.
	If a user has an appointment that has started within the previous 15 minutes, or starts in the next 15 minutes, the user will receive an alert.

##### Section 4 - Manage Customers

	Add
		To add a new customer fill out the form provided and click `Add`.
		To reset the form click `Reset`.
	Update
		To update an existing customer click the customer record in the table provided to load the customer's data.
		The form will populate with the current data.  You may make your changes and then click `Update` to update the customer record.

	Notes:
		You cannot deactive a customer who has appointments scheduled.
		Instead of deleting customers the ability exists to deactivate the customer.  This allows the customer to be reactivated
		in the event the customer wishes to do business again.

##### Section 5 - Manage Appointments
	
	Add
		To add a new appointment fill out the form provided and click `Add`.
		To reset the form click `Reset`.
	Update
		To update an existing appointment you must locate the appointment on the calendar, right-click, and click `Update Appointment`
		to load the data into the form.  You may make your changes and then click `Update` to update the appointment record.

	Notes:
		You cannot schedule an appointment outside of business hours.  Business hours are 08:00 AM - 05:00 PM in the local time zone.
		You cannot schedule an appointment with the same start and end time.
		You cannot schedule an appointment that overlaps with another appointment.

##### Section 6 - Reports
	
	To view a report choose the desired option from the dropdown box and then click `View Report`.  The report will populate to the table
	with the data from the database.

	The report options include:
		Appointment Types by Month
			A count of each appointment type for each month the appointment type exists.
		Consultant Schedule
			A full appointment schedule for all consultants.
		Appointent Types by Consultant, Year, Month
			A count of each appointment type for each consultant, for each year, and for each month.

##### Section 7 - Misc 

	Test User Credentials:
		Username: test
		Password: test

	Lambda Expressions
		SchedulerProMainController.java 
			Line 426: Used to efficiently build out the calendar view by month.
			Line 496: Used to efficiently add additional styles to the calendar day
		DataManager.java
			Line 83: Used to efficiently filter down appointments to only appointments for the logged in user.
			Line 97: Used to extract out the distinct appointment types available.

	Exception Control
		SchedulerProApplication.java
			Line 108: throws is used to throw a SQLException should one occur
		DataManager.java
			Line 1055: Try/Catch block that will catch any exception with generating the consultants report
