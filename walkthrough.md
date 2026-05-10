# Session Handling & Room DB UI Integration

We have successfully integrated your friend's `database` branch and implemented the core authentication flow for the UniRoll learning management system.

## Summary of Changes

### 1. Database Integration
- Successfully merged the `database` branch into `master`.
- You now have access to the `AppDatabase`, `User`, `Subject`, and `Enrollment` models.

### 2. Sign Up Page
- **Role Selection:** Added a RadioGroup in `activity_sign_up.xml` allowing users to register as either a "Lecturer" or a "Student".
- **Registration Logic:** Modified `SignUpActivity.java` to use the `UserDao`. When the "Sign Up" button is pressed, the app checks if the email already exists in the Room Database. If it's available, it creates the new user and routes them back to the Login Page.

### 3. Login Page
- **Authentication:** `LoginActivity.java` now queries the `UserDao` to verify the user's email and password.
- **Session Management:** Upon a successful login, the app uses `SharedPreferences` to save a local session named "UniRollSession". It stores the `USER_ID` and `USER_ROLE`.
- **Dynamic Routing:** After successful authentication, the user is redirected to either the `LecturerDashboardActivity` or the `StudentDashboardActivity` depending on their stored role.

### 4. Dashboards & Automatic Routing
- **Dashboards:** Created two new blank screens: `LecturerDashboardActivity` and `StudentDashboardActivity`. Each has a "Logout" button that clears the `SharedPreferences` session and returns the user to the login page.
- **Auto-Login:** Updated `MainActivity.java` so that when the app launches, it immediately checks `SharedPreferences`. If a session exists, the app bypasses the login process and automatically opens the correct dashboard.

## Next Steps
You are now ready to tackle milestones 3 and 4:
- Add / Edit Subject (for lecturers).
- Student Dashboard — View available subjects.
- My Subjects (for students).

*Note: Your `master` branch is currently updated with these changes, but they haven't been pushed to GitHub yet. You can use Android Studio's VCS menu or run `git push origin master` to sync these changes online.*
