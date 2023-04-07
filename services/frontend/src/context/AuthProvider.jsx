import React from "react";
import { AuthContext, authReducer, types } from "./";
import { useReducer } from "react";
import { roles } from "utils";

const initialState = {
  logged: false,
  role: roles.admin,
};

export const AuthProvider = ({ children }) => {
  const [authState, dispatch] = useReducer(authReducer, initialState);

  return (
    <AuthContext.Provider value={{ ...authState, dispatch, types }}>
      {children}
    </AuthContext.Provider>
  );
};
