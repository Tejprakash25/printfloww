Create a complete high-fidelity, professional and FULLY INTERACTIVE UI/UX prototype for a web-based digital printing management platform called:

PRINTFLOW
“Your Print Orders, Simplified”

IMPORTANT:
This is NOT a static UI design.
Build the entire project as a clickable, interactive Figma prototype.

EVERY IMPORTANT BUTTON, NAVIGATION ITEM, CARD ACTION, FORM ACTION, APPROVAL ACTION, STATUS ACTION, TAB, LINK, ICON ACTION, AND USER FLOW MUST HAVE A PROTOTYPE INTERACTION.

The prototype should feel like a real working web application even though it is only a Figma prototype.

Do not create only disconnected screens.
Create all screens and connect them logically with prototype interactions.

==================================================
1. PROJECT PURPOSE
==================================================

PrintFlow is a web-based Digital Printing Order, Approval and Workflow Management System.

It digitizes the complete printing business workflow:

Customer Enquiry
→ Printing Order
→ Design Upload
→ Quotation
→ Design Proof
→ Customer Approval / Change Request
→ Production
→ Quality Check
→ Ready for Delivery
→ Delivery / Pickup
→ Payment
→ Invoice
→ Order History

The system has four major user roles:

1. Customer
2. Admin / Owner
3. Production Staff
4. Delivery / Pickup Staff

Create a professional SaaS-style interface suitable for a real printing business.

==================================================
2. TECHNOLOGY / DESIGN CONTEXT
==================================================

This Figma design will later be implemented using:

Frontend:
HTML
CSS
JavaScript

Backend:
Java
Spring Boot
REST APIs
JPA / Hibernate

Database:
MySQL

Therefore the UI should be realistic and practical to implement using HTML, CSS and JavaScript.

Do NOT design it like a mobile-only application.

Primary design should be desktop web application with a responsive mobile version.

==================================================
3. DESIGN STYLE
==================================================

Use a modern professional SaaS dashboard style.

Visual style:

- Clean
- Minimal
- Professional
- Corporate
- Modern
- Spacious
- Easy to understand
- Real-world business application
- Not overly decorative
- Not overly colorful
- Strong visual hierarchy

Brand:

PrintFlow

Tagline:

“Your Print Orders, Simplified”

Primary colors:

Navy:
#0B2A4A

Primary Blue:
#1677FF

Background:
#F5F8FC

White:
#FFFFFF

Main Text:
#1E293B

Muted Text:
#64748B

Success:
#2CCB8A

Warning:
#FFB547

Danger:
#FF6B6B

Border:
#E2E8F0

Use Inter or a similar modern sans-serif font.

Typography:

Page heading:
24–28 px

Card heading:
16–18 px

Normal text:
14 px

Small labels:
12 px

Use an 8 px spacing system:
8, 16, 24, 32, 40, 48 px.

Use 12–16 px corner radius.

Use subtle shadows only where appropriate.

==================================================
4. SCREEN / FRAME SIZE
==================================================

Create desktop frames approximately:

1440 × 900 px

Create mobile frame:

390 × 844 px

Each screen must be an independent frame.

Do NOT place every screen inside one large frame.

Clearly name frames.

==================================================
5. CREATE THESE FIGMA PAGES
==================================================

Create these pages:

00 Design System
01 Customer
02 Production
03 Payment & Delivery
04 Admin
05 Mobile
06 Prototype

==================================================
6. DESIGN SYSTEM
==================================================

Create reusable components for:

- PrintFlow logo
- Sidebar
- Header
- Navigation item
- Active navigation item
- Primary button
- Secondary button
- Success button
- Danger button
- Input field
- Password field
- Dropdown
- Date picker field
- Textarea
- Search field
- Status badge
- Summary card
- Data table
- Table row
- Modal
- Confirmation dialog
- Toast notification
- Avatar
- Notification icon
- Upload area
- Progress indicator
- Timeline
- Pagination
- Tabs
- Empty state
- Loading state

