import { useContext } from "react";
import { NavLink } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { allowActionBaseOnRole, screens, userActions } from "utils";
import { AuthContext } from "context";

import "./Sidebar.scss";

const Sidebar = () => {
  const { t } = useTranslation();
  const { role } = useContext(AuthContext);

  const MENU_ITEMS = [
    {
      route: "/",
      icon: "bi-house",
      title: t("menu.home"),
    },
    {
      show: allowActionBaseOnRole(screens.users, role, userActions.view),
      route: "/users",
      icon: "bi-person-circle",
      title: t("menu.users"),
    },
    {
      show: allowActionBaseOnRole(screens.coupons, role, userActions.view),
      route: "/coupons",
      icon: "bi bi-tags",
      title: t("menu.coupons"),
    }
  ];

  return (
    <aside className="sidebar-component">
      <img
        className="sidebar-component__logo"
        alt="logo"
        src="/assets/images/logo.png"
      />
      <nav>
        <ul>
          {MENU_ITEMS.map(({ show = true, route, icon, title }) => {
            if (!show) return null;
            return (
              <NavLink
                to={route}
                className={({ isActive }) =>
                  isActive
                    ? "sidebar-component__item active-link"
                    : "sidebar-component__item"
                }
                key={title}
              >
                <li>
                  <i className={`bi ${icon}`} />
                  <span>{title}</span>
                </li>
              </NavLink>
            );
          })}
        </ul>
      </nav>
    </aside>
  );
};
export default Sidebar;
