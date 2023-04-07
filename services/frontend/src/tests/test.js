import { render } from "@testing-library/react";
import { I18nextProvider } from "react-i18next";
import { AuthContext } from "context";
import i18n from "./i18nForTesting";
import { MemoryRouter } from "react-router-dom";
import { roles } from "utils";

const TEST_CONTEXT_STATE = {
  role: roles.admin
};
const badRoute = "";

const renderWithContext = (ui, options) =>
  render(
    <MemoryRouter initialEntries={[badRoute]}>
      <AuthContext.Provider value={TEST_CONTEXT_STATE}>
        <I18nextProvider i18n={i18n}>{ui}</I18nextProvider>
      </AuthContext.Provider>
    </MemoryRouter>,
    {
      ...options,
    }
  );

export * from "@testing-library/react";
export { renderWithContext as render };
