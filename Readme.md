## Prerequisites
* jdk 11
* mvn 3.5.3

## dependencies
embed tomcat: include embed web server to be a self-contained project
junit: for unit test purpose
jackson: for converting from json string to object

## plugin
spotless: for code format

## Doc
### Model
#### User

| field | type | comment |
| :----: | :----: | :----: |
| username | String | |
| passwordSalt | byte[] | random salt |
| passwordHash | byte[] | The calculation method is hash(passwordSalt + originalPassword) by SHA-256 |
| roles | Set\<String\> | all role names associated with the user |

#### Role

| field | type | comment |
| :----: | :----: | :----: |
| roleName | String | |

#### Token

| field | type | comment |
| :----: | :----: | :----: |
| name | String | authentication token |
| username | String | owned by which user|
| expiredAt | Long | expired at which milliseconds |

### API
#### Create user
```shell script
curl -X POST 'localhost:8081/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "jasper",
    "password": "312010"
}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "User created"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if user not exist
```json
{
    "code": "UserAlreadyExist",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Delete user
```shell script
curl -X DELETE 'localhost:8081/api/v1/users/{user}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "User deleted"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if user not exist
```json
{
    "code": "UserNotExist",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Add a role to user
```shell script
curl -X PUT 'localhost:8081/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "jasper",
    "roleName": "analyst"
}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "User deleted"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if user not exist
```json
{
    "code": "UserNotExist",
    "message": "xxx"
}
```

Http code 400 if role not exist
```json
{
    "code": "RoleNotExist",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Check role
```shell script
curl -X GET 'localhost:8081/api/v1/users?token=1111&roleName=admin'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "Role checked"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if token is invalid or expired
```json
{
    "code": "TokenInvalidOrExpired",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Login
```shell script
curl -X POST 'localhost:8081/api/v1/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "jasper",
    "password": "123456789"
}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "User login"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if authentication failed
```json
{
    "code": "AuthenticationFailed",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Logout
```shell script
curl -X POST 'localhost:8081/api/v1/logout' \
--header 'Content-Type: application/json' \
--data-raw '{
    "token": "111111"
}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "User login"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if token is invalid or expired
```json
{
    "code": "TokenInvalidOrExpiredException",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Create role
```shell script
curl -X POST 'localhost:8081/api/v1/roles' \
--header 'Content-Type: application/json' \
--data-raw '{
    "roleName": "admin"
}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "Role created"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if role already exist
```json
{
    "code": "RoleAlreadyExistException",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### Delete role
```shell script
curl -X DELETE 'localhost:8081/api/v1/roles/{role}'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "Role deleted"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if role not exist
```json
{
    "code": "RoleNotExist",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

#### list roles
```shell script
curl -X GET 'localhost:8081/api/v1/roles?token=1111111'
```

Http code 200 if success
```json
{
    "code": "SUCCESS",
    "message": "Role list"
}
```

Http code 400 if argument is illegal
```json
{
    "code": "IllegalArgumentException",
    "message": "xxx"
}
```

Http code 400 if token is invalid or expired
```json
{
    "code": "TokenInvalidOrExpired",
    "message": "xxx"
}
```

Http code 500 if internal server error
```json
{
    "code": "InternalServerError",
    "message": "xxx"
}
```

## TODO
* unit test for servlet
* unit test for InMemoryDatabase
* extract base64 logic for authentication token
* expire token when access it, not only by scheduler
* split InMemoryDatabase to smaller class
* more comment
* simple ioc implementation
* Configuration read profiles and convert inner properties to a type validated class
* extract message string to central class
* limit memory of InMemoryDatabase and evict least recently used data
