# sandbox movementtracker
> Backend service for handling requests regarding geo locations.

Sandbox for playing with payara-micro and gradle

## Install
Only tested on windows for now, but should work on unix as well. Java 8 SDK is required and should be accesible from console.

Installs local gradle instance + builds project + starts payara micro on port 8080:
```bash
$ gradlew runMicro
```

Crawls over the tesdata csv and adds all entities via service calls.
```bash
$ gradlew initTestData
```

## Usage

Check http://localhost:8080/movement/swagger/ for API documentation and checkout the 'movement-tracker.postman_collection.json' file where all possible api calls are contained in a collection to be imported in postman.

Or check the API here out:
```
GET     /movement/api/interim/auth/allroles
GET     /movement/api/v1/vins
GET     /movement/api/v1/vins/{vin}/position
GET     /movement/api/v1/vins/{vin}/sessions
GET     /movement/api/v1/vins/{vin}/sessions/{session}/locations
POST    /movement/api/v1/vins/{vin}/sessions/{session}/locations
```
