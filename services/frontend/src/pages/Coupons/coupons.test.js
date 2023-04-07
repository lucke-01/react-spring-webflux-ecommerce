/* eslint-disable testing-library/no-node-access */
/* eslint-disable testing-library/no-container */
import { render, screen, cleanup, waitFor } from "@testing-library/react";
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router} from "react-router-dom";
import { rest} from 'msw';
import { setupServer } from 'msw/node';
import { MOCK_COUPONS_LIST } from "mocks";
import {render as renderWithContext} from "tests/test";
import { handlers } from "mocks/handlers";
import Coupons from "./Coupons";

const couponsPath = '/api/coupon';
// declare which API requests to mock
const server = setupServer(
    ...handlers,
    // capture POST requests
    rest.get(couponsPath, (req, res, ctx) => {
        // respond using a mocked JSON body
        return res(ctx.json(MOCK_COUPONS_LIST));
    }),
    rest.get(couponsPath + "/*", (req, res, ctx) => {
        // respond using a mocked JSON body
        return res(ctx.json(MOCK_COUPONS_LIST[0]));
    })    
);
//MOCKS
//END MOCKS
describe("coupon list test",  () => {
    // establish API mocking before all tests
    beforeAll(() => server.listen());
    beforeEach(() => {cleanup();});
    // reset any request handlers that are declared as a part of our tests
    afterEach(() => server.resetHandlers());
    // clean up once the tests are done
    afterAll(() => server.close());

    test("should list coupons", async () => {
        //GIVEN
        const userActions = userEvent.setup();
        //render component
        const { container } = render(
            <Router>
                <Coupons></Coupons>
            </Router> 
        );
        const rows = screen.getAllByRole('row');

        //WHEN coupons are loaded

        //THEN
        await waitFor(() => {
            expect(rows).toHaveLength(MOCK_COUPONS_LIST.length);
        });
    });

    test("show create modal", async () => {
        // GIVEN
        const userActions = userEvent.setup();
        const { container } = renderWithContext(<Coupons></Coupons>);
        const createCouponButton = screen.getByText("Create Coupon");
        // WHEN
        await userActions.click(createCouponButton);

        // THEN
        const codeField = screen.getByLabelText(/code$/i);
        expect(createCouponButton).toBeInTheDocument();
        expect(codeField).toBeInTheDocument();
      });

      test("Show the edit modal", async () => {
        const userActions = userEvent.setup();
        const { container } = renderWithContext(<Coupons></Coupons>);

        const editButton = await screen.findAllByRole("tooltip", {
          name: /edit$/i,
        });
    
        expect(editButton[0]).toBeInTheDocument();
        await userActions.click(editButton[0]);
        const createUserEmailField = await screen.findByLabelText(/code$/i);
        expect(createUserEmailField).toBeInTheDocument();
        await waitFor(() => {
            expect(screen.getByDisplayValue(MOCK_COUPONS_LIST[0].code)).toBeInTheDocument();
        });
      });
});