Make repeated elements reusable components.

Use Auto Layout wherever appropriate.

Create component variants for:

Button:
Primary
Secondary
Success
Danger
Disabled

Status:
Pending
Approved
Rejected
Proof Pending
In Production
Quality Check
Ready
Out for Delivery
Delivered
Paid
Payment Pending

==================================================
7. CUSTOMER LOGIN
==================================================

Create:

Customer / Login

Design:

Left side:
PrintFlow logo
Tagline
Email / Phone field
Password field
Show/hide password icon
Login button
Register link
Forgot Password link

Right side:
Professional printing press / printing machine image
Text:

“From Design to Delivery”
“We Print Your Ideas”

Benefits:

Quick Orders
Quality Prints
On-Time Delivery

INTERACTIONS:

Login button
→ Customer Dashboard

Register
→ Registration screen/state

Forgot Password
→ Forgot Password screen/state

Show password icon
→ Toggle password visibility state

Email / Phone field
→ Focus/input state

Password field
→ Focus/input state

==================================================
8. CUSTOMER REGISTRATION
==================================================

Create:

Customer / Register

Fields:

Full Name
Email
Phone
Password
Confirm Password
Address

Buttons:

Create Account
Back to Login

INTERACTIONS:

Create Account
→ Customer Dashboard or Registration Success state

Back to Login
→ Login

==================================================
9. CUSTOMER DASHBOARD
==================================================

Create:

Customer / Dashboard

Sidebar:

Dashboard
New Order
My Orders
Designs
Payments
Profile
Logout

Top header:

Welcome, Priya!
Notification icon
Profile avatar

Summary cards:

Total Orders
In Production
Ready for Delivery
Completed

Recent Orders table:

Order ID
Product
Quantity
Status
Date
Action

Example:

PF1024
Flex Banner
2
In Production
18 Sep 2025

PF1023
Visiting Cards
500
Proof Pending
16 Sep 2025

PF1022
Wedding Invitation
20
Approved
14 Sep 2025

PF1021
Banner
1
Delivered
10 Sep 2025

INTERACTIONS:

Dashboard
→ Dashboard

New Order
→ Create New Order

My Orders
→ My Orders

Designs
→ Designs

Payments
→ Payments

Profile
→ Profile

Logout
→ Login

View All
→ My Orders

Click order row / View
→ Order Details / Order Tracking

Notification icon
→ Notifications panel

Profile avatar
→ Profile menu

==================================================
10. NEW PRINTING ORDER
==================================================

Create:

Customer / New Order

Title:

New Printing Order

Create a 3-step progress indicator:

1. Product Details
2. Design Upload
3. Review & Submit

Product Details:

Product Type
Quantity
Width
Height
Material
Finishing
Required Date
Special Instructions
Delivery / Pickup selection

Example product types:

Flex Banner
Visiting Cards
Wedding Invitation
Brochure
Poster
Flyer
Banner
Business Card

Buttons:

Next: Upload Design
Cancel

INTERACTIONS:

Product Type dropdown
→ Open dropdown options

Material dropdown
→ Open options

Finishing dropdown
→ Open options

Delivery/Pickup
→ Select state

Next: Upload Design
→ Design Upload screen

Cancel
→ Customer Dashboard

==================================================
11. DESIGN UPLOAD
==================================================

Create:

Customer / Design Upload

Show:

Order ID
Selected product
Quantity
Dimensions
Material

Large drag-and-drop upload area.

Supported formats:

PDF
JPG
PNG
DOCX

Buttons:

Upload Design
Remove
Next: Review

INTERACTIONS:

Upload area
→ Upload selected state

Remove
→ Empty upload state

Next: Review
→ Review & Submit

Back
→ Product Details

==================================================
12. REVIEW & SUBMIT
==================================================

Create:

Customer / Review Order

Show complete order summary:

Product
Quantity
Size
Material
Finishing
Required Date
Delivery Type
Uploaded Design
Special Instructions

