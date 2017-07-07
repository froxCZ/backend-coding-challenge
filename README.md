### How to run

#### Frontend
in `frontend` dir run `gulp`
 
#### Backend
- First set db properties in `backend/src/main/resources/application-dev.properties`
- then from `backend` dir run `mvn spring-boot:run`
- API is running on `localhost:8081`

## Implementation
All stories implemented. 
- Story 3 (currency exchange): Not allowing expenses in EUR for days later than tomorrow. (Allowing tomorrow since users can be in timezone ahead of server timezone.)
- Added `ng-message` validation on frontend, but it would still need some more work to better style it. 

