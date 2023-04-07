import { types } from ".";

export const authReducer = (state = {}, action) => {
  switch (action.type) {
    case types.login:
      return { ...state, logged: true, ...action.payload };
      case types.logout:
        return { ...state, logged: false, ...action.payload };
    default:
      return state;
  }
};