Show estimated quotation section.

Buttons:

Submit Order
Edit Order
Back

INTERACTIONS:

Edit Order
→ New Order

Back
→ Design Upload

Submit Order
→ Order Submitted Success state

After submission:
→ Quotation / Order Details

==================================================
13. QUOTATION
==================================================

Create:

Customer / Quotation

Title:

Quotation for Order #PF1024

Status:
Approved / Pending depending on state

Pricing table:

Item
Quantity
Price

Example:

Flex Banner
2
₹800

Eyelet
20
₹100

Design Service
1
₹200

Delivery
1
₹100

Total:
₹1,200

Buttons:

Accept Quotation
Reject Quotation

INTERACTIONS:

Accept Quotation
→ Quotation Accepted confirmation
→ Continue to Design Proof

Reject Quotation
→ Reject confirmation modal

Confirm Reject
→ Quotation Rejected state

Cancel
→ Quotation screen

==================================================
14. DESIGN PROOF & APPROVAL
==================================================

Create:

Customer / Design Proof

Title:

Design Proof — Order #PF1024

Show:

Large design preview

Design Proof v2
Uploaded date
Filename

Buttons:

Approve
Request Changes

Change Request textarea

Submit button

INTERACTIONS:

Approve
→ Approval Confirmation modal

Confirm Approval
→ Order Tracking with Approved status

Request Changes
→ Show change request section

Submit
→ Change Request Submitted state

Back
→ Quotation

==================================================
15. ORDER TRACKING
==================================================

Create:

Customer / Order Tracking

Title:

Track Order — PF1024

Horizontal progress:

Order Placed
↓
Design Upload
↓
Approved
↓
Printing
↓
Delivery

Completed stages:
green checkmark

Current stage:
blue

Future stage:
gray outline

Order Details:

Product
Quantity
Required Date
Status

Vertical timeline:

Order Placed
Design Uploaded
Approved
In Production
Quality Check
Ready for Delivery
Delivered

INTERACTIONS:

Click timeline stage
→ Show stage details

View design
→ Design Proof

Payment
→ Payment Details

Delivery
→ Delivery Details

Back
→ My Orders

==================================================
16. MY ORDERS
==================================================

Create:

Customer / My Orders

Filters:

All
Pending
In Production
Ready
Completed

Search by:

Order ID
Product

Table:

Order ID
Product
Quantity
Date
Status
Action

INTERACTIONS:

Filter buttons
→ Change selected filter state

Search
→ Filter matching orders

View
→ Order Tracking

==================================================
17. DESIGNS
==================================================

Create:

Customer / Designs

Show uploaded design cards.

Each card:

Design preview
Design name
Order ID
Version
Date
Status

Actions:

View
Download
View Versions

INTERACTIONS:

View
→ Design Proof

View Versions
→ Version History modal

Download
→ Download confirmation state

==================================================
18. PAYMENTS
==================================================

Create:

Customer / Payments

Payment summary.

Table:

Order ID
Amount
Payment Method
Status
Date
Invoice

Methods:

UPI
Card
Bank Transfer

Statuses:

Paid
Pending
Failed

Buttons:

Pay Now
View Invoice

INTERACTIONS:

Pay Now
→ Payment Method selection

Select UPI/Card/Bank
→ Payment confirmation

Confirm Payment
→ Payment Successful

View Invoice
→ Invoice screen/modal

==================================================
19. PROFILE
==================================================

Create:

Customer / Profile

Show:

Profile photo
Full Name
Email
Phone
Address

Buttons:

Edit Profile
Change Password
Save Changes
Logout

INTERACTIONS:

Edit Profile
→ Editable state

Save Changes
→ Profile Updated confirmation

Change Password
→ Password form

Logout
→ Login

==================================================
20. PRODUCTION STAFF DASHBOARD
==================================================

Create:

Production / Dashboard

Sidebar:

Dashboard
Assigned Jobs
Job Status
Reports
Profile
Logout

