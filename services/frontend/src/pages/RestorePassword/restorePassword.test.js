/* eslint-disable testing-library/no-node-access */
/* eslint-disable testing-library/no-container */
import { render, screen, cleanup } from "@testing-library/react";
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router} from "react-router-dom";
import { rest} from 'msw';
import { setupServer } from 'msw/node';
import RestorePassword from "./";

const restorePasswordPath = '/api/administrator/restore-password';
// declare which API requests to mock
const server = setupServer(
    // capture POST requests
    rest.post(restorePasswordPath, (req, res, ctx) => {
        // respond using a mocked JSON body
        return res(ctx.status(200));
    }),
);
//MOCKS
const mockedToastSucess = jest.fn();
const mockedToastError = jest.fn();
jest.mock('react-hot-toast', () => ({
    success: () => mockedToastSucess(),
    error: () => mockedToastError(),
}));
const mockedUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate,
}));
//END MOCKS
describe("restore password test",  () => {
    // establish API mocking before all tests
    beforeAll(() => server.listen());
    beforeEach(() => {cleanup();});
    // reset any request handlers that are declared as a part of our tests
    afterEach(() => server.resetHandlers());
    // clean up once the tests are done
    afterAll(() => server.close());
    test("should restore password", async () => {
        //GIVEN
        const userActions = userEvent.setup();
        //render component
        const { container } = render(
            <Router>
            <RestorePassword></RestorePassword>
            </Router>
        );
        //fill up data
        const loginInput = container.querySelector(`input[name="login"]`);
        const newPasswordInput = container.querySelector(`input[name="newPassword"]`);
        await userActions.type(loginInput, 'test');
        await userActions.type(newPasswordInput, 'test');
    
        //WHEN send restore password
        await userActions.click(screen.getByText('Restore password'));
    
        //THEN
        expect(mockedUsedNavigate).toBeCalled();
        expect(mockedToastSucess).toBeCalled();
        expect(mockedToastError).not.toBeCalled();
    });
    test("should not restore password not found", async () => {
        //GIVEN
        server.use(
            rest.post(restorePasswordPath, (req, res, ctx) => {
                // respond using a mocked JSON body
                return res(ctx.status(404));
            })
        );
        const userActions = userEvent.setup();
        //render component
        const { container } = render(
            <Router>
            <RestorePassword></RestorePassword>
            </Router>
        );
        //fill up data
        const loginInput = container.querySelector(`input[name="login"]`);
        const newPasswordInput = container.querySelector(`input[name="newPassword"]`);
        await userActions.type(loginInput, 'test');
        await userActions.type(newPasswordInput, 'test');
    
        //WHEN send restore password
        await userActions.click(screen.getByText('Restore password'));
    
        //THEN
        expect(mockedUsedNavigate).not.toBeCalled();
        expect(mockedToastSucess).not.toBeCalled();
        expect(mockedToastError).toBeCalled();
    });
    test("should not restore password server error", async () => {
        //GIVEN
        server.use(
            rest.post(restorePasswordPath, (req, res, ctx) => {
                // respond using a mocked JSON body
                return res(ctx.status(500));
            })
        );
        const userActions = userEvent.setup();
        //render component
        const { container } = render(
            <Router>
            <RestorePassword></RestorePassword>
            </Router>
        );
        //fill up data
        const loginInput = container.querySelector(`input[name="login"]`);
        const newPasswordInput = container.querySelector(`input[name="newPassword"]`);
        await userActions.type(loginInput, 'test');
        await userActions.type(newPasswordInput, 'test');
    
        //WHEN send restore password
        await userActions.click(screen.getByText('Restore password'));
    
        //THEN
        expect(mockedUsedNavigate).not.toBeCalled();
        expect(mockedToastSucess).not.toBeCalled();
        expect(mockedToastError).toBeCalled();
    });
});

