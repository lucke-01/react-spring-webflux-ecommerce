import './pollyfills';
import React from "react";
import ReactDOM from "react-dom/client";
import { AuthProvider } from "./context";
import App from "./App";
import "./i18n";
import "./scss/_global.scss";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  // <React.StrictMode>
  <AuthProvider>
    <App />
  </AuthProvider>
  // </React.StrictMode>
);
