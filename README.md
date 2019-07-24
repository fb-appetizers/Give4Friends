# Give4Friends
A social network-like platform that allows friends to donate to charityAPI in eachother's names. On each person's profile it will show the donations raised for them (and maybe their past activity for friends).  On the profile people can list and describe their favorite charities/cause areas.

## User Stories (Required and Optional)

Required Must-have Stories
* See history (feed of exchanges)
* User has profile
* User can login
* User can create account
* User can pick a charityAPI/friend/amount to donate


## Optional Nice-to-have Stories

### Stretch
* Suggested Charities
  - Effetive charities
  - Charities mutual with friends
* Privacy Settings
* Liked Charities (on profile) 
* Friend requests
* Comments (and report user) - on charityAPI and feed events
* Anonymous donations
* Link to effective charities
* After signing up - add favorite charities 
* Privatley see total raised in your name


### Super Stretch
* Having charities be able to Charity
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
    - Likes on Charitys*
    - Comments on Charitys*
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
  - charityAPI
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

Class for charities - info pulled from API

### User
| Property         | Type              |  Description                                  | 
|------------------|-------------------|-----------------------------------------------|
| objectID         |String             | Unique Id for user                            |  
| userName         |String             | Unique Username                               | 
| password         |String             | Hashed Password                               |
| firstName        |String             | First Name                                    |
| lastName         |String             | Last name                                     |
| email            |String             | User email                                    |
| friends          |Array<Pointer>     | Array of pointers to friends                  | *
| favCharities     |Array<Charity>     | Array of charities(Objects)                   | *
| bio              |String             | User bio (Cause area interests)               |
| totalDonated     |Number             | Total amount personally donated               |
| totalRaised      |Number             | Total amount friends donated on behalf of them|
| financialInfo          |Pointer to Financia info   | Pointer to the credit card info   |
  


### Financial Info (credentials)
| Property         | Type             |  Description                                  | 
|------------------|------------------|-----------------------------------------------|
| objectID         |String            | Unique Id for user                            |  
| credit_card_num  |Number            | Credit Card Number                            | 
| expiration_date  |Date              | Date of Expiration for the Credit Card        |
| cvc              |Number            | Credit card code                              |
| email            |String            | User email                                    |



### Transaction History
| Property         | Type             |  Description                                  | 
|------------------|------------------|-----------------------------------------------|
| objectID         |String            | Unique Id for user transaction                |  
| donorID          |Pointer to User   | Pointer to user who donated                   |   
| friendID         |Pointer to User   | Pointer to person who was donated on behalf of|
| charityID        |Pointer to Charity| Pointer to charityAPI                            |
| amountDonated    |Number            | Amount that was donated                       |
| message          |String            | Message to friend                             |
| likesCount       |Number            | Total number of likes on the transaction      | *
| likesUsers       |Array             | Array of users who liked the transaction      | *

### Potential Model for charities

Property | Type | Description
-- | -- | --
objectID | String | Unique Id for user charityAPI
name | String | Name of charityAPI
info | String | Information on the charityAPI


--- Comments
--- Likes
--- Total 


## Goals

### Week 1

- [x] Parse models set up
- [x] Profile Page 
- [x] Sign up page
- [x] Login page
- [x] Search page layout and some functionality
- [x] Basics of API


### Week 2 
- [x] Basic flows with intents

### Halfway Point!
- [x] All layouts done

###  Week 3
- [x] Basic walkthrough ability
- [x] Most back end

### Week 4
- [x] Beta Version

### Week 5
- [ ] Cleaned
- [ ] Dogfooded
- [ ] Updated from feedback




Should do:
- [ ] Redesign transaction item - Hana
	- charity with link
- [ ] Redesign profile page (self and other)- Jessica
- [x] Make floating button solid - Hana
- [ ] Change settings icon - Jessica
- [ ] Push Notifications
- [ ] Compression for Parse - Babu
- [ ] Settings - Jessica eventually 
- [ ] Enter credit card info (make a note that it should be fake) - Hana
- [ ] Recommended charities fragment - Jessica

Stretch:
- [ ] Unlike a charity from your profile
- [ ] on your profile you can search for charities to add
- [ ] show past friends in friends search
- [ ] connect to fb fundraiser API
- [ ] comments on transactions
- [ ] comments on charities




