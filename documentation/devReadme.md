## Requirements:
1. **Java** version 17
1. **Maven** version used 3.8.7

## To run:
1. cd to project directory and run `./mvnw spring-boot:run`

## Postman Collection:
1. in **documentation** directory you can find a postman collection with the name of the `LibraryManagementSystemApi.json`
1. please take a look at the collection's variables that are already defined.

## Library Management System
1. All routes are protected by Authentication except (Authenticate, Register requests)

1. There are two Roles you can choose from (ADMIN, USER)
1. Register first (by running the register request) if an error occurred please refer to [problems section](#problems-with-this-solution)
1. login with credentials
1. copy the token to the `bearer_token` variables in [Postman collection](#postman-collection)
1. authenticated users are allowed to CreateReadUpdate Book & Patron entities as well as BorrowingRecords (borrow, return), Deletion of Book & Patron allowed only for ADMIN Role.
1. `data.sql` file can help you in your journey need to copy it to resources folder



## Problems with this solution
1. when Registering, can't use the same username in two consecutive requests. **username uniqueness is not validated and handled**.
1. caching
1. test coverage (need testing)
1. logging
1. exception handling all cases (create custom domain related exceptions)
1. extract jwt secret_key and expiration to properties
1. implement jwt refresh token and store tokens in db with revoke tokens functionality

