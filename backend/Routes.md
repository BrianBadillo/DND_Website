## GET /users
### Purpose:
all users in Group
### Request format: 

    {
        "mGroupId": int,
        "mUserId": int
    }

#### Fields: 
* *mGroupId* - The ID of the group for which users are requested.
* *mUserId* - The ID of the user requesting

### Response format: 

    {
        "mStatus": String,
        "mMessage": String,
        "mData": JSON object
        {
            "mUsers": list of JSON
                [
                    {
                        "mUserId": "int",
                        "mUsername": "string",
                        "mEmail": "string"
                    },
                ]
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mMessage* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mUsers* - A list of all user and their attributes
        * *mUserId* - user's id
        * *mUserName* - Username of user
        * *mEmail* - Email of user

## GET /items/shared
### Purpose:
all shareable items in group
### Request format: 

    {
        "mGroupId": int,
        "mUserId": int
    }

#### Fields: 
* *mGroupId* - The ID of the group for which users are requested.
* *mUserId* - The ID of the user requesting

### Response format: 

    {
        "mStatus": "string",
        "mMessage": "string",
        "mData": 
        {
            "mItems": 
            [
                {
                    "mItemId": "int",
                    "mItemName": "string",
                    "mItemDescription": "string",
                    "mCharacterId": "int"
                },
            ]
        }
    }
    
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mMessage* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mItems* - A list of all items and their attributes
        * *mItemId* - id of items
        * *mItemName* - name of item
        * *mItemDescription* - description of item
        * *mCharacterId* - character id for item

## GET /items/{id}
### Purpose:
all items from character
### Request format: 

    {
        "mUserId": int
    }

#### Fields: 
* *mUserId* - The ID of the user requesting

### Response format: 

    {
        "mStatus": "string",
        "mMessage": "string",
        "mData": 
        {
            "mItems": 
            [
                {
                    "mItemId": "int",
                    "mItemName": "string",
                    "mItemDescription": "string",
                    "mCharacterId": "int"
                },
            ]
        }
    }
    
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mMessage* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mItems* - A list of all items and their attributes
        * *mItemId* - id of items
        * *mItemName* - name of item
        * *mItemDescription* - description of item
        * *mCharacterId* - character id for item
## GET /groups
### Purpose:
all Groups
### Request format: 

    {
        "mUserId": int
    }

#### Fields: 
* *mUserId* - The ID of the user requesting the groups.

### Response format: 

    {
        "mStatus": "string",
        "mMessage": "string",
        "mData": 
        {
            "mGroups": 
            [
                {
                    "mGroupId": "int",
                    "mGroupName": "string",
                    "mCreatedBy": "int"
                },
            ]
        }
    }
    
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error."
* *mMessage* - Error message (null if mStatus is "ok").
* *mData* - The data being provided in the response.
  * *mGroups* - A list of all groups and their attributes.
    * *mGroupId* - The unique ID of the group.
    * *mGroupName* - The name of the group.
    * *mCreatedBy* - The ID of the user who created the group.


## GET /characters
### Purpose:
all characters from user
### Request format: 

    {
        "mUserId": int
    }

#### Fields: 
* *mUserId* - The ID of the user requesting the characters.

### Response format: 

    {
    "mStatus": "string",
    "mMessage": "string",
    "mData": 
    {
        "mCharacters": 
        [
            {
                "mCharacterId": "int",
                "mName": "string",
                "mProfilePicture": "string",
                "mGroupId": "int"
            },
        ]
    }
}

#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error."
* *mMessage* - Error message (null if mStatus is "ok").
* *mData* - The data being provided in the response.
  * *mCharacters* - A list of all characters and their attributes.
    * *mCharacterId* - The unique ID of the character.
    * *mName* - The name of the character.
    * *mProfilePicture* - A link to the character's profile picture.
    * *mGroupId* - The ID of the group the character belongs to.


## POST /items
### Purpose: 
add a new item
### Request format: 

    {
    
    }

#### Fields: 
* 

### Response format: 

    {
        
    }

#### Fields: 
*

## POST /characters
### Purpose: 
make a new character
### Request format: 

    {
        
    }

#### Fields: 
* 

### Response format: 

    {
        
    }

#### Fields: 
*

## PUT /items
### Purpose: 
edit an item 
### Request format: 

    {
        
    }

#### Fields: 
* 

### Response format: 

    {
        
    }

#### Fields: 
*

## PUT /characters
### Purpose: 
edit a character
### Request format: 

    {
    "character_id": "string",
    "name": "string",
    "profile_picture": "string",
    "group_id": "string"
    }

#### Fields: 
* 

### Response format: 

    {
        
    }

#### Fields: 
*
