# Virtual Account System

## Overview

Welcome to the **Virtual Account System API** repository. This backend service 
allows user to signup, login for an account, then proceed to create a virtual account that can be used
to perform other transactions. The virtual account is a result of consuming KORA's API.

## Endpoints (Authentication)

- **POST /api/v1/auth/signup**: SignUp for an Account.
- **POST /api/v1/auth/login**: Login to the Account Created.
- **POST /api/v1/auth/refreshToken**: Refresh Access token that grants authorisation.

## Endpoints (Virtual Account System)

- **GET /api/v1/account/get-vba-details**: Get details related to your virtual account.
- **GET /api/v1/account/transactions**: View all transactions made with your account.
- **GET /api/v1/account/generateOtp**: Generates an OTP to perform account functions.
- **POST /api/v1/account/create-virtual-account**: Creates Virtual Account for a User.
- **POST /api/v1/account/credit**: Credits the Virtual Account created.
- **PUT /api/v1/account/resetPassword**: Allows a User to Reset Account Password.
- **PATCH /api/v1/account/updateProfile**: Allows a User to update their profile details.


## Contribution

Contributions are welcome! 
If you have any suggestions, improvements, or bug fixes, 
please feel free to open an issue or submit a pull request.
