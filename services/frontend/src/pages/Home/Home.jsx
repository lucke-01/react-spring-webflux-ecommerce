import { useContext } from "react";
import { useTranslation } from "react-i18next";
import { AuthContext } from "context";
import { useNavigate } from "react-router-dom";
import { allowActionBaseOnRole, screens, userActions } from "utils";
import Option from "../../components/UI/Option/Option";
import "./home.scss";

const Home = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { role } = useContext(AuthContext);

  const OPTIONS = [
    {
      show: allowActionBaseOnRole(screens.users, role, userActions.view),
      description: t("home.addEdit"),
      title: t("menu.users"),
      icon: "bi-person-square",
      route: "/users",
    },
    {
      show: allowActionBaseOnRole(screens.coupons, role, userActions.view),
      description: t("home.addView"),
      title: t("menu.coupons"),
      icon: "bi-tags",
      route: "/coupons",
    }
  ];

  const onClickNavigate = (route) => {
    navigate(route);
  };

  return (
    <div className="home-screen screen__wrapper">
      <p className="home-screen__description">{t("home.dashboard")}</p>
      <p className="home-screen__title">{t("home.title")}</p>
      <section className="options__row">
        {OPTIONS.map((option) => {
          if (!option.show) return null;
          return (
            <Option
              key={option.title}
              option={option}
              handleOnClick={() => onClickNavigate(option?.route)}
            />
          );
        })}
      </section>
    </div>
  );
};

export default Home;