Title:

Production Dashboard

Today's Jobs table:

Job ID
Product
Customer
Quantity
Material
Size
Status
Action

Statuses:

Approved
Printing
Quality Check
Ready

Action:

Update

INTERACTIONS:

Assigned Jobs
→ Assigned Jobs

Job Status
→ Job Status

Update
→ Job Status update modal

Change Printing → Quality Check
→ Updated state

Change Quality Check → Ready
→ Updated state

Reports
→ Production Reports

Profile
→ Staff Profile

Logout
→ Login

==================================================
21. PRODUCTION JOB DETAILS
==================================================

Create:

Production / Job Details

Show:

Job ID
Order ID
Customer
Product
Quantity
Material
Size
Required Date
Design Preview

Status progress:

Approved
Printing
Quality Check
Ready

Buttons:

Start Printing
Mark Quality Check
Mark Ready

INTERACTIONS:

Start Printing
→ Printing state

Mark Quality Check
→ Quality Check state

Mark Ready
→ Ready state

Back
→ Production Dashboard

==================================================
22. PAYMENT STAFF / PAYMENT DETAILS
==================================================

Create:

Payment / Dashboard

Sidebar:

Dashboard
Payments
Invoices
Reports
Profile

Table:

Order ID
Amount
Payment Method
Status
Date
Invoice

Actions:

View Invoice
Update Payment

INTERACTIONS:

View Invoice
→ Invoice screen

Update Payment
→ Payment update modal

Save
→ Payment Updated state

==================================================
23. DELIVERY / PICKUP DASHBOARD
==================================================

Create:

Delivery / Dashboard

Summary cards:

Ready for Delivery
In Transit
Delivered

Table:

Order ID
Customer
Address
Type
Payment
Status
Action

Type:

Pickup
Delivery

Status:

Ready
Out for Delivery
Delivered

INTERACTIONS:

View
→ Delivery Details

Mark Out for Delivery
→ Out for Delivery state

Mark Delivered
→ Delivered state

Pickup
→ Pickup confirmation

Back
→ Delivery Dashboard

==================================================
24. DELIVERY DETAILS
==================================================

Create:

Delivery / Details

Show:

Order ID
Customer
Address
Phone
Order
Payment
Delivery Type
Current Status

Buttons:

Start Delivery
Mark Delivered
Call Customer
Back

INTERACTIONS:

Start Delivery
→ Out for Delivery

Mark Delivered
→ Delivered

Back
→ Delivery Dashboard

==================================================
25. ADMIN DASHBOARD
==================================================

Create:

Admin / Dashboard

Sidebar:

Dashboard
Manage Customers
All Orders
Quotations
Employees
Reports
Settings
Logout

Summary cards:

Total Orders
Pending
In Production
Completed

Recent Orders:

Order ID
Customer
Product
Status
Date
Action

INTERACTIONS:

Dashboard
→ Admin Dashboard

Manage Customers
→ Customer Management

All Orders
→ All Orders

Quotations
→ Quotation Management

Employees
→ Employee Management

Reports
→ Reports

Settings
→ Settings

Logout
→ Login

View All
→ All Orders

==================================================
26. ADMIN CUSTOMER MANAGEMENT
==================================================

Create:

Admin / Customers

Search bar.

Filters.

Customer table:

Customer ID
Name
Email
Phone
Total Orders
Status
Action

Actions:

View
Edit
Deactivate

INTERACTIONS:

View
→ Customer Details

Edit
→ Edit Customer

Deactivate
→ Confirmation modal

Confirm
→ Customer Deactivated state

==================================================
27. ADMIN ALL ORDERS
==================================================

Create:

Admin / All Orders

Filters:

All
Pending
Proof Pending
Approved
Printing
Quality Check
Ready
Delivered

Search:

Order ID
Customer
Product

Table:

Order ID
Customer
Product
Quantity
Date
Status
Action

INTERACTIONS:

Filters
→ Filtered state

Search
→ Search state

