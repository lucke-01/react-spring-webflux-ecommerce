import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { useParams } from "react-router-dom";
import { useLogin } from "../../hooks";
import "./restorePassword.scss";
import Button from "../../components/UI/Button/Button";

const RestorePassword = () => {
  const { passwordToken } = useParams();
  const restorePasswordForm = {
        login: "",
        newPassword: ""
  };
  const navigate = useNavigate();
  
  const [restorePassword, setRestorePassword] = useState(restorePasswordForm);

  const { sendRestorePassword } = useLogin();

  const onsendRestorePassword = (e) => {
    const restorePasswordReq = {
        ...restorePassword, 
        passwordToken: passwordToken
    };
    sendRestorePassword(restorePasswordReq)
    .then((resp) => {
      toast.success("new password has been set successfully", {
        position: "top-center",
      });
      navigate("/login");
    })
    .catch((err) => {
        let message = "Unexpected error setting new password. try it later.";
        if (err.code === "ERR_BAD_REQUEST") {
            message = err.response.data;
        }
        toast.error(message, {position: "top-center"});
    });
    e.preventDefault();
  };
  const onChange = (e) => {
    setRestorePassword({
      ...restorePassword,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="forgot-password-screen">
      <div className="forgot-password-screen__logo">
        <img src="/assets/images/logo.png" alt="logo" />
      </div>
      <div className="forgot-password-screen__form">
        <p className="forgot-password-screen__form--text">
          Enter your login and a new password to reset it
        </p>
        <form>
          <label>Login</label><br/>
          <input
            type="text"
            placeholder="Enter your login"
            name="login"
            value={restorePassword.login}
            onChange={onChange} />
          
          <label>New password</label><br/>
          <input
            type="password"
            placeholder="Enter your new password"
            name="newPassword"
            value={restorePassword.newPassword}
            onChange={onChange} />
          <Button text="Restore password" handleOnClick={onsendRestorePassword} />
          <Link to="/login">Login</Link>
        </form>

        <p>© 2022 Ricardo Jara. All Rights Reserved</p>
      </div>
    </div>
  );
};

export default RestorePassword;
