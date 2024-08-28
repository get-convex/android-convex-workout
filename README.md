# android-convex-workout
Sample Android Convex app using Jetpack Compose

## Getting Started

Run `npx convex dev` and setup a new Convex application (or connect to an existing one for Workout
Tracker) for the required backend functionality.

Create (or get access to) an Auth0 application configuration for a native Android app. On the Auth0
Settings page for your application, the callback and logout URLs should be set to:

```
app://{yourAuth0Domain}/android/dev.convex.workouttracker/callback
```

## Configuration

You'll need to create a `workout.properties` file in the root of the repository/project that
contains something like the following:

```
# IMPORTANT - do not quote any of the following values!
# They'll wind up as strings with quotes in them.

# IMPORTANT - this file should not be checked in.


# You can get these values from your Convex dashboard or the .env.local file for the dev_url.
convex.dev_url = https://your-dev.convex.cloud
convex.prod_url = https://your-prod.convex.cloud

# It's fine to leave this as app; you'll need to use it in your callback URLs in the Auth0 config.
auth0.scheme = app

# You can get these values from your Auth0 Application Settings.
auth0.dev_domain = your-dev-domain.us.auth0.com
auth0.dev_client_id = DEV_CLIENT_ID

# If you don't have a prod deployment, you can reuse your dev values here.
auth0.prod_domain = your-prod-domain.us.auth0.com
auth0.prod_client_id = PROD_CLIENT_ID
```

On your Convex dashboard for the application, add environment variables for `AUTH0_DOMAIN` and
`AUTH0_CLIENT_ID`. See the
[Auth0 integration docs](https://docs.convex.dev/auth/auth0#configuring-dev-and-prod-tenants) for
more information.
