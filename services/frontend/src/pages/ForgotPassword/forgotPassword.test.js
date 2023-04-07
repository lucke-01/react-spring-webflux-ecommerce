import { screen, cleanup } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { render } from "tests/test";
import ForgotPassword from "./ForgotPassword";

// declare which API requests to mock
const forgotPasswordPath = "/api/administrator/send-forgot-password";
const server = setupServer(
  // capture POST send-forgot-password requests
  rest.post(forgotPasswordPath, (req, res, ctx) => {
    // respond using a mocked JSON body
    return res(ctx.status(200));
  })
);
//MOCKS
const mockedToastSucess = jest.fn();
const mockedToastError = jest.fn();
jest.mock("react-hot-toast", () => ({
  success: () => mockedToastSucess(),
  error: () => mockedToastError(),
}));
const mockedUsedNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockedUsedNavigate,
}));
//END MOCKS

describe("forgot password test", () => {
  // establish API mocking before all tests
  beforeAll(() => server.listen());
  beforeEach(() => {
    cleanup();
  });
  // reset any request handlers that are declared as a part of our tests
  afterEach(() => server.resetHandlers());
  // clean up once the tests are done
  afterAll(() => server.close());
  test("should send email", async () => {
    //GIVEN
    const userActions = userEvent.setup();
    //render component
    render(<ForgotPassword />);
    //fill email
    const emailInput = screen.queryByPlaceholderText(/Enter your email/i);
    await userActions.type(emailInput, "test@email.com");

    //WHEN send email
    await userActions.click(screen.getByText("Send email"));

    //THEN
    expect(mockedUsedNavigate).toBeCalled();
    expect(mockedToastSucess).toBeCalled();
    expect(mockedToastError).not.toBeCalled();
  });

  test("should given error sending email", async () => {
    //GIVEN
    // POST send-forgot-password requests error
    server.use(
      rest.post(forgotPasswordPath, (req, res, ctx) => {
        // respond using a mocked JSON body
        return res(ctx.status(500));
      })
    );
    const userActions = userEvent.setup();
    //render component
    render(<ForgotPassword />);
    //fill email
    const emailInput = screen.queryByPlaceholderText(/Enter your email/i);
    await userActions.type(emailInput, "test@email.com");

    //WHEN send email
    await userActions.click(screen.getByText("Send email"));
    //THEN
    expect(mockedToastError).toBeCalled();
    expect(mockedToastSucess).not.toBeCalled();
    expect(mockedUsedNavigate).not.toBeCalled();
  });
});
