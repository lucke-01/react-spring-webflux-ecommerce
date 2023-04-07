import { useContext, useState } from "react";
import { AuthContext } from "context";
import { BASE_API_URL } from "utils";
import axios from "axios";

const useLogin = () => {
  const loginInitialState = {
    login: "",
    token: "",
    superAdmin: "",
    isLoading: false,
  };

  const { dispatch, types } = useContext(AuthContext);

  const [state, setState] = useState(loginInitialState);

  const url = `${BASE_API_URL}/administrator/login`;
  const urlAdmin = `${BASE_API_URL}/administrator`;

  const loginUser = async (data) => {
    setState({
      ...state,
      isLoading: true,
    });

    await axios
      .post(url, data)
      .then(async ({ data }) => {
        setState({
          ...state,
          ...data,
          isLoading: false,
        });
        dispatch({ type: types.login, payload: data });
      })
      .catch(({ response }) => {
        setState({
          ...state,
          isLoading: false,
          error: response.data,
        });
      });
  };
  const sendForgotPassword = async (forgotPassword) => {
    return await axios.post(urlAdmin + "/send-forgot-password", forgotPassword);
  };
  const sendRestorePassword = async (restorePassword) => {
    return await axios.post(urlAdmin + "/restore-password", restorePassword);
  };
  const logout = () => {
    dispatch({ type: types.logout, payload: {} });
  };

  return {
    ...state,
    loginUser,
    sendForgotPassword,
    sendRestorePassword,
    logout,
  };
};

export default useLogin;
