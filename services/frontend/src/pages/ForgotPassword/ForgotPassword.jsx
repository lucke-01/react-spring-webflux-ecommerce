import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { useLogin } from "../../hooks";
import "./forgotPassword.scss";
import Button from "../../components/UI/Button/Button";

const ForgotPassword = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");

  const { sendForgotPassword } = useLogin();

  const onSendEmail = (e) => {
    sendForgotPassword({
      email: email
    })
    .then((resp) => {
      toast.success("It has sent email to restore password", {
        position: "top-center",
      });
      navigate("/login");
    })
    .catch((err) => {
      toast.error("Unexpected error sending email to restore password. try it later.", {
        position: "top-center",
      });
    });
    e.preventDefault();
  };

  return (
    <div className="forgot-password-screen">
      <div className="forgot-password-screen__logo">
        <img src="/assets/images/logo.png" alt="logo" />
      </div>
      <div className="forgot-password-screen__form">
        <p className="forgot-password-screen__form--text">
          Enter your email and we will send you a link where you can reset your
          password.
        </p>

        <form>
          <input
            type="email"
            placeholder="Enter your email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <Button text="Send email" handleOnClick={onSendEmail} />
          <Link to="/login">Login</Link>
        </form>

        <p>© 2022 Ricardo Jara. All Rights Reserved</p>
      </div>
    </div>
  );
};

export default ForgotPassword;
