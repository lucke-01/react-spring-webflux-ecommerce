import { screen } from "@testing-library/react";
import useEvent from "@testing-library/user-event";
import { TEST_USER_LIST } from "mocks/mocks.utils";
import { server } from "mocks/server";
import { render } from "tests/test";
import Users from "./Users";

describe("Users test", () => {
  beforeAll(() => server.listen());
  afterEach(() => server.resetHandlers());
  afterAll(() => server.close());

  test("Display the create user modal", async () => {
    const user = useEvent.setup();
    render(<Users />);

    const createUserButton = await screen.findByRole("button", {
      name: /create user$/i,
    });

    expect(createUserButton).toBeInTheDocument();

    await user.click(createUserButton);

    const createUserEmailField = screen.getByLabelText(/email$/i);

    expect(createUserEmailField).toBeInTheDocument();
  });

  test("Display the edit user modal", async () => {
    const user = useEvent.setup();
    render(<Users />);

    const editUserButton = await screen.findByRole("tooltip", {
      name: /edit$/i,
    });

    expect(editUserButton).toBeInTheDocument();

    await user.click(editUserButton);

    const createUserEmailField = await screen.findByLabelText(/email$/i);

    expect(createUserEmailField).toBeInTheDocument();

    expect(
      screen.getByDisplayValue(TEST_USER_LIST[0].email)
    ).toBeInTheDocument();
  });

  test("Display the confirm delete user modal", async () => {
    const user = useEvent.setup();
    render(<Users />);

    const deleteUserButton = await screen.findByRole("tooltip", {
      name: /delete$/i,
    });

    expect(deleteUserButton).toBeInTheDocument();

    await user.click(deleteUserButton);

    const confirmDeleteUser = await screen.findByRole("button", {
      name: /delete$/i,
    });

    expect(confirmDeleteUser).toBeInTheDocument();
  });
});
