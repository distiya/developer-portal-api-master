Developer Portal API
====================

## Requirements

1. Java 11
2. Docker and docker compose

## Building executable jar

Run the following command to build the project:

```
./mvnw package
```

or

```
./mvnw.cmd package
```

An executable jar (`developer-portal-api-0.0.1-SNAPSHOT.jar`) will be built in `target` directory.

Running the jar (Note it requires several environment variables to start successfully):

```
java -jar target/developer-portal-pai-0.0.1-SNAPSHOT.jar
```

The easiest way to start a dev API is by using `docker-compose`.

## Building docker image

Run the following command to build the docker image:

```
docker-compose build
```

## Starting the dev API stack using docker.

Run the following command to start a dev stack:

```
docker-compose up -d
```

Three containers will be started: API server, database and a dev mail server.

1. API server address: `localhost:8082`
2. Database address: `localhost:5432`, user: `postgres`, password: `Topcoder123`
3. Mail server UI: `http://localhost:1080`
4. Mail server SMTP: `localhost:25`, user: any, password: any.

## API Environment Variables

- `DB_URL` - database jdbc url, required. e.g. `jdbc:postgresql://db:5432/faa-notam-developer-portal`
- `DB_USER` - database username, required.
- `DB_PASS` - database password, required.
- `DB_DRIVER` - database driver class name, required. e.g. `org.postgresql.Driver`
- `MAIL_HOST` - SMTP server hostname, required.
- `MAIL_PORT` - SMTP server port, required.
- `MAIL_USER` - SMTP user, required.
- `MAIL_PASS` - SMTP password, required.
- `SERVER_PORT` - API server port, default: 8080.
- `PASSWORD_HASH_SECRET` - Password hash secret, default: change-weak-secret
- `JWT_SECRET` - JWT token signing key, base64 encoded string, should be at least 256 bits, default: xZNEEuk91OgleeDllowBLt0F48HPVh/zfRoXsRgB0Lc=
- `JWT_VALID_DURATION` - JWT token expiry time, see `java.time.Duration.parse` for the format. default: PT24H
- `PASSWORD_POLICY` - Password policy YAML resource location, will be resolved to a spring resource, default: classpath:/password-policy.yaml
- `RECAPTCHA_SECRET_KEY` - ReCaptcha server secret key, required. See `docker-compose.yaml` for a working value.
- `MAIL_FROM` - Mail from address shown to the user, default: noreply@developer-portal.notam.faa.gov
- `MAIL_FEEDBACK` - Mail feedback address to receive user feedback emails, default: support@developer-portal.notam.faa.gov
- `MAIL_TEMPLATES_PATH` - Mail templates directory, templates will be resolved as spring resources, default: classpath:/templates/email/
- `PAGINATION_LIMIT_DEFAULT` - Default page size, default: 20
- `PAGINATION_LIMIT_MAX` - Max page size, default: 1000

## Creating ReCaptcha key pair

1. Goto [Google reCAPTCHA create page](https://www.google.com/recaptcha/admin/create)
2. Enter a meaningful label.
3. Select reCAPTCHA v2
4. Add domain `localhost`
5. Submit!

Two keys will be generated:

1. Site key to be used on the web page.
2. Secret key to be used in the backend, see `RECAPTCHA_SECRET_KEY` environment variable.

## Getting a reCAPTCHA response

1. (Optional) edit the `docs/recaptcha-test.html` file to update the site key (if you are using a new pair of keys):
    
    ```
    <div class="g-recaptcha" data-sitekey="[NEW_SITE_KEY]"></div>
    ```

2. Open `docs/recaptcha-test.html` in a web browser.

3. Click `I'm not a robot`

4. Click `submit`, the page will refresh, and a response will be displayed.

5. Copy the response (it can only be used once.) and send it to the register api user endpoint.

## Password policy configuration

It's using the `passay` library for validating passwords. Currently, the code only supports a subset of the rules available:

- `Length` - min and max lengths
- `AllowedCharacter` - allowed characters in password
- `AllowedRegex` - allowed regex for password
- `Username` - password should not contain the username (email)
- `Whitespace` - password should not contain white spaces
- `Dictionary` - password should not be any of the words

Extending the rules should be straightforward (see `gov.faa.notam.developerportal.security.PasswordPolicy`).

## Junit Test 

1. For running the junit from the source folder run the command mvn test

2. For checking the junit test coverage, from the source folder open target/site/jacoco/index.html
 