View
→ Admin Order Details

==================================================
28. ADMIN QUOTATION MANAGEMENT
==================================================

Create:

Admin / Quotations

Table:

Quotation ID
Order ID
Customer
Amount
Status
Date
Action

Actions:

Create Quotation
Edit
View
Send

INTERACTIONS:

Create Quotation
→ Quotation Form

Save
→ Quotation Created

Send
→ Quotation Sent confirmation

View
→ Quotation Details

==================================================
29. ADMIN EMPLOYEE MANAGEMENT
==================================================

Create:

Admin / Employees

Employee table:

Employee ID
Name
Role
Phone
Status
Action

Roles:

Admin
Production Staff
Delivery Staff

Actions:

Add Employee
Edit
Deactivate

INTERACTIONS:

Add Employee
→ Add Employee form

Save
→ Employee Added

Edit
→ Edit Employee

Deactivate
→ Confirmation modal

==================================================
30. ADMIN REPORTS
==================================================

Create:

Admin / Reports

Show:

Total Orders
Revenue
Pending Payments
Completed Orders
Production Jobs

Charts:

Orders by Status
Monthly Orders
Revenue Summary

Date filter:

Today
This Week
This Month
Custom

INTERACTIONS:

Date filter
→ Change report state

View Details
→ Detailed report

==================================================
31. SETTINGS
==================================================

Create:

Admin / Settings

Sections:

Business Information
Printing Prices
Notification Settings
Account Settings

Buttons:

Save Changes
Reset

INTERACTIONS:

Save Changes
→ Settings Saved confirmation

Reset
→ Reset confirmation

==================================================
32. NOTIFICATIONS
==================================================

Create notification dropdown/panel.

Examples:

Quotation received
Design proof ready
Order approved
Order entered production
Order ready for delivery
Payment received

Each notification should be clickable.

INTERACTIONS:

Click notification
→ Navigate to relevant page.

Mark as Read
→ Read state.

Mark All Read
→ All notifications read.

==================================================
33. MODALS AND CONFIRMATIONS
==================================================

Create reusable modal components.

Use confirmation modals for:

Reject quotation
Approve proof
Request changes
Delete
Deactivate customer
Deactivate employee
Logout
Mark delivered
Payment confirmation

Each modal must have:

Title
Message
Cancel
Confirm

INTERACTIONS:

Cancel
→ Close modal

Confirm
→ Perform relevant next state

==================================================
34. TOAST NOTIFICATIONS
==================================================

Create reusable toast notifications.

Examples:

“Order created successfully.”
“Quotation accepted.”
“Design approved.”
“Change request submitted.”
“Payment successful.”
“Order status updated.”
“Customer updated.”
“Employee added.”

Use appropriate success/warning/error styling.

==================================================
35. MOBILE RESPONSIVE DESIGN
==================================================

Create:

Mobile / Customer Dashboard

Frame:
390 × 844

Do NOT simply shrink the desktop screen.

Create a real mobile layout.

Mobile header:

PrintFlow logo
Notification
Menu

Customer greeting.

Summary cards:

2 × 2 grid.

Recent Orders:

Compact cards/list instead of wide desktop table.

Bottom navigation:

Home
Orders
Designs
Profile

Make mobile buttons large enough to tap.

Create mobile versions of important customer screens:

Login
Dashboard
New Order
Order Details
Design Proof
Order Tracking
Payments
Profile

Connect them interactively.

==================================================
36. COMPLETE PROTOTYPE CONNECTIONS
==================================================

THIS IS EXTREMELY IMPORTANT.

Do NOT leave screens disconnected.

Create prototype interactions between all relevant screens.

Main Customer Flow:

Login
→ Dashboard
→ New Order
→ Product Details
→ Design Upload
→ Review
→ Order Submitted
→ Quotation
→ Accept Quotation
→ Design Proof
→ Approve
→ Order Tracking
→ Payment
→ Payment Success
→ Delivery
→ Completed
