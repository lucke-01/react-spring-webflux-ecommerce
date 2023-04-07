# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

execute: `npm install -g serve` to install local server and `serve -s build` to check the pro files (js,css etc...) works properly in local.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can't go back!**

If you aren't satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you're on your own.

You don't have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn't feel obligated to use this feature. However we understand that this tool wouldn't be useful if you couldn't customize it when you are ready for it.

## testing built app-pro in local

### `npm run build`

### `npx serve -s build`

## enviroment variables:

we have .env and .env.test files to set up env variables.

We use .env.test to override variables in testing environment

We do not use .env so far instead of that we use: /public/env/config-env.js where we will set our variables in order to be modified by external CI like helm.

priority would be: .env > config-env.js so we will use .env firstly if not set then config-env.js which allows dinamyc changes.

### public/config-env.js

is the only file which is aside from bundle.js (or main js of the app) to allow dinamic changes generally used with variables like: windows.configurationVar...