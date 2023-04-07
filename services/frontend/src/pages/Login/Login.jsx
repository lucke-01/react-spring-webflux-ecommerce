import { Link } from "react-router-dom";
import { Controller, useForm } from "react-hook-form";
import { Alert } from "@mui/material";
import { useLogin } from "hooks";
import { RULE_REQUIRED } from "utils/validation.utils";

import "./login.scss";
import Input from "../../components/UI/Input/Input";
import Button from "../../components/UI/Button/Button";

const Login = () => {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    mode: "onSubmit",
    defaultValues: {
      login: "",
      password: "",
    },
  });

  const { loginUser, error, isLoading } = useLogin();

  const onLogin = (user) => loginUser(user);

  return (
    <div className="login-screen">
      <div className="login-screen__logo">
        <img src="/assets/images/logo.png" alt="logo" />
      </div>
      <div className="login-screen__login">
        <form onSubmit={handleSubmit(onLogin)}>
          {error ? <Alert severity="warning">{error}</Alert> : null}
          <Controller
            name="login"
            control={control}
            rules={{ ...RULE_REQUIRED }}
            render={({ field }) => (
              <Input
                label="Username"
                error={!!errors?.login}
                helperText={errors?.login?.message}
                {...field}
              />
            )}
          />
          <Controller
            name="password"
            control={control}
            rules={{ ...RULE_REQUIRED }}
            render={({ field }) => (
              <Input
                type="password"
                label="Password"
                error={!!errors?.password}
                helperText={errors?.password?.message}
                {...field}
              />
            )}
          />
          <div className="login-screen__login--buttons">
            <Button text="Login" loading={isLoading} />
            <Link to="/forgotpassword">Forgot Password?</Link>
          </div>
        </form>

        <p>© 2022 Ricardo Jara. All Rights Reserved</p>
      </div>
    </div>
  );
};

export default Login;
