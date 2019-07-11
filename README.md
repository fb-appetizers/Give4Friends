# Give4Friends
A social network-like platform that allows friends to donate to charity in eachother's names. On each person's profile it will show the donations raised for them (and maybe their past activity for friends).  On the profile people can list and describe their favorite charities/cause areas.

## User Stories (Required and Optional)

Required Must-have Stories
* See history (feed of exchanges)
* User has profile
* User can login
* User can create account
* User can pick a charity/friend/amount to donate


## Optional Nice-to-have Stories

### Stretch
* Suggested Charities
  - Effetive charities
  - Charities mutual with friends
* Privacy Settings
* Liked Charities (on profile) 
* Friend requests
* Comments (and report user) - on charity and feed events
* Anonymous donations
* Link to effective charities
* After signing up - add favorite charities 
* Privatley see total raised in your name


### Super Stretch
* Having charities be able to post
* Milestones



## Screen Archetypes

* Log in
  - Sign in 
  - Sign up
  
* Sign up
  - Name
  - Email
  - Username
  - Password
  - Photo
  - Credit Card

* Preferences*
  * Skip
  * List of charities you can pick from
  
* Settings (not public)
  - Credit card info 
  - Privacy Settings*
  - Change Password*
  - Notifications*
  - Edit Profile
  
* Profile
  - Description of favorite cause areas/charities (optional)
  - Total raised (private)*
  - User info
  - User pic
  - Favorite charities*
  - Edit Profile
    
* Home Page
    - Get to see most recent events/ updates from friends
    - Nav Bar
    - Likes on posts*
    - Comments on posts*
    - Donate Now button  
  
* Charity search page
  - Suggested charities*
  - Dropdown search bar for charities

* Charity Info Page *
  - Charity name
  - Info
  - Comments *
    
* Donate page
  - friend's info
  - amount
  - charity
  - Message to friend

* Liked charities* (Not Public)
  - List of liked charities
  - time liked
  
* Transactions*
  - different sides for if they donated or were donated on behalf of *
  - Different colors ^^ *

…
# Navigation
Tab Navigation (Tab to Screen)

Navigation Bar-
* Profile
* Settings
* Log out
* Liked Charities 
* Charity search Page
* Recent transactions*

# Flow Navigation (Screen to Screen)

* Login
=> Home
=> Sign up

* Sign up
=> Home

* Home
=> Charity Search Page

* Charity Search Page
=> Charity info
=> Donate 

* Donate
=> Home


…


# Schema            


### User
| Property         | Type             |  Description                                  | 
|------------------|------------------|-----------------------------------------------|
| objectID         |String            | Unique Id for user                            |  
| userName         |String            | Unique Username                               | 
| firstName        |String            | First Name                                    |
| lastName         |String            | Last name                                     |
| email            |String            | User email                                    |
| friendID         |Pointer to User   | Pointer to person who was donated on behalf of|
| charityID        |Pointer to Charity| Pointer to charity                            |


### Financial Info (credentials)
| Property         | Type             |  Description                                  | 
|------------------|------------------|-----------------------------------------------|
| objectID         |String            | Unique Id for user                            |  
| credit card num  |Number            | Credit Card Number                            | 
| expiration date  |Date              | Date of Expiration for the Credit Card        |
| cvc              |Number            | Credit card code                              |
| email            |String            | User email                                    |
| userId           |Pointer to User   | Pointer to the user of the credit card info   |



### Transaction History
| Property         | Type             |  Description                                  | 
|------------------|------------------|-----------------------------------------------|
| objectID         |String            | Unique Id for user transaction                |  
| donorID          |Pointer to User   | Pointer to user who donated                   |   
| friendID         |Pointer to User   | Pointer to person who was donated on behalf of|
| charityID        |Pointer to Charity| Pointer to charity                            |
| amountDonated    |Number            | Amount that was donated                       |
| likesCount       |Number            | Total number of likes on the transaction      |
| likesUsers       |Array             | Array of users who liked the transaction      |


