/*import { render, screen } from "@testing-library/react";
import Menu from "./Menu";
import { BrowserRouter as Router } from "react-router-dom";

jest.mock("react-i18next", () => ({
  // this mock makes sure any components using the translate hook can use it without a warning being shown
  useTranslation: () => {
    return {
      t: (str) => str,
      i18n: {
        changeLanguage: () => new Promise(() => {}),
      },
    };
  },
}));
test.skip("renders main container", () => {
  const { container } = render(
    <Router>
      <Menu />
    </Router>
  );
  // eslint-disable-next-line testing-library/no-container,testing-library/no-node-access
  const mainElement = container.getElementsByClassName("active-link");
  expect(mainElement.length).toBe(1);
});*/
