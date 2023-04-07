import { useContext } from "react";
import { Route, Routes } from "react-router-dom";
import { AuthContext } from "context";
import { useLogin } from "hooks";

import "./Layout.scss";
import Coupons from "../../pages/Coupons/Coupons";
import Users from "../../pages/Users/Users";
import Home from "../../pages/Home/Home";
import Sidebar from "../UI/Sidebar/Sidebar";

function Layout() {
  const { login } = useContext(AuthContext);
  const { logout } = useLogin();

  const onLogout = () => {
    logout();
  };

  return (
    <div className="layout-screen">
      <Sidebar />
      <div>
        <main className="layout-screen__content">
          <div className="layout-screen__content--top">
            {login} |{" "}
            <span className="logout" onClick={onLogout}>
              <span className="bi bi-power"></span>
              Logout
            </span>
          </div>
          <div className="layout-screen__content--screens">
            <Routes>
              <Route exact path="/" element={<Home />} />
              <Route path="/users" element={<Users />} />
              <Route path="/coupons" element={<Coupons />} />
            </Routes>
          </div>
        </main>
      </div>
    </div>
  );
}
export default Layout;
