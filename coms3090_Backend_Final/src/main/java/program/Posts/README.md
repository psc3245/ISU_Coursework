In order to create a new post type there is several things you must do:
- Make a new Post type which extends Post
- Make a new PostDTO class which extends PostDTO
- Add the post type to the PostTypes.java enum
- Add the DiscriminatorValue annotation to your new post file with the same string as your addition to the ENUM file.
- Now to facilitate creation and updating of the posts you must create both a creation request and update request for your post type
- to create a creation request you must extend PostCreationRequest and then add your new post type to the @JsonSubTypes annotation in PostCreationRequest.
- Now for updates, do the same exact thing except with PostUpdateRequest
- The final step is going through the PostService and adding your enum value to the switch-case statements in updatePost(), createPost(), and convertPostToRelevantPostDTO()





# Post Creation API - README

## Overview

This project is a part of a larger application that allows users to create different types of posts. The system supports three main post types:

1. **Auction**  
2. **Item Listing**  
3. **Trade**

The posts can be created through a RESTful API, and each post has a `title`, `description`, and a `userId`. Based on the type specified, the system will create a specific subclass of `Post`.

---

## Technologies Used

- **Java**: Backend language
- **Spring Boot**: Framework for building the application
- **Jackson**: For JSON processing and serialization
- **JPA (Java Persistence API)**: For interacting with the database
- **H2 Database**: In-memory database for development

---

## Post Creation Request

### Request Body

To create a post, the client needs to send a JSON request body with the required information. The following fields are accepted:

- **`title`** (String): Title of the post
- **`description`** (String): Detailed description of the post
- **`userId`** (Integer): ID of the user creating the post
- **`type`** (String): Type of the post. Possible values: `"AUCTION"`, `"ITEM_LISTING"`, `"TRADE"`.
- **`price`** (Integer): Price for `Item Listing` posts (optional for other post types)

### Example JSON:

**Item Listing Example:**

```json
{
  "title": "Brand Laptop for Sale",
  "description": "Selling a brand new laptop, barely used with all accessories included.",
  "userId": 1,
  "price": 900,
  "type": "ITEM_LISTING"
}